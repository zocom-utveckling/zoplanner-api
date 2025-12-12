package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.enums.ConsultantStatusType;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.ConsultantStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ConsultantStatusServiceIntegrationTest {
    @Autowired private ConsultantStatusService consultantStatusService;
    @Autowired private ConsultantRepository consultantRepository;
    @Autowired private ConsultantStatusRepository consultantStatusRepository;
    @Autowired private UserRepository userRepository;

    private Consultant consultant;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("consultant1");
        user.setPassword("pass");
        user.setName("Consultant One");
        user.setRole(UserRole.CONSULTANT);
        userRepository.save(user);

        consultant = new Consultant();
        consultant.setUser(user);
        consultant.setCity("City A");
        consultantRepository.save(consultant);


    }


    @Test
    void testCreateStatus() {
        ConsultantStatusDTO dto = new ConsultantStatusDTO();
        dto.setConsultantId(consultant.getId());
        dto.setStatus(ConsultantStatusType.AVAILABLE);
        dto.setDateStart(LocalDate.now());
        dto.setDateEnd(LocalDate.now().plusDays(3));
        dto.setComment("comment");

        ConsultantStatus status = consultantStatusService.createStatus(dto);

        assertThat(status.getId()).isNotNull();
        assertThat(status.getStatus()).isEqualTo(ConsultantStatusType.AVAILABLE);
        assertThat(status.getConsultant().getId()).isEqualTo(consultant.getId());

    }

    @Test
    void testUpdateStatus() {
        ConsultantStatus status = new ConsultantStatus();
        status.setConsultant(consultant);
        status.setStatus(ConsultantStatusType.AVAILABLE);
        status.setDateStart(LocalDate.now());
        status.setDateEnd(LocalDate.now().plusDays(3));
        status.setComment("old comment");
        consultantStatusRepository.save(status);

        ConsultantStatusDTO dto = new ConsultantStatusDTO();
        dto.setConsultantId(consultant.getId());
        dto.setStatus(ConsultantStatusType.AVAILABLE);
        dto.setDateStart(LocalDate.now());
        dto.setDateEnd(LocalDate.now().plusDays(3));
        dto.setComment("new comment");

        ConsultantStatus updated = consultantStatusService.updateStatus(status.getId(), dto);

        assertThat(updated.getStatus()).isEqualTo(ConsultantStatusType.AVAILABLE);
        assertThat(updated.getComment()).isEqualTo("new comment");

    }

    @Test
    void testGetStatusNotFound() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                consultantStatusService.getStatusById(999L)
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("Status with ID 999 not found");
    }


    @Test
    void testDeleteStatus() {
        ConsultantStatus status = new ConsultantStatus();
        status.setConsultant(consultant);
        status.setStatus(ConsultantStatusType.AVAILABLE);
        status.setDateStart(LocalDate.now());
        status.setDateEnd(LocalDate.now().plusDays(3));
        consultantStatusRepository.save(status);

        consultantStatusService.deleteStatus(status.getId());
        assertThat(consultantStatusRepository.findById(status.getId())).isEmpty();
    }

}

