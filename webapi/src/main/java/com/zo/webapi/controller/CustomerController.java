package com.zo.webapi.controller;

import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> showAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Customer with id " + id + " does not exist"));
        }
        return ResponseEntity.ok(customer);
    }
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestParam String name,
                                            @RequestParam String city) {
        Customer customer = customerService.createCustomer(name, city);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            Throwable root = e.getRootCause();
            String systemMessage = (root != null && root.getMessage() != null) ? root.getMessage() : e.getMessage();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", systemMessage));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
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
