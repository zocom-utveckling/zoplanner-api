package com.zo.webapi.service;

import com.zo.webapi.model.User;
import com.zo.webapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    //Constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Gets all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Creates a user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    //Updates all columns in a user
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setRole(userDetails.getRole());
        user.setCity(userDetails.getCity());
        user.setName(userDetails.getName());

        return userRepository.save(user);
    }

    //Deletes a user
    public void deleteUser(Long id) {
       if (!userRepository.existsById(id)) {
           throw new IllegalArgumentException("User not found with id " + id);
       }
       userRepository.deleteById(id);
    }

    //Get one user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

    }

    //Check if username already exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    //Get one user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with name " + username));
    }

}
