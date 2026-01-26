package com.zo.webapi.repository;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Session;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*@DataJpaTest
public class SessionRepositoryIntegrationTest {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    AssignmentRepository assignmentRepository;

    @Test
    void testFindSessionByAssignmentId(){
        // Arrange
        Assignment assignment = new Assignment("Java", 1L, LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 10), 100L);
        assignmentRepository.save(assignment);

        Session session1 = new Session(LocalDateTime.of(2025,1,1,9,0),
                LocalDateTime.of(2025,1,1,12,0));
        session1.setAssignment(assignment);

        Session session2 = new Session(LocalDateTime.of(2025,1,2,13,0),
                LocalDateTime.of(2025,1,2,16,0));
        session2.setAssignment(assignment);

        sessionRepository.save(session1);
        sessionRepository.save(session2);

        // Act
        List<Session> sessions = sessionRepository.findSessionsByAssignmentId(assignment.getId());

        // Assert
        assertThat(sessions).hasSize(2);
        assertThat(sessions).extracting(Session::getTimeStart)
                .contains(LocalDateTime.of(2025,1,1,9,0),
                        LocalDateTime.of(2025,1,2,13,0));
    }

    @Test
    void testFindSessions_NotFound() {
        // Arrange
        Assignment assignment = new Assignment("Java", 200L,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 10), 100L);

        assignmentRepository.save(assignment);

        // Act
        List<Session> sessions = sessionRepository.findSessionsByAssignmentId(assignment.getId());

        // Assert
        assertThat(sessions).isEmpty();

    }

    @Test
    void testSaveMultipleAssignmentsEachWithSession(){
        // Arrange
        Assignment assignment1 = new Assignment("Java", 300L, LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 10), 10L);

        Assignment assignment2 = new Assignment("Testing", 301L, LocalDate.of(2025,2, 1),
                LocalDate.of(2025, 2, 10), 11L);

        assignmentRepository.save(assignment1);
        assignmentRepository.save(assignment2);

        Session session1 = new Session(LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 12,0));
        session1.setAssignment(assignment1);

        Session session2 = new Session(LocalDateTime.of(2025, 2, 1, 12, 0),
                LocalDateTime.of(2025, 2, 10, 16, 0));
        session2.setAssignment(assignment2);

        sessionRepository.save(session1);
        sessionRepository.save(session2);

        // Act
        List<Session> sessionsA1 = sessionRepository.findSessionsByAssignmentId(assignment1.getId());
        List<Session> sessionsA2 = sessionRepository.findSessionsByAssignmentId(assignment2.getId());

        // Assert
        assertThat(sessionsA1).hasSize(1);
        assertThat(sessionsA1.get(0).getAssignment().getCourseName()).isEqualTo("Java");
        assertThat(sessionsA2).hasSize(1);
        assertThat(sessionsA2.get(0).getAssignment().getCourseName()).isEqualTo("Testing");


    }

    @Test
    void testFindSessionsByNonExistentAssignmentId(){
        // Act
        List<Session> sessions = sessionRepository.findSessionsByAssignmentId(99L);

        // Assert
        assertThat(sessions).isEmpty();
    }

    @Test
    void testSaveSessionWithoutAssignment() {
        // Arrange
        Session invalidSession = new Session(LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 10, 12,0));

        // Act and Assert
        assertThrows(Exception.class, () -> sessionRepository.saveAndFlush(invalidSession));
    }
}
*/