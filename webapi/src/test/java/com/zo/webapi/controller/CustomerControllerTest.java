package com.zo.webapi.controller;

import com.zo.webapi.model.Customer;
import com.zo.webapi.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void testShowAllCustomers_whenListNotEmpty() throws Exception {
        // Arrange
        List<Customer> customers = List.of(
                new Customer(1L, "Högskolan i Halmstad", "Halmstad"),
                new Customer(2L, "Grit Academy", "Malmö")
        );
        when(customerService.getAllCustomers()).thenReturn(customers);

        //Act & Assert
        mockMvc.perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Högskolan i Halmstad"))
                .andExpect(jsonPath("$[1].name").value("Grit Academy"))
                .andExpect(jsonPath("$[0].city").value("Halmstad"))
                .andExpect(jsonPath("$[1].city").value("Malmö"));

    }
    @Test
    void testShowAllCustomers_whenListIsEmpty() throws Exception {
        // Arrange
        List<Customer> customers = List.of();
        when(customerService.getAllCustomers()).thenReturn(customers);

        //Act & Assert
        mockMvc.perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().json("[]"));

    }

}
