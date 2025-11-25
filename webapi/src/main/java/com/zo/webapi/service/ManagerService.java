package com.zo.webapi.service;
import com.zo.webapi.dto.ManagerResponseDTO;
import com.zo.webapi.model.Consultant;

import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import enums.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;

    public ManagerService(ManagerRepository managerRepository, UserRepository userRepository) {
        this.managerRepository = managerRepository;
        this.userRepository = userRepository;
    }

    //CREATE manager for existing user
    @Transactional
    public ManagerResponseDTO createManager(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.BOTH) {
            throw new RuntimeException("User must have MANAGER or BOTH role");
        }

        //Prevent duplicate manager
        if (managerRepository.existsByUserId(userId)) {
            throw new RuntimeException("User is already a manager");
        }

        Manager manager = new Manager(user);
        managerRepository.save(manager);

        return new ManagerResponseDTO(
                manager.getId(),
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    //READ
    public Manager getManagerById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + id));
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Manager getManagerByUserId(Long userId) {
        return managerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Manager not found with user id: " + userId));
    }

    public List<?> getConsultantsForManager(Long managerId) {
        Manager manager = getManagerById(managerId);
        return manager.getConsultants();
    }

    public List<?> getCustomersForManager(Long managerId) {
        Manager manager = getManagerById(managerId);
        return manager.getCustomers();
    }


    //Update : change which user is associated with this manager
    @Transactional
    public ManagerResponseDTO updateManagerUser(Long managerId, Long newUserId) {

        Manager manager = getManagerById(managerId);

        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + newUserId));

        // Validate new role
        if (newUser.getRole() != UserRole.MANAGER && newUser.getRole() != UserRole.BOTH) {
            throw new RuntimeException("User must have MANAGER or BOTH role");
        }

        // Ensure the new user is not already a manager
        if (managerRepository.existsByUserId(newUserId)) {
            throw new RuntimeException("This user is already a manager");
        }

        manager.setUser(newUser);
        managerRepository.save(manager);

        return new ManagerResponseDTO(
                manager.getId(),
                newUser.getId(),
                newUser.getUsername(),
                newUser.getRole().name()
        );
    }

    //DELETE : delete manager and set NULL for associated consultants and customers
    @Transactional
    public void deleteManager(Long id) {
        Manager manager = getManagerById(id);

        // SET NULL for consultants
        if (manager.getConsultants() != null) {
            manager.getConsultants().forEach(c -> c.setManager(null));
        }

        // SET NULL for customers
        if (manager.getCustomers() != null) {
            manager.getCustomers().forEach(c -> c.setManager(null));
        }

        managerRepository.delete(manager);
    }

    // DELETE BY USER ID (for UserService integration)
    @Transactional
    public void deleteByUserId(Long userId) {
        Manager manager = managerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Manager not found with user id: " + userId));
        deleteManager(manager.getId());
    }
}


