package service;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Session;
import com.zo.webapi.repository.SessionRepository;
import com.zo.webapi.service.AssignmentService;
import com.zo.webapi.service.SessionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AssignmentService assignmentService;
    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private Assignment assignment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assignment = new Assignment();
        assignment.setId(1L);

        session = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        session.setId(10L);
        session.setAssignment(assignment);
    }

    @Test
    void testGetAllSessions_shouldReturnList() {
        when(sessionRepository.findAll()).thenReturn(List.of(session));

        List<Session> result = sessionService.getAllSessions();

        assertEquals(1, result.size());
        assertEquals(session, result.get(0));
        verify(sessionRepository).findAll();
    }

    @Test
    void testGetSessionById_shouldReturnSessionIfFound() {
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));

        Optional<Session> result = sessionService.getSessionById(10L);

        assertTrue(result.isPresent());
        assertEquals(session, result.get());
        verify(sessionRepository).findById(10L);
    }

    @Test
    void testGetSessionById_shouldReturnEmptyIfNotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Session> result = sessionService.getSessionById(99L);

        assertFalse(result.isPresent());
        verify(sessionRepository).findById(99L);
    }

    @Test
    void testCreateSession_shouldReturnSavedSession_whenAssignmentExists() {
        when(assignmentService.getAssignmentById(1L)).thenReturn(Optional.of(assignment));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.createSession(1L, session);

        assertNotNull(result);
        assertEquals(assignment, result.getAssignment());
        verify(assignmentService).getAssignmentById(1L);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void testCreateSession_shouldReturnNull_whenAssignmentNotExists() {
        when(assignmentService.getAssignmentById(99L)).thenReturn(Optional.empty());

        Session result = sessionService.createSession(99L, session);

        assertNull(result);
        verify(assignmentService).getAssignmentById(99L);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testDeleteSessionById_Success() {
        when(sessionRepository.existsById(10L)).thenReturn(true);

        boolean result =  sessionService.deleteSessionById(10L);

        assertTrue(result);
        verify(sessionRepository).existsById(10L);
        verify(sessionRepository).deleteById(10L);
    }

    @Test
    void testDeleteSessionById_Failure() {
        when(sessionRepository.existsById(99L)).thenReturn(false);

        boolean result = sessionService.deleteSessionById(99L);

        assertFalse(result);
        verify(sessionRepository).existsById(99L);
        verify(sessionRepository, never()).deleteById(99L);
    }

}
