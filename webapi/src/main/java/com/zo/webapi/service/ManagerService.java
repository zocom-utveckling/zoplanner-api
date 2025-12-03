package com.zo.webapi.service;
import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.CustomerDTO;
import com.zo.webapi.dto.ManagerResponseDTO;
import com.zo.webapi.model.Consultant;

import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.enums.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final ConsultantRepository consultantRepository;
    private final CustomerRepository customerRepository;

    public ManagerService(ManagerRepository managerRepository, UserRepository userRepository, ConsultantRepository consultantRepository, CustomerRepository customerRepository) {
        this.managerRepository = managerRepository;
        this.userRepository = userRepository;
        this.consultantRepository = consultantRepository;
        this.customerRepository = customerRepository;
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

    public Manager findManagerById(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));
        return manager;
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

    public List<ConsultantDTO> getConsultantsForManager(Long managerId) {

        // verify manager exists
        if(!managerRepository.existsById(managerId)) {
            throw new RuntimeException("Manager not found with id: " + managerId);
        }
        return consultantRepository.findByManagerId(managerId)
                .stream()
                .map(consultant -> new ConsultantDTO(
                        consultant.getId(),
                        consultant.getUser().getName(),
                        consultant.getCity()
                ))
                .toList();
    }

    public List<CustomerDTO> getCustomersForManager(Long managerId) {

        //verify manager exists
        if(!managerRepository.existsById(managerId)) {
            throw new RuntimeException("Manager not found with id: " + managerId);
        }
        return customerRepository.findByManagerId(managerId)
                .stream()
                .map(customer -> new CustomerDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getCity()
                ))
                .toList();
    }

    //Assign consultant to manager
    @Transactional
    public void assignConsultantToManager(Long managerId, Long consultantId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));

        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found with id: " + consultantId));

        consultant.setManager(manager);
        consultantRepository.save(consultant);
    }

    @Transactional
    public void removeConsultantFromManager(Long managerId, Long consultantId) {
        // we don't really need manager here, but can verify it exists
        if (!managerRepository.existsById(managerId)) {
            throw new RuntimeException("Manager not found with id: " + managerId);
        }

        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found with id: " + consultantId));

        consultant.setManager(null);
        consultantRepository.save(consultant);
    }


    @Transactional
    public void assignCustomerToManager(Long managerId, Long customerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        customer.setManager(manager);
        customerRepository.save(customer);
    }

    @Transactional
    public void removeCustomerFromManager(Long managerId, Long customerId) {
        if (!managerRepository.existsById(managerId)) {
            throw new RuntimeException("Manager not found with id: " + managerId);
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        customer.setManager(null);
        customerRepository.save(customer);
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


