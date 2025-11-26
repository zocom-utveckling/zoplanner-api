package com.zo.webapi.service;

import com.zo.webapi.dto.UserPatchDTO;
import com.zo.webapi.model.User;
import com.zo.webapi.model.UserRole;
import com.zo.webapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    //TODO:
    // private final ManagerRepository managerRepository;
    // private final ConsultantRepository consultantRepository;

    //Constructor
    public UserService(UserRepository userRepository
                        /*, ManagerRepository managerRepository,
                        ConsultantRepository consultantRepository*/) {

        this.userRepository = userRepository;
        // this.managerRepository = managerRepository;
        // this.consultantRepository = consultantRepository
    }

    //Gets all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Get one user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

    }

    //Get one user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with name " + username));
    }

    //Check if username already exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    //Creates a user
    public User createUser(User user) {
        User savedUser = userRepository.save(user);

        // -----------------------------
        // TODO: Create entries in managers / consultants
        // if (user.getRole() == UserRole.MANAGER) {...}
        // if (user.getRole() == UserRole.CONSULTANT) {...}
        // if (user.getRole() == UserRole.BOTH) {...}
        //------------------------------

        return savedUser;
    }

    //Updates all columns in a user
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setName(userDetails.getName());

        UserRole oldRole = user.getRole();
        UserRole newRole = userDetails.getRole();
        user.setRole(newRole);

        User updatedUser = userRepository.save(user);

        // --------------------------
        // TODO: Handle role changes
        // handleRoleChanges(updateUser, oldRole, newRole);
        // --------------------------

        return updatedUser;
    }

    public User patchUser(Long id, UserPatchDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        UserRole oldRole = user.getRole();

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getRole() != null) user.setRole(dto.getRole());

        User updatedUser = userRepository.save(user);

        // TODO: handle role changes
        // handleRoleChanges(updateUser, oldRole, updatedUser.getRole());

        return updatedUser;
    }

    //Deletes a user
    public void deleteUser(Long id) {
       if (!userRepository.existsById(id)) {
           throw new IllegalArgumentException("User not found with id " + id);
       }

       // ---------------------------
        // TODO: Delete related manage / consultant
        // --------------------------

        userRepository.deleteById(id);

    }

    // Private helper for role changes
    /*
        private void handleRoleChanges(User user, UserRole oldRole, UserRole newRole) {
            // TODO: Implement role sync
        }
     */



}
