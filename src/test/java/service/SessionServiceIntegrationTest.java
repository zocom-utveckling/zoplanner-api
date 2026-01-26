package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Session;
import com.zo.webapi.repository.SessionRepository;
import com.zo.webapi.service.AssignmentService;
import com.zo.webapi.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class SessionServiceIntegrationTest {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private AssignmentService assignmentService;

    private Assignment existingAssignment;
    private Session existingSession;

    @BeforeEach
    public void setup() {
        sessionRepository.deleteAll();

        existingAssignment = new Assignment("Java", 1L,
                LocalDateTime.of(2025, 1, 1, 10, 0).toLocalDate(),
                LocalDateTime.of(2025, 1, 31, 10, 0).toLocalDate(),
                100L);

        existingAssignment =  assignmentService.createAssignment(existingAssignment);

        existingSession = new Session(LocalDateTime.of(2025, 2, 1, 10, 0),
                LocalDateTime.of(2025, 2, 10, 10, 0));

        existingSession = sessionService.createSession(existingAssignment.getId(), existingSession);
    }

    @Test
    void testCreateSession_Success() {
        Session newSession = new Session(LocalDateTime.of(2025, 11, 1, 10, 0),
                LocalDateTime.of(2025, 11, 10, 10, 0));

        Session saved =  sessionService.createSession(existingAssignment.getId(), newSession);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAssignment().getId()).isEqualTo(existingAssignment.getId());
        assertThat(saved.getTimeStart()).isEqualTo(newSession.getTimeStart());
    }

    @Test
    void testGetSessionById_Success() {
        Optional<Session> found = sessionService.getSessionById(existingSession.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTimeStart()).isEqualTo(existingSession.getTimeStart());
    }

    @Test
    void testUpdateSession_NotFound() {
        Session update = new Session(LocalDateTime.of(2025, 12, 1, 10, 0),
                LocalDateTime.of(2025, 12, 10, 10, 0));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                sessionService.updateSession(999L, update));

        assertThat(exception.getMessage()).isEqualTo("Session with id 999 does not exist.");
    }

    @Test
    void testDeleteSession_NotFound() {
        boolean result = sessionService.deleteSessionById(999L);
        assertThat(result).isFalse();
    }



}*/
