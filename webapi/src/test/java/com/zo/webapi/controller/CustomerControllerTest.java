package com.zo.webapi.controller;

import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.service.CustomerService;
import com.zo.webapi.service.ManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ManagerService managerService;

    @Test
    void testShowAllCustomers_whenListNotEmpty() throws Exception {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane");

        when(customerService.getAllCustomers()).thenReturn(List.of(customer1, customer2));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$.length()").value(2));

        verify(customerService).getAllCustomers();

    }


    @Test
    void testShowAllCustomers_whenListIsEmpty() throws Exception {
        // Arrange
        List<Customer> customers = List.of();
        when(customerService.getAllCustomers()).thenReturn(customers);

        //Act & Assert
        mockMvc.perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetCustomerById_Success() throws Exception {
        Manager manager = new Manager();
        manager.setId(1L);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setCity("Berlin");
        customer.setManager(manager);

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.city").value("Berlin"));
        verify(customerService).getCustomerById(1L);
    }


    @Test
    void testGetCustomerById_NotFound() throws Exception {
             when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

             mockMvc.perform(get("/api/customers/99"))
                     .andExpect(status().isNotFound());

             verify(customerService).getCustomerById(99L);
    }

    @Test
    void testCreateCustomer_Success() throws Exception {
        Manager manager = new Manager();
        manager.setId(1L);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setCity("Berlin");
        customer.setManager(manager);

        when(managerService.findManagerById(1L)).thenReturn(manager);
        when(customerService.createCustomer(eq("John"), eq("Berlin"), any())).thenReturn(customer);

        // Send as form data to match @RequestParam
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "John")
                        .param("city", "Berlin")
                        .param("managerId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.city").value("Berlin")); // skip manager

        verify(customerService).createCustomer("John", "Berlin", manager);

    }

    @Test
    void testDeleteCustomer_Success() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
        verify(customerService).deleteCustomer(1L);
    }

    @Test
    void testDeleteCustomer_NotFound() throws Exception {
        doThrow(new RuntimeException()).when(customerService).deleteCustomer(99L);

        mockMvc.perform(delete("/api/customers/99"))
                .andExpect(status().isNotFound());
        verify(customerService).deleteCustomer(99L);
    }

    // ===== PATCH /api/customers/{id} =====
    @Test
    void testUpdateCustomer_Success() throws Exception {
        String json = "{\"name\":\"John\",\"city\":\"Berlin\"}";

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Bob");
        updatedCustomer.setCity("London");

        when(customerService.updateCustomer(eq(1L), any())).thenReturn(updatedCustomer);

        mockMvc.perform(patch("/api/customers/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.city").value("London"));

        verify(customerService).updateCustomer(eq(1L), any());

    }
}

