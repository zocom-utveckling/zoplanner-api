
/*package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import com.zo.webapi.service.ConsultantStatusService;
import enums.ConsultantStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class ConsultantStatusServiceIntegrationTest {

    @Autowired
    private ConsultantStatusService statusService;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private ConsultantStatusRepository statusRepository;

    private Consultant consultant;

    @BeforeEach
    void setup() {
        statusRepository.deleteAll();
        consultantRepository.deleteAll();

        consultant = new Consultant();
        consultant.setName("Test Consultant");
        consultant.setEmail("test@consultant.com");
        consultantRepository.save(consultant);
    }

    @Test
    void testCreateStatus() {
        ConsultantStatus status = statusService.createStatus(
                consultant.getId(),
                ConsultantStatusType.AVAILABLE,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1),
                "Test note"
        );

        assertNotNull(status);
        assertEquals(consultant.getId(), status.getConsultant().getId());
        assertEquals(ConsultantStatusType.AVAILABLE, status.getStatus());
    }

    @Test
    void testGetStatusesByConsultant() {
        statusService.createStatus(
                consultant.getId(),
                ConsultantStatusType.BOOKED,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 10),
                null
        );

        List<ConsultantStatus> statuses = statusService.getStatusesByConsultant(consultant.getId());

        assertEquals(1, statuses.size());
        assertEquals(ConsultantStatusType.BOOKED, statuses.get(0).getStatus());
    }
}

*/