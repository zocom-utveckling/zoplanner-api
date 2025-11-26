package com.zo.webapi.service;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import enums.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ConsultantRepository consultantRepository;
    private final ManagerRepository managerRepository;

    //Constructor
    public UserService(UserRepository userRepository, ConsultantRepository consultantRepository, ManagerRepository managerRepository) {
        this.userRepository = userRepository;
        this.consultantRepository = consultantRepository;
        this.managerRepository = managerRepository;
    }

    //Gets all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Creates a user
    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);

        applyRoleTables(savedUser);
        return savedUser;
    }

    //Updates all columns in a user
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        boolean roleChanged = user.getRole() != userDetails.getRole();

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setName(userDetails.getName());
        user.setRole(userDetails.getRole());

        User updatedUser = userRepository.save(user);

        if (roleChanged) {
            applyRoleTables(updatedUser);
        }

        return updatedUser;

    }

    //Deletes a user
    public void deleteUser(Long id) {
       if (!userRepository.existsById(id)) {
           throw new IllegalArgumentException("User not found with id " + id);
       }

       consultantRepository.findByUserId(id).ifPresent(consultantRepository::delete);
       managerRepository.findByUserId(id).ifPresent(managerRepository::delete);

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

    private void applyRoleTables(User user) {
        Long userId = user.getId();

        consultantRepository.findByUserId(userId).ifPresent(consultantRepository::delete);
        managerRepository.findByUserId(userId).ifPresent(managerRepository::delete);

        if (user.getRole() == UserRole.MANAGER) {
            Manager manager = new Manager(user);
            managerRepository.save(manager);
        }

        if (user.getRole() == UserRole.CONSULTANT) {
            Consultant consultant = new Consultant();
            consultant.setUser(user);
            consultant.setCity("Unknown");
            consultantRepository.save(consultant);

        }

        if (user.getRole() == UserRole.BOTH) {
            Manager manager = new Manager(user);
            managerRepository.save(manager);

            Consultant consultant = new Consultant();
            consultant.setUser(user);
            consultant.setCity("Unknown");
            consultantRepository.save(consultant);
        }
    }

}
