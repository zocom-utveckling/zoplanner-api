package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.*;
import com.zo.webapi.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired private SessionService sessionService;
    @Autowired private AssignmentService assignmentService;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ConsultantRepository consultantRepository;
    @Autowired private ManagerRepository managerRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ClassGroupRepository classGroupRepository;
    @Autowired private AssignmentRepository assignmentRepository;

    private Assignment assignment1;
    private Assignment assignment2;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        assignmentRepository.deleteAll();
        courseRepository.deleteAll();
        classGroupRepository.deleteAll();
        customerRepository.deleteAll();

        // Consultant
        User user = new User();
        user.setUsername("session.user");
        user.setPassword("pw");
        user.setName("Session User");
        user.setEmail("session@test.com");
        user.setCity("Stockholm");
        user.setRole(UserRole.CONSULTANT);
        user = userRepository.save(user);

        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant = consultantRepository.save(consultant);

        // Manager
        User user2 = new User();
        user2.setUsername("session.user2");
        user2.setPassword("pw");
        user2.setName("Session User2");
        user2.setEmail("session2@test.com");
        user2.setCity("Stockholm");
        user2.setRole(UserRole.MANAGER);
        user2 = userRepository.save(user2);

        Manager manager = new Manager(user2);
        manager = managerRepository.save(manager);

        // Course
        Customer customer = new Customer();
        customer.setName("Customer A");
        customer.setCity("Stockholm");
        customer = customerRepository.save(customer);

        ClassGroup classGroup = new ClassGroup();
        classGroup.setName("Class A");
        classGroup.setCustomer(customer);
        classGroup = classGroupRepository.save(classGroup);

        Course course = new Course();
        course.setName("Course A");
        course.setClassGroup(classGroup);
        course.setDateStart(LocalDate.of(2026, 1, 1));
        course.setDateEnd(LocalDate.of(2026, 12, 31));
        course = courseRepository.save(course);

        // Assignments
        com.zo.webapi.dto.AssignmentDTO dto1 = new com.zo.webapi.dto.AssignmentDTO();
        dto1.setConsultantId(consultant.getId());
        dto1.setCourseId(course.getId());
        dto1.setDateStart(LocalDate.of(2026, 1, 1));
        dto1.setDateEnd(LocalDate.of(2026, 6, 30));
        dto1.setManagerId(manager.getId());
        assignment1 = assignmentService.createAssignment(dto1);

        com.zo.webapi.dto.AssignmentDTO dto2 = new com.zo.webapi.dto.AssignmentDTO();
        dto2.setConsultantId(consultant.getId());
        dto2.setCourseId(course.getId());
        dto2.setDateStart(LocalDate.of(2026, 7, 1));
        dto2.setDateEnd(LocalDate.of(2026, 12, 31));
        dto2.setManagerId(manager.getId());
        assignment2 = assignmentService.createAssignment(dto2);
    }

    @Test
    void testCreateSession_Success() {
        Session session = new Session("Session 1",
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0));
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
        Session session = new Session("Session 2",
                LocalDateTime.of(2026, 2, 1, 10, 0),
                LocalDateTime.of(2026, 2, 1, 12, 0));
        session.setLocation(SessionLocation.ONSITE);

        session = sessionService.createSession(assignment1.getId(), session);

        Optional<Session> found = sessionService.getSessionById(session.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(session.getId());
    }

    @Test
    void testGetAllSessions_Success() {
        Session session1 = new Session("Session 3",
                LocalDateTime.of(2026, 3, 1, 9, 0),
                LocalDateTime.of(2026, 3, 1, 11, 0));
        session1.setLocation(SessionLocation.REMOTE);

        Session session2 = new Session("Session 4",
                LocalDateTime.of(2026, 3, 2, 14, 0),
                LocalDateTime.of(2026, 3, 2, 16, 0));
        session2.setLocation(SessionLocation.ONSITE);

        sessionService.createSession(assignment1.getId(), session1);
        sessionService.createSession(assignment2.getId(), session2);

        List<Session> allSessions = sessionService.getAllSessions();
        assertThat(allSessions).hasSize(2);
    }

    @Test
    void testUpdateSession_Success() {
        Session session = new Session("Session 5",
                LocalDateTime.of(2026, 4, 1, 10, 0),
                LocalDateTime.of(2026, 4, 1, 12, 0));
        session.setLocation(SessionLocation.ONSITE);

        session = sessionService.createSession(assignment1.getId(), session);

        Session updateSession = new Session("Updated Session",
                LocalDateTime.of(2026, 4, 1, 11, 0),
                LocalDateTime.of(2026, 4, 1, 13, 0));
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
        Session session = new Session("Ghost Session",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        session.setLocation(SessionLocation.REMOTE);

        assertThrows(EntityNotFoundException.class, () ->
                sessionService.updateSession(9999L, session));
    }

    @Test
    void testDeleteSession_Success() {
        Session session = new Session("Session 6",
                LocalDateTime.of(2026, 5, 1, 10, 0),
                LocalDateTime.of(2026, 5, 1, 12, 0));
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