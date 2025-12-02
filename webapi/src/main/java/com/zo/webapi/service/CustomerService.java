package com.zo.webapi.service;

import com.zo.webapi.dto.*;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.repository.ManagerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;

    public CustomerService(CustomerRepository customerRepository, ManagerRepository managerRepository) {
        this.customerRepository = customerRepository;
        this.managerRepository = managerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomersDto() {
        return customerRepository.findAllWithManagers().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerByIdDto(Long id) {
        return customerRepository.findByIdWithManager(id)
                .map(this::toDto)
                .orElse(null);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    public CustomerResponseDTO createCustomer(CustomerCreateDTO dto) {
        // Find manager by ID
        Manager manager = null;
        if (dto.getManagerId() != null) {
            manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with id: " + + dto.getManagerId()));
        }

        // Create and save customer
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setCity(dto.getCity());
        customer.setManager(manager);

        Customer saved = customerRepository.save(customer);
        return toDto(saved);
    }

    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElse(null);
        if (customer == null) {
            return null;
        }

        // Update name, if not null
        if (updateDTO.getName() != null) {
            customer.setName(updateDTO.getName());
        }

        // Update city, if not null
        if (updateDTO.getCity() != null) {
            customer.setCity(updateDTO.getCity());
        }

        // Update manager, if not null
        if (updateDTO.getManagerId() != null) {
            Manager manager = managerRepository.findById(updateDTO.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with id: " + updateDTO.getManagerId()));
            customer.setManager(manager);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return toDto(savedCustomer);
    }

    private CustomerResponseDTO toDto(Customer c) {
        Manager m = c.getManager();
        return new CustomerResponseDTO(
                c.getId(),
                c.getName(),
                c.getCity(),
                m == null ? null : new ManagerResponseToCustomerDTO(
                        m.getId(),
                        m.getUser().getUsername(),
                        m.getUser().getRole().name()
                )
        );
    }


}
