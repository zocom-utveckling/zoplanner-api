package com.zo.webapi.controller;

import com.zo.webapi.dto.*;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.service.CustomerService;
import com.zo.webapi.service.ManagerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        ManagerResponseToCustomerDTO mgr = null;
        CustomerResponseDTO customer1 = new CustomerResponseDTO(1L, "John", "City1", mgr);
        CustomerResponseDTO customer2 = new CustomerResponseDTO(2L, "Jane", "City2", mgr);

        when(customerService.getAllCustomersDto()).thenReturn(List.of(customer1, customer2));

        mockMvc.perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"));

        verify(customerService).getAllCustomersDto();
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
        ManagerResponseToCustomerDTO mgrDto = new ManagerResponseToCustomerDTO(1L, "mgrUser", "MANAGER");
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "John", "Berlin", mgrDto);

        when(customerService.getCustomerByIdDto(1L)).thenReturn(response);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.city").value("Berlin"))
                .andExpect(jsonPath("$.manager.id").value(1));

        verify(customerService).getCustomerByIdDto(1L);
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerByIdDto(99L)).thenReturn(null);

        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());

        verify(customerService).getCustomerByIdDto(99L);
    }

    @Test
    void testCreateCustomer_Success() throws Exception {
        Manager manager = new Manager();
        manager.setId(1L);

        ManagerResponseToCustomerDTO mgrDto = new ManagerResponseToCustomerDTO(1L, "mgrUser", "MANAGER");
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "John", "Berlin", mgrDto);

        when(managerService.getManagerById(1L)).thenReturn(new ManagerResponseDTO(1L, 1L, "mgrUser", "MANAGER"));
        when(customerService.createCustomer(any(CustomerCreateDTO.class))).thenReturn(response);

        String json = "{\"name\":\"John\",\"city\":\"Berlin\",\"managerId\":1}";

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.city").value("Berlin"));

        ArgumentCaptor<CustomerCreateDTO> captor = ArgumentCaptor.forClass(CustomerCreateDTO.class);
        verify(customerService).createCustomer(captor.capture());
        CustomerCreateDTO dto = captor.getValue();
        assertEquals("John", dto.getName());
        assertEquals("Berlin", dto.getCity());
        assertEquals(1L, dto.getManagerId());
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

        CustomerResponseDTO response = new CustomerResponseDTO(1L, "Bob", "London", null);

        when(customerService.updateCustomer(eq(1L), any(CustomerUpdateDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.city").value("London"));

        ArgumentCaptor<CustomerUpdateDTO> captor = ArgumentCaptor.forClass(CustomerUpdateDTO.class);
        verify(customerService).updateCustomer(eq(1L), captor.capture());
        CustomerUpdateDTO sent = captor.getValue();
        assertEquals("John", sent.getName());
        assertEquals("Berlin", sent.getCity());
    }
}

