package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.model.*;
import com.zo.webapi.repository.*;
import com.zo.webapi.service.AssignmentService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional //Rollback DB changes after each test
public class AssignmentServiceIntegrationTest {
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository
            ;
    @Autowired
    private ClassGroupRepository classGroupRepository;

    private Consultant consultant;
    private Course course;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setUsername("consultant1");
        user.setPassword("password");
        user.setName("Consultant One");
        user.setRole(UserRole.CONSULTANT);
        userRepository.save(user);

        consultant = new Consultant();
        consultant.setUser(user);
        consultant.setCity("City a");
        consultantRepository.save(consultant);

        Customer customer = new Customer();
        customer.setName("Customer a");
        customer.setCity("City b");
        customerRepository.save(customer);

        ClassGroup group = new ClassGroup("Class 1", customer);
        classGroupRepository.save(group);

        course = new Course("Course 1", group, LocalDate.now(), LocalDate.now().plusDays(3));
        courseRepository.save(course);

    }


    @Test
    void testCreateAssignment() {
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setConsultantId(consultant.getId());
        assignmentDTO.setCourseId(course.getId());
        assignmentDTO.setDateStart(LocalDate.now());
        assignmentDTO.setDateEnd(LocalDate.now().plusDays(1));

        Assignment created = assignmentService.createAssignment(assignmentDTO);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getConsultant().getId()).isEqualTo(consultant.getId());
        assertThat(created.getCourse().getId()).isEqualTo(course.getId());

    }

    @Test
    void testGetAssignmentByConsultant() {
        Assignment assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(LocalDate.now());
        assignment.setDateEnd(LocalDate.now().plusDays(1));
        assignmentRepository.save(assignment);


        List<Assignment> assignments = assignmentService.getAssignmentsByConsultant(consultant.getId());

        assertThat(assignments).hasSize(1);
        assertThat(assignments.get(0).getId()).isEqualTo(assignment.getId());


    }


    @Test
    void testUpdateAssignment() {
        Assignment assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(LocalDate.now());
        assignment.setDateEnd(LocalDate.now().plusDays(1));
        assignmentRepository.save(assignment);

        AssignmentDTO updateDto = new AssignmentDTO();
        updateDto.setConsultantId(consultant.getId());
        updateDto.setCourseId(course.getId());
        updateDto.setDateStart(LocalDate.now().plusDays(2));
        updateDto.setDateEnd(LocalDate.now().plusDays(5));

        Assignment updated = assignmentService.updateAssignment(assignment.getId(), updateDto);

        assertThat(updated.getDateStart()).isEqualTo(LocalDate.now().plusDays(2));
        assertThat(updated.getDateEnd()).isEqualTo(LocalDate.now().plusDays(5));

    }

    @Test
    void testCreateAssignmentInvalidDTO() {
        AssignmentDTO assignmentDTO = new AssignmentDTO();

        assertThatThrownBy(() -> assignmentService.createAssignment(assignmentDTO))
        .isInstanceOf(InvalidDataException.class)
                .hasMessage("Consultant ID is required");
    }
}
