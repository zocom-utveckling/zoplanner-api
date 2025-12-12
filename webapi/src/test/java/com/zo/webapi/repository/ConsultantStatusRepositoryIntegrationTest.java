package com.zo.webapi.repository;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.enums.ConsultantStatusType;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class ConsultantStatusRepositoryIntegrationTest {
    @Autowired
    private ConsultantStatusRepository consultantStatusRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private UserRepository userRepository;

    private Consultant consultant;

    @BeforeEach
    void setup() {
        consultantStatusRepository.deleteAll();
        consultantRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        User user = new User();
        user.setUsername("consultant1");
        user.setPassword("password123");
        user.setName("Test Consultant");
        user.setRole(UserRole.CONSULTANT);
        user = userRepository.save(user);

        // Create test consultant
        consultant = new Consultant();
        consultant.setUser(user);
        consultant.setCity("Stolkholm");
        consultant = consultantRepository.save(consultant);
    }

    @Test
    void testFindByConsultantId() {
        // Arrange
        ConsultantStatus status1 = new ConsultantStatus();
        status1.setConsultant(consultant);
        status1.setStatus(ConsultantStatusType.AVAILABLE);
        status1.setDateStart(LocalDate.of(2024, 1, 1));
        status1.setDateEnd(LocalDate.of(2024, 1, 31));
        consultantStatusRepository.save(status1);

        ConsultantStatus status2 = new ConsultantStatus();
        status2.setConsultant(consultant);
        status2.setStatus(ConsultantStatusType.BUSY);
        status2.setDateStart(LocalDate.of(2024, 2, 1));
        status2.setDateEnd(LocalDate.of(2024, 2, 28));
        consultantStatusRepository.save(status2);

        // Act
        List<ConsultantStatus> statuses = consultantStatusRepository.findByConsultantId(consultant.getId());

        // Assert
        assertEquals(2, statuses.size());
        assertTrue(statuses.stream().allMatch(s -> s.getConsultant().getId().equals(consultant.getId())));
    }

    @Test
    void testFindByStatus() {
        // Arrange
        ConsultantStatus status1 = new ConsultantStatus();
        status1.setConsultant(consultant);
        status1.setStatus(ConsultantStatusType.AVAILABLE);
        status1.setDateStart(LocalDate.of(2024, 1, 1));
        status1.setDateEnd(LocalDate.of(2024, 1, 31));
        consultantStatusRepository.save(status1);

        ConsultantStatus status2 = new ConsultantStatus();
        status2.setConsultant(consultant);
        status2.setStatus(ConsultantStatusType.AVAILABLE);
        status2.setDateStart(LocalDate.of(2024, 3, 1));
        status2.setDateEnd(LocalDate.of(2024, 3, 31));
        consultantStatusRepository.save(status2);

        // Act
        List<ConsultantStatus> availableStatuses = consultantStatusRepository.findByStatus(ConsultantStatusType.AVAILABLE);

        // Assert
        assertEquals(2, availableStatuses.size());
        assertTrue(availableStatuses.stream().allMatch(s -> s.getStatus() == ConsultantStatusType.AVAILABLE));
    }

    @Test
    void testFindByConsultantIdAndDate() {
        // Arrange
        ConsultantStatus status1 = new ConsultantStatus();
        status1.setConsultant(consultant);
        status1.setStatus(ConsultantStatusType.VACATION);
        status1.setDateStart(LocalDate.of(2024, 1, 1));
        status1.setDateEnd(LocalDate.of(2024, 1, 31));
        consultantStatusRepository.save(status1);

        ConsultantStatus status2 = new ConsultantStatus();
        status2.setConsultant(consultant);
        status2.setStatus(ConsultantStatusType.BUSY);
        status2.setDateStart(LocalDate.of(2024, 2, 1));
        status2.setDateEnd(LocalDate.of(2024, 2, 28));
        consultantStatusRepository.save(status2);

        // Act - Find statuses for date within first status period
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        List<ConsultantStatus> statuses = consultantStatusRepository.findByConsultantIdAndDate(consultant.getId(), testDate);

        // Assert
        assertEquals(1, statuses.size());
        assertEquals(ConsultantStatusType.VACATION, statuses.get(0).getStatus());
    }

    @Test
    void testFindActiveStatusesOnDate() {
        // Arrange
        ConsultantStatus status1 = new ConsultantStatus();
        status1.setConsultant(consultant);
        status1.setStatus(ConsultantStatusType.AVAILABLE);
        status1.setDateStart(LocalDate.of(2024, 1, 1));
        status1.setDateEnd(LocalDate.of(2024, 1, 31));
        consultantStatusRepository.save(status1);

        ConsultantStatus status2 = new ConsultantStatus();
        status2.setConsultant(consultant);
        status2.setStatus(ConsultantStatusType.SICK);
        status2.setDateStart(LocalDate.of(2024, 3, 1));
        status2.setDateEnd(LocalDate.of(2024, 3, 31));
        consultantStatusRepository.save(status2);

        // Act - Find active statuses on a date within first status
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        List<ConsultantStatus> activeStatuses = consultantStatusRepository.findActiveStatusesOnDate(testDate);

        // Assert
        assertEquals(1, activeStatuses.size());
        assertEquals(ConsultantStatusType.AVAILABLE, activeStatuses.get(0).getStatus());
    }
}
