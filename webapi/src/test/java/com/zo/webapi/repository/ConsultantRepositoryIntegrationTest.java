package com.zo.webapi.repository;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConsultantRepositoryIntegrationTest {

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private User user;
    private Manager manager;

    @BeforeEach
    void setup() {
        consultantRepository.deleteAll();
        managerRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        user = new User();
        user.setUsername("consultant1");
        user.setPassword("password123");
        user.setName("John Doe");
        user.setRole(UserRole.CONSULTANT);
        user = userRepository.save(user);

        // Create test manager
        User managerUser = new User();
        managerUser.setUsername("manager1");
        managerUser.setPassword("password123");
        managerUser.setName("Manager One");
        managerUser.setRole(UserRole.MANAGER);
        managerUser = userRepository.save(managerUser);

        manager = new Manager();
        manager.setUser(managerUser);
        manager = managerRepository.save(manager);
    }

    @Test
    void testFindByUserId() {
        // Arrange
        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant.setManager(manager);
        consultant.setCity("Stockholm");
        consultantRepository.save(consultant);

        // Act
        Optional<Consultant> found = consultantRepository.findByUserId(user.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Stockholm", found.get().getCity());
    }

    @Test
    void testFindByManagerId() {
        // Arrange
        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant.setManager(manager);
        consultant.setCity("Göteborg");
        consultantRepository.save(consultant);

        // Act
        List<Consultant> consultants = consultantRepository.findByManagerId(manager.getId());

        // Assert
        assertEquals(1, consultants.size());
        assertEquals(manager.getId(), consultants.get(0).getManager().getId());
    }

    @Test
    void testFindByCityIgnoreCase() {
        // Arrange
        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant.setManager(manager);
        consultant.setCity("Stockholm");
        consultantRepository.save(consultant);

        // Act - Test case insensitivity
        List<Consultant> consultants = consultantRepository.findByCityIgnoreCase("STOCKHOLM");

        // Assert
        assertEquals(1, consultants.size());
        assertEquals("Stockholm", consultants.get(0).getCity());
    }

    @Test
    void testFindByManagerIdAndCity() {
        // Arrange
        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant.setManager(manager);
        consultant.setCity("Malmö");
        consultantRepository.save(consultant);

        // Act
        List<Consultant> consultants = consultantRepository.findByManagerIdAndCity(manager.getId(), "Malmö");

        // Assert
        assertEquals(1, consultants.size());
        assertEquals("Malmö", consultants.get(0).getCity());
    }
}
