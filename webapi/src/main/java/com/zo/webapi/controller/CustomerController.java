package com.zo.webapi.controller;

import com.zo.webapi.model.Customers;
import com.zo.webapi.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customers> showAllCustomers() {
        return customerService.getAllCustomers();
    }
}
