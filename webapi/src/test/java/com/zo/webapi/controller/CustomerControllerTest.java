/*
// java
package com.zo.webapi.controller;

import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.service.CustomerService;
import org.junit.jupiter.api.Disabled;
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

    @Test
    void testShowAllCustomers_whenListNotEmpty() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(new Customer(), new Customer()));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
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
    void testShowCustomerById() throws Exception {
        // Arrange
        Customer customer = new Customer(1L, "Nercia Utbildning", "Malmö", new Manager(1L, new User(1L, "manager1", "pass", "MANAGER", "Malmö", "Manager One")));
        when(customerService.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        //Act & Assert
        mockMvc.perform(get("/api/customers/{id}", customer.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nercia Utbildning"))
                .andExpect(jsonPath("$.city").value("Malmö"))
                .andExpect(jsonPath("$.manager.id").value(1));
    }


    @Test
    void testShowCustomerById_whenIdIsNotInDatabase() throws Exception {
        // Arrange
        when(customerService.getCustomerById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/customers/{id}", 100L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCustomer() throws Exception {
        // Arrange
        Customer customer = new Customer(1L, "Eslövs Folkhögskola", "Eslöv", new Manager(1L, new User(1L, "manager1", "pass", "MANAGER", "Malmö", "Manager One")));
        when(customerService.createCustomer(eq("Eslövs Folkhögskola"), eq("Eslöv"))).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", customer.getName())
                        .param("city", customer.getCity()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Eslövs Folkhögskola"))
                .andExpect(jsonPath("$.city").value("Eslöv"))
                .andExpect(jsonPath("$.manager.id").value(1));

        verify(customerService).createCustomer("Eslövs Folkhögskola", "Eslöv");
    }

    @Test
    void testDeleteCustomer() throws Exception {
        //Arrange
        doNothing().when(customerService).deleteCustomer(eq(1L));

        mockMvc.perform(delete("/api/customers/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(eq(1L));

        // Arrange - non-existent customer
        doThrow(new RuntimeException()).when(customerService).deleteCustomer(eq(999L));

        mockMvc.perform(delete("/api/customers/{id}", 999L))
                .andExpect(status().isNotFound());
    }

}
*/
