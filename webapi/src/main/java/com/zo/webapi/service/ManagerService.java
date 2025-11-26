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
import java.util.stream.Collectors;


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

        // Validate role
        if (user.getRole() != UserRole.MANAGER && user.getRole() != UserRole.BOTH) {
            throw new RuntimeException("User must have MANAGER or BOTH role");
        }

        //Prevent duplicate manager
        if (managerRepository.existsByUserId(userId)) {
            throw new RuntimeException("User is already a manager");
        }

        // Create and save manager
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

    public ManagerResponseDTO getManagerById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + id));

        User user = manager.getUser();

        return new ManagerResponseDTO(
                manager.getId(),
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    public List<ManagerResponseDTO> getAllManagers() {
        return managerRepository.findAll()
                .stream()
                .map(manager -> {
                    User user = manager.getUser();
                    return new ManagerResponseDTO(
                            manager.getId(),
                            user.getId(),
                            user.getUsername(),
                            user.getRole().name()
                    );
                })
                .collect(Collectors.toList());
    }

    public ManagerResponseDTO getManagerByUserId(Long userId) {
        Manager manager = managerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Manager not found with user id: " + userId));

        User user = manager.getUser();

        return new ManagerResponseDTO(
                manager.getId(),
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    public List<?> getConsultantsForManager(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));
        return manager.getConsultants();
    }

    public List<?> getCustomersForManager(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));
        return manager.getCustomers();
    }


    //Update : change which user is associated with this manager
    @Transactional
    public ManagerResponseDTO updateManagerUser(Long managerId, Long newUserId) {

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));

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
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + id));

        // SET NULL for consultants
        if (manager.getConsultants() != null) {
            manager.getConsultants().forEach(consultant -> consultant.setManager(null));
        }

        // SET NULL for customers
        if (manager.getCustomers() != null) {
            manager.getCustomers().forEach(customer -> customer.setManager(null));
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


