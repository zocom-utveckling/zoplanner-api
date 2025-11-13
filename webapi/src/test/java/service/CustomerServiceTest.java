package service;

import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.service.CustomerService;
import jakarta.validation.constraints.AssertTrue;
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
        when(customerRepository.findAll()).thenReturn(List.of(new Customer()));
        assertEquals(1, customerService.getAllCustomers().size());
    }

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer created = customerService.createCustomer("Test", "Paris");
        assertNotNull(created);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> customerService.deleteCustomer(1L));
    }

    @Test
    void testGetCustomerById_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");
        customer.setCity("Paris");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> foundOpt = customerService.getCustomerById(1L);

        assertTrue(foundOpt.isPresent());
        Customer found = foundOpt.get();
        assertEquals("Alice", found.getName());
        assertEquals("Paris", found.getCity());
        verify(customerRepository).findById(1L);
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Customer> result = customerService.getCustomerById(99L);
        assertTrue(result.isEmpty());
        verify(customerRepository).findById(99L);
    }
}
