package com.zo.webapi.repository;

import com.zo.webapi.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*@DataJpaTest
public class CustomerRepositoryIntegrationTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindCustomer() {
        // Arrange
        Customer customer = new Customer(null, "Alice", "Paris", null);
        Customer saved = customerRepository.save(customer);

        // Act
        Optional<Customer> found = customerRepository.findById(saved.getId());

        //Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(saved.getName());
    }

    @Test
    void testFindAllCustomer() {
        // Arrange
        customerRepository.save(new Customer(null, "Alice", "Paris", null));
        customerRepository.save(new Customer(null, "Bob", "Paris", null));

        // Act
        List<Customer> customers = customerRepository.findAll();

        // Arrange
        assertThat(customers).hasSize(2);
    }

    @Test
    void testFindById_NotFound() {
        // Act
        Optional<Customer> found = customerRepository.findById(999L);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void testDeleteNonExistingCustomer() {
        // Act
        customerRepository.deleteById(999L);

        // Assert
        assertThat(customerRepository.findAll()).isEmpty();

    }
}
*/