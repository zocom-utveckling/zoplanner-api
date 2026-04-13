package com.zo.webapi.controller;

import com.zo.webapi.dto.CreateUserRequestDTO;
import com.zo.webapi.dto.ProfilePictureResponseDTO;
import com.zo.webapi.exception.FileStorageException;
import com.zo.webapi.model.User;
import com.zo.webapi.service.UserService;
import jakarta.validation.Valid;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final GenericResponseService responseBuilder;

    public UserController(UserService userService, GenericResponseService responseBuilder) {
        this.userService = userService;
        this.responseBuilder = responseBuilder;
    }

    //Gets all users
    @GetMapping
    public ResponseEntity<?> showAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving users" + e.getMessage());
        }
    }

    //database: not null missing -- null and empty data gets accepted.
    //Creates a user
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        try {
            if (userService.usernameExists(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username already exists " + request.getUsername());
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setRole(request.getRole());
            user.setCity(request.getCity());

            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);


        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid user data: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding user: " + e.getMessage());
        }

    }

    //database: not null missing -- null and empty data gets accepted.
    //Updates all columns
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            User updateUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updateUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }

    }



    //Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
            /*return ResponseEntity.ok("User with id " + id + " deleted successfully.");*/
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id " + id + " not found: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }

    }

    //Get one user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id " + id + " not found: " + e.getMessage());
        }
    }

    //Get one user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with name " + username + " not found: " + e.getMessage());
        }
    }

    @PostMapping(value = "/{id}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfilePictureResponseDTO> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file){

        try {
            ProfilePictureResponseDTO response = userService.uploadProfilePicture(id, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new FileStorageException("Failed to upload profile picture: " + e.getMessage(), e);
        }
    }
}
