
package service;


import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.enums.ConsultantStatusType;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.*;
import com.zo.webapi.service.ConsultantStatusService;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ConsultantStatusServiceIntegrationTest {

    @Autowired
    private ConsultantStatusService consultantStatusService;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private ConsultantStatusRepository consultantStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private Consultant consultant;

    @BeforeEach
    void setup() {
        consultantStatusRepository.deleteAll();
        consultantRepository.deleteAll();
        managerRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        User consultantUser = new User();
        consultantUser.setUsername("consultant1");
        consultantUser.setPassword("password123");
        consultantUser.setName("Test Consultant");
        consultantUser.setRole(UserRole.CONSULTANT);
        consultantUser = userRepository.save(consultantUser);


        consultant = new Consultant();
        consultant.setCity("Stockholm");
        consultant.setUser(consultantUser);
        consultant = consultantRepository.save(consultant);
    }


    @Test
    void testCreateStatus() {
        ConsultantStatusDTO dto = new ConsultantStatusDTO();
        dto.setConsultantId(consultant.getId());
        dto.setStatus(ConsultantStatusType.AVAILABLE);
        dto.setDateStart(LocalDate.of(2024, 1, 1));
        dto.setDateEnd(LocalDate.of(2024, 1, 31));
        dto.setComment("Test comment");

        // Act
        ConsultantStatus created = consultantStatusService.createStatus(dto);

        // Assert
        assertNotNull(created.getId());
        assertEquals(ConsultantStatusType.AVAILABLE, created.getStatus());
        assertEquals(LocalDate.of(2024, 1, 1), created.getDateStart());
    }


    @Test
    void testGetStatusesByConsultant_NotFound() {
        Long nonExistentConsultantId = 999L;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            consultantStatusService.getStatusesByConsultant(nonExistentConsultantId);
        });

        assertEquals(404, exception.getStatusCode().value());
        assertTrue(exception.getReason().contains("No statuses found"));
    }

    @Test
    void testCreateStatus_AllEnumTypes() {
        for (ConsultantStatusType statusType : ConsultantStatusType.values()) {
            ConsultantStatusDTO dto = new ConsultantStatusDTO();
            dto.setConsultantId(consultant.getId());
            dto.setStatus(statusType);
            dto.setDateStart(LocalDate.of(2024, 2, 1));
            dto.setDateEnd(LocalDate.of(2024, 2, 28));
            dto.setComment("Testing status type: " + statusType);

            ConsultantStatus created = consultantStatusService.createStatus(dto);

            assertNotNull(created.getId());
            assertEquals(statusType, created.getStatus());
        }
    }

    @Test
    void testGetStatusById_Success() {
        ConsultantStatusDTO dto = new ConsultantStatusDTO();
        dto.setConsultantId(consultant.getId());
        dto.setStatus(ConsultantStatusType.AVAILABLE);
        dto.setDateStart(LocalDate.of(2024, 1, 1));
        dto.setDateEnd(LocalDate.of(2024, 1, 31));
        ConsultantStatus created = consultantStatusService.createStatus(dto);

        // Get by ID
        ConsultantStatus found = consultantStatusService.getStatusById(created.getId());

        assertEquals(created.getId(), found.getId());
    }

    @Test
    void testGetStatusById_NotFound() {
        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.getStatusById(999L));
    }

    @Test
    void testGetStatusesByConsultant_Success() {
        // Create multiple statuses
        ConsultantStatusDTO dto1 = new ConsultantStatusDTO();
        dto1.setConsultantId(consultant.getId());
        dto1.setStatus(ConsultantStatusType.AVAILABLE);
        dto1.setDateStart(LocalDate.of(2024, 1, 1));
        dto1.setDateEnd(LocalDate.of(2024, 1, 31));
        consultantStatusService.createStatus(dto1);

        ConsultantStatusDTO dto2 = new ConsultantStatusDTO();
        dto2.setConsultantId(consultant.getId());
        dto2.setStatus(ConsultantStatusType.BUSY);
        dto2.setDateStart(LocalDate.of(2024, 2, 1));
        dto2.setDateEnd(LocalDate.of(2024, 2, 28));
        consultantStatusService.createStatus(dto2);

        // Get by consultant
        List<ConsultantStatus> statuses = consultantStatusService.getStatusesByConsultant(consultant.getId());

        assertEquals(2, statuses.size());
    }

    @Test
    void testGetAllStatus() {
        // Create multiple statuses
        ConsultantStatusDTO dto1 = new ConsultantStatusDTO();
        dto1.setConsultantId(consultant.getId());
        dto1.setStatus(ConsultantStatusType.AVAILABLE);
        dto1.setDateStart(LocalDate.of(2024, 1, 1));
        dto1.setDateEnd(LocalDate.of(2024, 1, 31));
        consultantStatusService.createStatus(dto1);

        // Get all statuses
        List<ConsultantStatus> statuses = consultantStatusRepository.findAll();

        assertEquals(1, statuses.size());
    }

    @Test
    void testupdateStatus_Success() {
        ConsultantStatusDTO createDto = new ConsultantStatusDTO();
        createDto.setConsultantId(consultant.getId());
        createDto.setStatus(ConsultantStatusType.AVAILABLE);
        createDto.setDateStart(LocalDate.of(2024, 1, 1));
        createDto.setDateEnd(LocalDate.of(2024, 1, 31));
        createDto.setComment("Initial");
        ConsultantStatus created = consultantStatusService.createStatus(createDto);

        // Update
        ConsultantStatusDTO updateDto = new ConsultantStatusDTO();
        updateDto.setStatus(ConsultantStatusType.BUSY);
        updateDto.setDateStart(LocalDate.of(2024, 2, 1));
        updateDto.setDateEnd(LocalDate.of(2024, 2, 28));
        updateDto.setComment("Updated");
        ConsultantStatus updated = consultantStatusService.updateStatus(created.getId(), updateDto);

        assertEquals(ConsultantStatusType.BUSY, updated.getStatus());
        assertEquals("Updated", updated.getComment());
    }

    @Test
    void testUpdateStatus_NotFound() {
        ConsultantStatusDTO updateDto = new ConsultantStatusDTO();
        updateDto.setStatus(ConsultantStatusType.BUSY);
        updateDto.setDateStart(LocalDate.of(2024, 1, 1));
        updateDto.setDateEnd(LocalDate.of(2024, 1, 31));

        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.updateStatus(999L, updateDto));
    }

    @Test
    void testDeleteStatus_Success() {
        // Create status
        ConsultantStatusDTO dto = new ConsultantStatusDTO();
        dto.setConsultantId(consultant.getId());
        dto.setStatus(ConsultantStatusType.AVAILABLE);
        dto.setDateStart(LocalDate.of(2024, 1, 1));
        dto.setDateEnd(LocalDate.of(2024, 1, 31));
        ConsultantStatus created = consultantStatusService.createStatus(dto);

        // Delete
        consultantStatusService.deleteStatus(created.getId());

        // Verify deleted
        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.getStatusById(created.getId()));
    }

    @Test
    void testDeleteStatus_NotFound() {
        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.deleteStatus(999L));
    }
}
