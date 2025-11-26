package com.zo.webapi.controller;

import com.zo.webapi.dto.UserPatchDTO;
import com.zo.webapi.model.User;
import com.zo.webapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Gets all users
    @GetMapping
    public ResponseEntity<List<User>> showAllUsers() {
       List<User> users = userService.getAllUsers();
       return ResponseEntity.ok(users);
    }

    //database: not null missing -- null and empty data gets accepted.
    //Creates a user
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
       if (userService.usernameExists(user.getUsername())) {
           return ResponseEntity.status(HttpStatus.CONFLICT)
                   .body("Username already exists: " + user.getUsername());
       }

       User createdUser = userService.createUser(user);
       return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //Updates all columns
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);

    }

    //Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //Get one user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    //Get one user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody UserPatchDTO dto) {
       User patchedUser = userService.patchUser(id, dto);
       return ResponseEntity.ok(patchedUser);
    }
}
