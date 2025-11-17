package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class CustomerServiceIntegrationTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
        testCustomer = customerService.createCustomer("John", "London");

    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getName());
    }

    @Test
    void testUpdateCustomers() {
        CustomerUpdateDTO customerUpdateDTO = new CustomerUpdateDTO();
        customerUpdateDTO.setName("John Updated");
        customerUpdateDTO.setCity("Stockholm");

        Customer updatedCustomer = customerService.updateCustomer(testCustomer.getId(), customerUpdateDTO);

        assertEquals("John Updated", updatedCustomer.getName());
        assertEquals("Stockholm", updatedCustomer.getCity());
    }

    @Test
    void testDeleteCustomer_NotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.deleteCustomer(999L);
        });
        assertTrue(exception.getMessage().contains("Customer not found with id"));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        CustomerUpdateDTO customerUpdateDTO = new CustomerUpdateDTO();
        customerUpdateDTO.setName("Non Existent");
        customerUpdateDTO.setCity("None");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(999L, customerUpdateDTO);

        });
        assertTrue(exception.getMessage().contains("Customer not found"));
    }
}
