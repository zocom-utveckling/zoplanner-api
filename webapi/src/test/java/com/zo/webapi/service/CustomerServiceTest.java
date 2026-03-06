package com.zo.webapi.service;

import com.zo.webapi.dto.CustomerCreateDTO;
import com.zo.webapi.dto.CustomerResponseDTO;
import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void testGetCustomerById_Success() {

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());

        verify(customerRepository).findById(1L);
    }

    @Test
    void testGetCustomerById_NotFound() {

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getCustomerById(99L);

        assertTrue(result.isEmpty());

        verify(customerRepository).findById(99L);
    }

    @Test
    void testCreateCustomer() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setCity("Paris");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("John Doe");
        dto.setCity("Paris");
        dto.setManagerId(null);

        CustomerResponseDTO created = customerService.createCustomer(dto);

        assertNotNull(created);
        assertEquals("John Doe", created.getName());
        assertEquals("Paris", created.getCity());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_Success() {

        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.deleteCustomer(1L);

        verify(customerRepository).existsById(1L);
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_NotFound() {

        when(customerRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> customerService.deleteCustomer(99L));

        assertEquals("Customer not found with id: 99", exception.getMessage());

        verify(customerRepository).existsById(99L);
        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateCustomer_Success() {

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setName("John Doe");
        updateDTO.setCity("Paris");

        CustomerResponseDTO updated = customerService.updateCustomer(1L, updateDTO);

        assertEquals("John Doe", updated.getName());
        assertEquals("Paris", updated.getCity());

        verify(customerRepository).save(customer);
    }

    @Test
    void testUpdateCustomer_NotFound() {

        long id = 99L;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setName("John Doe");

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> customerService.updateCustomer(id, updateDTO));

        assertEquals("Customer not found with id: " + id, exception.getMessage());

        verify(customerRepository, never()).save(any());
    }
}