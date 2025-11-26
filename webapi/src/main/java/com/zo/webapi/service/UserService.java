package com.zo.webapi.service;

import com.zo.webapi.dto.UserPatchDTO;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zo.webapi.enums.UserRole;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final ManagerRepository managerRepository;
    private final ConsultantRepository consultantRepository;

    //Constructor
    public UserService(UserRepository userRepository, ManagerRepository managerRepository,
                        ConsultantRepository consultantRepository) {

        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.consultantRepository = consultantRepository;
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
    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        handleRoleCreation(savedUser);

        return savedUser;
    }

    private void handleRoleCreation(User user) {
        UserRole role = user.getRole();

        if (role == UserRole.MANAGER || role == UserRole.BOTH) {
            Manager manager = new Manager(user);
            managerRepository.save(manager);
        }

        if (role == UserRole.CONSULTANT ||  role == UserRole.BOTH) {
            Consultant consultant = new Consultant();
            consultant.setUser(user);
            consultant.setCity("Unknown");
            consultantRepository.save(consultant);
        }
    }

    //Updates all columns in a user
    @Transactional
    public User updateUser(Long id, User userDetails) {
       User user = getUserById(id);

       UserRole oldRole = user.getRole();

       user.setUsername(userDetails.getUsername());
       user.setPassword(userDetails.getPassword());
       user.setName(userDetails.getName());
       user.setRole(userDetails.getRole());

       User updatedUser = userRepository.save(user);

       handleRoleChanges(updatedUser, oldRole, updatedUser.getRole());
       return updatedUser;

    }

    // Updates only the fields provided
    public User patchUser(Long id, UserPatchDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        UserRole oldRole = user.getRole();

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getRole() != null) user.setRole(dto.getRole());

        User updatedUser = userRepository.save(user);

        if (dto.getRole() != null) {
            handleRoleChanges(updatedUser, oldRole, dto.getRole());
        }

        return updatedUser;
    }

    //Deletes a user
    @Transactional
    public void deleteUser(Long id) {
       if (!userRepository.existsById(id)) {
           throw new IllegalArgumentException("User not found with id " + id);
       }

       managerRepository.findByUserId(id).ifPresent(managerRepository::delete);
       consultantRepository.findByUserId(id).ifPresent(consultantRepository::delete);

        userRepository.deleteById(id);

    }

    private void handleRoleChanges(User user, UserRole oldRole, UserRole newRole) {
        boolean wasManager = oldRole == UserRole.MANAGER || oldRole == UserRole.BOTH;
        boolean wasConsultant = oldRole == UserRole.CONSULTANT ||  oldRole == UserRole.BOTH;

        boolean isManager = newRole == UserRole.MANAGER || newRole == UserRole.BOTH;
        boolean isConsultant = newRole == UserRole.CONSULTANT ||  newRole == UserRole.BOTH;

        // Remove manager role
        if (wasManager && !isManager) {
            managerRepository.findByUserId(user.getId()).ifPresent(managerRepository::delete);

        }

        // add manager role
        if (!wasManager && isManager) {
            managerRepository.save(new Manager(user));
        }

        // Remove consultant role
        if (wasConsultant && !isConsultant) {
            consultantRepository.findByUserId(user.getId()).ifPresent(consultantRepository::delete);
        }

        // Add consultant role
        if (!wasConsultant && isConsultant) {
            Consultant consultant = new Consultant();
            consultant.setUser(user);
            consultant.setCity("Unknown");
            consultantRepository.save(consultant);
        }
    }



}
