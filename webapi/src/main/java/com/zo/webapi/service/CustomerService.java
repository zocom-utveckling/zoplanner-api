package com.zo.webapi.service;

import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.dto.ManagerResponseDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(String name, String city, Manager manager) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setCity(city);
        customer.setManager(manager);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    public Customer updateCustomer(Long id, CustomerUpdateDTO updateDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (updateDto.getName() != null) {
            customer.setName(updateDto.getName());
        }
        if (updateDto.getCity() != null) {
            customer.setCity(updateDto.getCity());
        }

        return customerRepository.save(customer);
    }


}
