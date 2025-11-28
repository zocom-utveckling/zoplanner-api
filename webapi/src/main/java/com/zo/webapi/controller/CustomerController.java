package com.zo.webapi.controller;

import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.service.CustomerService;
import com.zo.webapi.service.ManagerService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService;
    private final ManagerService managerService;

    public CustomerController(CustomerService customerService, ManagerService managerService) {
        this.customerService = customerService;
        this.managerService  = managerService;
    }

    @GetMapping
    public List<Customer> showAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(
            @RequestParam @NotBlank(message = "name must not be blank") @Size(max = 255, message = "name must be at most 255 characters") String name,
            @RequestParam @NotBlank(message = "city must not be blank") @Size(max = 255, message = "city must be at most 255 characters") String city,
            @RequestParam(required = false) Long managerId) {

        Manager manager = (managerId != null) ? managerService.findManagerById(managerId) : null;
        Customer customer = customerService.createCustomer(name, city, manager);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDTO updateDto) {
        Customer updatedCustomer = customerService.updateCustomer(id, updateDto);
        return ResponseEntity.ok(updatedCustomer);
    }




}
