package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.CustomerCreateDTO;
import com.zo.webapi.dto.CustomerCreateDTO;
import com.zo.webapi.dto.CustomerResponseDTO;
import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;

    @BeforeEach
    void setUp() {

        customerRepository.deleteAll();


        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("John");
        dto.setCity("London");
        dto.setManagerId(null); // om manager är nullable

        CustomerResponseDTO customer1 = customerService.createCustomer(dto);

    }

    @Test
    void testGetAllCustomers_Success() {

        List<Customer> customers = customerService.getAllCustomers();

        assertThat(customers).isNotEmpty();
        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getName()).isEqualTo("John");
    }

    @Test
    void testCreateCustomer_Success() {
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("Alice");
        dto.setCity("Paris");
        dto.setManagerId(null);

        CustomerResponseDTO created = customerService.createCustomer(dto);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Alice");
        assertThat(created.getCity()).isEqualTo("Paris");
        assertThat(customerRepository.findById(created.getId())).isPresent();
    }

    @Test
    void testUpdateCustomer_Success() {

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setName("John Updated");
        updateDTO.setCity("Stockholm");

        CustomerResponseDTO updated = customerService.updateCustomer(customer1.getId(), updateDTO);

        assertThat(updated.getName()).isEqualTo("John Updated");
        assertThat(updated.getCity()).isEqualTo("Stockholm");
    }

    @Test
    void testUpdateCustomer_NotFound() {

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setName("Not found");
        updateDTO.setCity("None");

        RuntimeException exc = assertThrows(
                RuntimeException.class,
                () -> customerService.updateCustomer(9999L, updateDTO)
        );

        assertThat(exc.getMessage()).contains("Customer not found");
    }

    @Test
    void testDeleteCustomer_Success() {

        customerService.deleteCustomer(customer1.getId());

        assertThat(customerRepository.findById(customer1.getId())).isEmpty();
    }

    @Test
    void testDeleteCustomer_NotFound() {

        RuntimeException exc = assertThrows(
                RuntimeException.class,
                () -> customerService.deleteCustomer(9999L)
        );

        assertThat(exc.getMessage()).contains("Customer not found");
    }
}