package com.zo.webapi.service;

import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final ConsultantRepository consultantRepository;
    private final ManagerService managerService;
    private final ConsultantService consultantService;

    //Constructor
    public UserService(UserRepository userRepository, ManagerRepository managerRepository, ConsultantRepository consultantRepository, ManagerService managerService, ConsultantService consultantService) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.consultantRepository = consultantRepository;
        this.managerService = managerService;
        this.consultantService = consultantService;
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

        if (!user.getUsername().equals(userDetails.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new IllegalArgumentException("Username already exists: " +  userDetails.getUsername());
            }
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setRole(userDetails.getRole());
        user.setName(userDetails.getName());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        // Verify first if user exists

        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        // If user is a manager - delete manager first
        managerRepository.findByUserId(userId).ifPresent(manager -> {
            managerService.deleteManager(manager.getId());
        });

        // If user is a consultant - delete consultant first
        consultantRepository.findByUserId(userId).ifPresent(consultant -> consultantService.deleteConsultant(consultant.getId()));

        // Finally delete the user
        userRepository.deleteById(userId);

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

    //Check if email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    //Get one user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with name " + username));
    }

}
