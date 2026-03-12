package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.enums.ConsultantStatusType;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.ConsultantStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private Consultant consultant;

    private ConsultantStatusDTO statusDTO;


    @BeforeEach
    void setup() {
        consultantRepository.deleteAll();
        managerRepository.deleteAll();
        userRepository.deleteAll();
        statusRepository.deleteAll();

        userRepository.save(new User(null, "testuser", "testuser", "testuser", "e@mail.com", "Stockholm", UserRole.CONSULTANT));

        consultant = new Consultant();
        consultant.setUser(userRepository.findAll().getFirst());
        consultantRepository.save(consultant);
        System.out.println(consultantRepository.findAll());

        //create consultantDto
        statusDTO = new ConsultantStatusDTO();
        statusDTO.setStatus(ConsultantStatusType.AVAILABLE);
        statusDTO.setConsultantId(consultantRepository.findAll().getFirst().getId());
        statusDTO.setDateStart(LocalDate.of(2026, 10, 13));
        statusDTO.setDateEnd(LocalDate.of(2026, 12, 13));
        statusDTO.setComment("test comment");
    }

    @Test
    void testCreateStatus() {
        ConsultantStatus status = statusService.createStatus(statusDTO);
        assertNotNull(status);
        assertEquals(ConsultantStatusType.AVAILABLE, status.getStatus());
        assertEquals(consultant.getId(), status.getConsultant().getId());
        assertEquals(LocalDate.of(2026, 10, 13), status.getDateStart());
        assertEquals(LocalDate.of(2026, 12, 13), status.getDateEnd());
        assertEquals("test comment", status.getComment());
    }

    @Test
    void testGetStatusesByConsultant() {
        statusService.createStatus(statusDTO);
        List<ConsultantStatus> statuses = statusService.getStatusesByConsultant(consultant.getId());

        assertEquals(1, statuses.size());
        assertEquals(ConsultantStatusType.AVAILABLE, statuses.getFirst().getStatus());
    }

    @Test
    void testGetStatusesByConsultant_HasNoStatus() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> statusService.getStatusesByConsultant(consultant.getId()));
        assertEquals("404 NOT_FOUND \"No statuses found for consultant ID " + consultant.getId() + "\"", exception.getMessage());
    }
}
