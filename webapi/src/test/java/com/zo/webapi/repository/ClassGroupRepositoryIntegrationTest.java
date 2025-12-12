package com.zo.webapi.repository;

import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ClassGroupRepositoryIntegrationTest {
    @Autowired
    private ClassGroupRepository classGroupRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private UserRepository userRepository;

    private Customer customer;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("manager1");
        user.setPassword("password");
        user.setName("Manager one");
        user.setRole(UserRole.MANAGER);
        userRepository.save(user);

        Manager manager = new Manager(user);
        managerRepository.save(manager);

        customer = new Customer();
        customer.setName("Customer a");
        customer.setCity("City a");
        customer.setManager(manager);
        customerRepository.save(customer);

        ClassGroup group1 = new ClassGroup("Class 1", customer);
        ClassGroup group2 = new ClassGroup("Class 2", customer);

        classGroupRepository.save(group1);
        classGroupRepository.save(group2);

    }

    @Test
    void testFindByCustomerId() {

        List<ClassGroup> groups = classGroupRepository.findByCustomerId(customer.getId());
        assertThat(groups).hasSize(2);
        assertThat(groups).allMatch(c -> c.getCustomer().getId().equals(customer.getId()));

    }

    @Test
    void testFindByName() {
        Optional<ClassGroup> found = classGroupRepository.findByName("Class 1");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Class 1");
    }

    @Test
    void testExistsByNameAndCustomerId() {
        boolean exists = classGroupRepository.existsByNameAndCustomerId("Class 1", customer.getId());
        assertThat(exists).isTrue();

        boolean notExists = classGroupRepository.existsByNameAndCustomerId("Nonexistent", customer.getId());
        assertThat(notExists).isFalse();

    }

}

