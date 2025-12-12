package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.*;
import com.zo.webapi.repository.*;
import com.zo.webapi.service.AssignmentService;
import com.zo.webapi.service.SessionService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class SessionServiceIntegrationTest {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private AssignmentService assignmentService;

    @Autowired private SessionRepository sessionRepository;
    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private ClassGroupRepository classGroupRepository;
    @Autowired private CustomerRepository  customerRepository;
    @Autowired private ConsultantRepository consultantRepository;
    @Autowired UserRepository userRepository;

    private Assignment assignment;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setName("Test User");
        user.setRole(UserRole.CONSULTANT);
        userRepository.save(user);

        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant.setCity("City");
        consultantRepository.save(consultant);


        Customer customer = new Customer();
        customer.setName("Customer a");
        customer.setCity("City");
        customerRepository.save(customer);

        ClassGroup cg = new ClassGroup();
        cg.setName("CG");
        cg.setCustomer(customer);
        classGroupRepository.save(cg);

        Course course = new Course();
        course.setName("Course a");
        course.setClassGroup(cg);
        course.setDateStart(LocalDate.now());
        course.setDateEnd(LocalDate.now().plusDays(1));
        courseRepository.save(course);

        assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(LocalDate.now());
        assignment.setDateEnd(LocalDate.now().plusDays(1));
        assignmentRepository.save(assignment);

    }

    @Test
    void testGetAllSessions() {
        assertThat(sessionService.getAllSessions()).isEmpty();

        Session session = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        session.setAssignment(assignment);
        sessionRepository.save(session);

        List<Session> allSessions = sessionService.getAllSessions();
        assertThat(allSessions).hasSize(1);

    }

    @Test
    void testGetSessionById() {
        Session session = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        session.setAssignment(assignment);
        sessionRepository.save(session);

        Optional<Session> found = sessionService.getSessionById(session.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(session.getId());

    }

    @Test
    void testGetSessionsByAssignmentId() {
        Session s1 = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        s1.setAssignment(assignment);

        Session s2 = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(3));
        s2.setAssignment(assignment);
        sessionRepository.save(s1);
        sessionRepository.save(s2);

        List<Session> result = sessionService.getSessionsByAssignmentId(assignment.getId());
        assertThat(result).hasSize(2);

    }

    @Test
    void testCreateSession() {
        Session session = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        session.setLocation(SessionLocation.ONSITE);
        session.setComment("Test Comment");

        Session created = sessionService.createSession(assignment.getId(), session);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getAssignment().getId()).isEqualTo(assignment.getId());
    }

    @Test
    void testCreateSessionWithNonExistingAssignment() {
        Session session = new Session();
        Session created = sessionService.createSession(999L, session);
        assertThat(created).isNull();
    }

    @Test
    void testUpdateSession() {
        Session session = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        session.setAssignment(assignment);
        session.setComment("Test Comment");
        sessionRepository.save(session);

        LocalDateTime start = LocalDateTime.of(2025, 12, 12, 12, 0, 0, 0);
        LocalDateTime end = start.plusHours(3);
        Session update = new Session(start, end);
        update.setComment("Updated Comment");
        update.setLocation(SessionLocation.REMOTE);

        Session updated = sessionService.updateSession(session.getId(), update);
        assertThat(updated.getComment()).isEqualTo("Updated Comment");
        assertThat(updated.getLocation()).isEqualTo(SessionLocation.REMOTE);
        assertThat(updated.getTimeEnd()).isEqualTo(start.plusHours(3));


    }

    @Test
    void testUpdateSessionNotFound() {
        Session update = new Session();
        assertThatThrownBy(() -> sessionService.updateSession(999L, update))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testDeleteSession() {
        Session session = new Session(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        session.setAssignment(assignment);
        sessionRepository.save(session);

        boolean deleted = sessionService.deleteSessionById(session.getId());

        assertThat(deleted).isTrue();
        assertThat(sessionRepository.findById(session.getId())).isEmpty();
    }

    @Test
    void testDeleteSessionNotFound() {
        boolean deleted = sessionService.deleteSessionById(999L);
        assertThat(deleted).isFalse();
    }


}
