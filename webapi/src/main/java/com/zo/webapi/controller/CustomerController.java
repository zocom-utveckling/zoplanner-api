package com.zo.webapi.controller;

import com.zo.webapi.dto.CustomerCreateDTO;
import com.zo.webapi.dto.CustomerResponseDTO;
import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.service.CustomerService;
import com.zo.webapi.service.ManagerService;
import jakarta.validation.Valid;
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
    public List<CustomerResponseDTO> showAllCustomers() {
        return customerService.getAllCustomersDto();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id) {
        CustomerResponseDTO dto = customerService.getCustomerByIdDto(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO dto) {
        CustomerResponseDTO created = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDTO customerUpdateDTO) {

        CustomerResponseDTO updated = customerService.updateCustomer(id, customerUpdateDTO);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }




}
