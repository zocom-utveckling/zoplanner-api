package com.zo.webapi.repository;

import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerRepositoryIntegrationTest {
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ManagerRepository managerRepository;
    @Autowired private UserRepository userRepository;

    private Manager manager;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setName("test user");
        user.setRole(UserRole.MANAGER);
        userRepository.save(user);

        manager = new Manager(user);
        managerRepository.save(manager);

        Customer c1 = new Customer();
        c1.setName("Customer a");
        c1.setCity("City a");
        c1.setManager(manager);
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setName("Customer b");
        c2.setCity("City b");
        c2.setManager(manager);
        customerRepository.save(c2);

        Customer c3 = new Customer();
        c3.setName("Customer c");
        c3.setCity("City c");
        customerRepository.save(c3);


    }

    @Test
    void testFindManagerId() {
        List<Customer> customers = customerRepository.findByManagerId(manager.getId());
        assertThat(customers).hasSize(2);
        assertThat(customers).allMatch(c -> c.getManager() != null);


    }

    @Test
    void testFindByIdWithManager() {
        Customer c = customerRepository.findByManagerId(manager.getId()).get(0);
        Optional<Customer> found = customerRepository.findByIdWithManager(c.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getManager()).isNotNull();
        assertThat(found.get().getManager().getId()).isEqualTo(manager.getId());

    }

    @Test
    void testFindAllWithManager() {
        List<Customer> customers = customerRepository.findAllWithManagers();
        assertThat(customers).anyMatch(c ->  c.getManager() != null);
        assertThat(customers).anyMatch(c ->  c.getManager() == null);
    }

    @Test
    void testFindByIdNonExistent() {
        Optional<Customer> found = customerRepository.findByIdWithManager(999L);
        assertThat(found).isEmpty();
    }

}
