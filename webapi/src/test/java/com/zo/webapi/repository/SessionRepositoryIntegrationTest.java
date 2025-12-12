package com.zo.webapi.repository;

import com.zo.webapi.model.*;
import com.zo.webapi.enums.UserRole;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.h2.engine.Engine.createSession;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real Postgres
@ActiveProfiles("test")
public class SessionRepositoryIntegrationTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClassGroupRepository classRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private UserRepository userRepository;

    private Assignment assignment;

    @BeforeEach
    void setup() {
        // Clean up any existing data
        sessionRepository.deleteAll();
        assignmentRepository.deleteAll();
        courseRepository.deleteAll();
        classRepository.deleteAll();
        customerRepository.deleteAll();
        consultantRepository.deleteAll();
        userRepository.deleteAll();



        // 1. Create User
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        user.setName("Test User");
        user.setRole(UserRole.CONSULTANT);
        userRepository.save(user);

        // 2. Create Consultant
        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant.setCity("Test City");
        consultantRepository.save(consultant);

        // 3. Create Customer
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setCity("Test City");
        customerRepository.save(customer);

        // 4. Create Class
        ClassGroup classGroup = new ClassGroup();
        classGroup.setName("Test Class");
        classGroup.setCustomer(customer);
        classRepository.save(classGroup);

        // 5. Create Course
        Course course = new Course();
        course.setName("Test Course");
        course.setClassGroup(classGroup);
        course.setDateStart(LocalDate.now());
        course.setDateEnd(LocalDate.now().plusDays(5));
        courseRepository.save(course);

        // 6. Create Assignment
        assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(LocalDate.now());
        assignment.setDateEnd(LocalDate.now().plusDays(5));
        assignmentRepository.save(assignment);
    }

    @Test
    void testSaveAndFindSession() {
        // Create a session
        Session session = new Session();
        session.setAssignment(assignment);
        session.setTimeStart(LocalDateTime.now());
        session.setTimeEnd(LocalDateTime.now().plusHours(2));
        session.setLocation(SessionLocation.ONSITE);
        session.setComment("Integration test session");

        sessionRepository.save(session);

        // Verify session saved
        List<Session> sessions = sessionRepository.findSessionsByAssignmentId(assignment.getId());
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getComment()).isEqualTo("Integration test session");
    }


    @Test
    void testFindSessionWhenNoneExist() {
        List<Session> sessions = sessionRepository.findSessionsByAssignmentId(assignment.getId());

        assertThat(sessions).isEmpty();
    }

    @Test
    void testFindMultipleSessions() {
        Session s1 = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        s1.setAssignment(assignment);
        s1.setLocation(SessionLocation.REMOTE);

        Session s2 = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        s2.setAssignment(assignment);
        s2.setLocation(SessionLocation.ONSITE);

        sessionRepository.save(s1);
        sessionRepository.save(s2);

        List<Session> sessions = sessionRepository.findSessionsByAssignmentId(assignment.getId());

        assertThat(sessions).hasSize(2);
        assertThat(sessions).extracting(Session::getLocation).containsExactly(SessionLocation.REMOTE, SessionLocation.ONSITE);


    }

    @Test
    void testFindSessionByCorrectAssignment() {
        Assignment otherAssignment = new Assignment();
        otherAssignment.setConsultant(assignment.getConsultant());
        otherAssignment.setCourse(assignment.getCourse());
        otherAssignment.setDateStart(LocalDate.now());
        otherAssignment.setDateEnd(LocalDate.now().plusDays(5));
        assignmentRepository.save(otherAssignment);

        Session s1 =  new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        s1.setAssignment(assignment);
        sessionRepository.save(s1);

        Session s2 =  new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        s2.setAssignment(otherAssignment);
        sessionRepository.save(s2);
        List<Session> result = sessionRepository.findSessionsByAssignmentId(assignment.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAssignment().getId()).isEqualTo(assignment.getId());


    }

    @Test
    void testEnumAndTimeStampsRemainsCorrect() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10 ,9, 0);
        LocalDateTime end = start.plusHours(2);

        Session session = new Session();
        session.setAssignment(assignment);
        session.setTimeStart(start);
        session.setTimeEnd(end);
        session.setLocation(SessionLocation.ONSITE);

        sessionRepository.save(session);

        List<Session> sessions = sessionRepository.findSessionsByAssignmentId(assignment.getId());
        Session loaded = sessions.get(0);

        assertThat(loaded.getTimeStart()).isEqualTo(start);
        assertThat(loaded.getTimeEnd()).isEqualTo(end);
        assertThat(loaded.getLocation()).isEqualTo(SessionLocation.ONSITE);

    }





}
