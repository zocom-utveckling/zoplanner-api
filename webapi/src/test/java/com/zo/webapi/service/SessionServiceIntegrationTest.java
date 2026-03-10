package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Session;
import com.zo.webapi.model.SessionLocation;
import com.zo.webapi.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EntityManager entityManager;

    private Assignment assignment1;
    private Assignment assignment2;

    @BeforeEach
    void setup() {
        sessionRepository.deleteAll();

        // Skapa en testkonsult med korrekt Long ID
        Consultant testConsultant = new Consultant();
        testConsultant.setUserId(1L); // Om du har User-fält, sätt Long ID
        testConsultant.setCity("Test City"); // Om du har city-fält
        entityManager.persist(testConsultant);
        entityManager.flush(); // Genererar ID

        Long consultantId = testConsultant.getId(); // Få Long ID

        // Skapa två assignments med consultantId
        assignment1 = assignmentService.createAssignment(validAssignmentDTO(1, consultantId));
        assignment2 = assignmentService.createAssignment(validAssignmentDTO(2, consultantId));
    }

    private com.zo.webapi.dto.AssignmentDTO validAssignmentDTO(long suffix, Long consultantId) {
        com.zo.webapi.dto.AssignmentDTO dto = new com.zo.webapi.dto.AssignmentDTO();
        dto.setConsultantId(consultantId); // ✅ Long
        dto.setCourseId(1L);
        dto.setDateStart(LocalDate.of(2026, 1, (int) suffix));
        dto.setDateEnd(LocalDate.of(2026, 1, (int) (suffix + 2)));
        return dto;
    }

    @Test
    void testCreateSession_Success() {
        Session session = new Session();
        session.setTimeStart(LocalDateTime.of(2026, 1, 1, 10, 0));
        session.setTimeEnd(LocalDateTime.of(2026, 1, 1, 12, 0));
        session.setComment("Session 1 comment");
        session.setLocation(SessionLocation.REMOTE);

        Session created = sessionService.createSession(assignment1.getId(), session);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getAssignment().getId()).isEqualTo(assignment1.getId());
        assertThat(created.getComment()).isEqualTo("Session 1 comment");
        assertThat(created.getLocation()).isEqualTo(SessionLocation.REMOTE);
    }

    @Test
    void testGetSessionById_Success() {
        Session session = new Session();
        session.setTimeStart(LocalDateTime.of(2026, 2, 1, 10, 0));
        session.setTimeEnd(LocalDateTime.of(2026, 2, 1, 12, 0));
        session.setLocation(SessionLocation.ONSITE);

        session = sessionService.createSession(assignment1.getId(), session);

        Optional<Session> found = sessionService.getSessionById(session.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(session.getId());
    }

    @Test
    void testGetAllSessions_Success() {
        Session session1 = new Session();
        session1.setTimeStart(LocalDateTime.of(2026, 3, 1, 9, 0));
        session1.setTimeEnd(LocalDateTime.of(2026, 3, 1, 11, 0));
        session1.setLocation(SessionLocation.REMOTE);

        Session session2 = new Session();
        session2.setTimeStart(LocalDateTime.of(2026, 3, 2, 14, 0));
        session2.setTimeEnd(LocalDateTime.of(2026, 3, 2, 16, 0));
        session2.setLocation(SessionLocation.ONSITE);

        sessionService.createSession(assignment1.getId(), session1);
        sessionService.createSession(assignment2.getId(), session2);

        List<Session> allSessions = sessionService.getAllSessions();
        assertThat(allSessions).hasSize(2);
    }

    @Test
    void testUpdateSession_Success() {
        Session session = new Session();
        session.setTimeStart(LocalDateTime.of(2026, 4, 1, 10, 0));
        session.setTimeEnd(LocalDateTime.of(2026, 4, 1, 12, 0));
        session.setLocation(SessionLocation.ONSITE);

        session = sessionService.createSession(assignment1.getId(), session);

        Session updateSession = new Session();
        updateSession.setTimeStart(LocalDateTime.of(2026, 4, 1, 11, 0));
        updateSession.setTimeEnd(LocalDateTime.of(2026, 4, 1, 13, 0));
        updateSession.setComment("Updated comment");
        updateSession.setLocation(SessionLocation.HYBRID);

        Session updated = sessionService.updateSession(session.getId(), updateSession);

        assertThat(updated.getTimeStart()).isEqualTo(updateSession.getTimeStart());
        assertThat(updated.getTimeEnd()).isEqualTo(updateSession.getTimeEnd());
        assertThat(updated.getComment()).isEqualTo("Updated comment");
        assertThat(updated.getLocation()).isEqualTo(SessionLocation.HYBRID);
    }

    @Test
    void testUpdateSession_NotFound() {
        Session session = new Session();
        session.setTimeStart(LocalDateTime.now());
        session.setTimeEnd(LocalDateTime.now().plusHours(1));
        session.setLocation(SessionLocation.REMOTE);

        assertThrows(ResourceNotFoundException.class, () ->
                sessionService.updateSession(9999L, session));
    }

    @Test
    void testDeleteSession_Success() {
        Session session = new Session();
        session.setTimeStart(LocalDateTime.of(2026, 5, 1, 10, 0));
        session.setTimeEnd(LocalDateTime.of(2026, 5, 1, 12, 0));
        session.setLocation(SessionLocation.REMOTE);

        session = sessionService.createSession(assignment1.getId(), session);

        boolean deleted = sessionService.deleteSessionById(session.getId());
        assertThat(deleted).isTrue();
        assertThat(sessionRepository.existsById(session.getId())).isFalse();
    }

    @Test
    void testDeleteSession_NotFound() {
        boolean deleted = sessionService.deleteSessionById(9999L);
        assertThat(deleted).isFalse();
    }
}