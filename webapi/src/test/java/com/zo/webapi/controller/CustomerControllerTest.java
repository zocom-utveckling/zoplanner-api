package com.zo.webapi.controller;

import com.zo.webapi.model.Customer;
import com.zo.webapi.service.CustomerService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @Test
    void testShowCustomerById() throws Exception {
        // Arrange
        Customer customer = new Customer(1L, "Nercia Utbildning", "Malmö");
        when(customerService.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        //Act & Assert
        mockMvc.perform(get("/api/customers/{id}", customer.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nercia Utbildning"))
                .andExpect(jsonPath("$.city").value("Malmö"));

    }

    @Disabled("Temporarily disable this method")
    @Test
    void testShowCustomerById_whenIdIsNotInDatabase() throws Exception {
        // Arrange
        when(customerService.getCustomerById(100L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/customers/{id}", 100L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer with id 100 does not exist"));
    }

   @Test
   void testCreateCustomer() throws Exception {
       // Arrange
       Customer customer = new Customer(1L, "Eslövs Folkhögskola", "Eslöv");
       when(customerService.createCustomer(eq("Eslövs Folkhögskola"), eq("Eslöv"))).thenReturn(customer);

       // Act & Assert
       mockMvc.perform(post("/api/customers")
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                       .param("name", customer.getName())
                       .param("city", customer.getCity()))
               .andExpect(status().isCreated()) // или isOk() в зависимости от контроллера
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Eslövs Folkhögskola"))
               .andExpect(jsonPath("$.city").value("Eslöv"));

       verify(customerService).createCustomer("Eslövs Folkhögskola", "Eslöv");
   }



}
