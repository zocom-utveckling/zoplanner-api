package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.*;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.ConsultantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ConsultantServiceIntegrationTest {

   @Autowired
    private ConsultantService consultantService;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private User consultantUser;
    private Manager manager;



    @BeforeEach
    public void setup() {
        consultantRepository.deleteAll();
        userRepository.deleteAll();
        managerRepository.deleteAll();

        // create test user
        consultantUser = new User();
        consultantUser.setUsername("consultant1");
        consultantUser.setPassword("password123");
        consultantUser.setName("Test Consultant");
        consultantUser.setRole(UserRole.CONSULTANT);
        consultantUser = userRepository.save(consultantUser);

        // create test manager user
        User managerUser = new User();
        managerUser.setUsername("manager1");
        managerUser.setPassword("password123");
        managerUser.setName("Test Manager");
        managerUser.setRole(UserRole.MANAGER);
        managerUser = userRepository.save(managerUser);

        manager = new Manager();
        manager.setUser(managerUser);
        manager = managerRepository.save(manager);
    }

    @Test
    void testCreateConsultant_Success() {
        // Arrange
        ConsultantDTO dto = new ConsultantDTO(null, "Stockholm", consultantUser.getId(), manager.getId());

        // Act
        ConsultantDTO created = consultantService.createConsultant(dto);

        // Assert
        assertNotNull(created.getId());
        assertEquals("Stockholm", created.getCity());
        assertEquals(consultantUser.getId(), created.getUserId());
        assertEquals(manager.getId(), created.getManagerId());
    }

    @Test

    void testGetAllConsultants() {
        // Create consultants
        ConsultantDTO dto1 = new ConsultantDTO(null, "Stockholm", consultantUser.getId(), manager.getId());
        consultantService.createConsultant(dto1);

        // Get all consultants
        List<ConsultantDTO> consultants = consultantService.getAllConsultants();

        // Assert
        assertNotNull(consultants);
        assertEquals(1, consultants.size());
    }

    @Test
    void testGetConsultantById_Success() {
        // Create consultant
        ConsultantDTO dto = new ConsultantDTO(null, "Malmö", consultantUser.getId(), manager.getId());
        ConsultantDTO created = consultantService.createConsultant(dto);

        // Get consultant by ID
        ConsultantDTO found = consultantService.getConsultantById(created.getId());

        // Assert
        assertEquals(created.getId(), found.getId());
        assertEquals("Malmö", found.getCity());
    }

    @Test
    void testGetConsultantById_NotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> consultantService.getConsultantById(999L));
    }

    @Test
    void testGetConsultantsByManagerId() {
        Consultant consultant = new Consultant();
        consultant.setCity("Malmö");
        consultant.setUser(consultantUser);
        consultant.setManager(manager);
        consultantRepository.save(consultant);

        List<ConsultantDTO> list = consultantService.getConsultantsByManagerId(manager.getId());

        assertEquals(1, list.size());
        assertEquals("Malmö", list.get(0).getCity());
    }



    @Test
    void testUpdateConsultant_Success() {
        // Create consultant
        ConsultantDTO dto = new ConsultantDTO(null, "Uppsala", consultantUser.getId(), manager.getId());
        ConsultantDTO created = consultantService.createConsultant(dto);

        // Update data
        ConsultantDTO updateDto = new ConsultantDTO(null, "Västerås", consultantUser.getId(), null);
        ConsultantDTO updated = consultantService.updateConsultant(created.getId(), updateDto);

        // Assert
        assertEquals("Västerås", updated.getCity());
        assertNull(updated.getManagerId());
    }

    @Test
    void testUpdateConsultant_NotFound() {
        ConsultantDTO updateDto = new ConsultantDTO(999L, "Västerås", consultantUser.getId(), manager.getId());
        assertThrows(ResourceNotFoundException.class,
                () -> consultantService.updateConsultant(999L, updateDto));
    }

    @Test
    void testDeleteConsultant_Success() {
        // Create consultant
        ConsultantDTO dto = new ConsultantDTO(null, "Linköping", consultantUser.getId(), manager.getId());
        ConsultantDTO created = consultantService.createConsultant(dto);

        // Delete consultant
        consultantService.deleteConsultant(created.getId());

        // Verify deletion
        assertThrows(ResourceNotFoundException.class,
                () -> consultantService.getConsultantById(created.getId()));
    }

    @Test
    void testDeleteConsultant_NotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> consultantService.deleteConsultant(999L));
    }
}
