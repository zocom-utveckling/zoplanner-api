package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.*;
import com.zo.webapi.repository.*;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private ConsultantRepository consultantRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ClassGroupRepository classGroupRepository;

    private Consultant consultant1;
    private Course course1;

    @BeforeEach
    void setUp() {
        assignmentRepository.deleteAll();

        consultant1 = persistConsultant("consultant.one", "Consultant One", "consultant.one@mail.com", "Stockholm");
        course1 = persistCourseWithClassGroup("Course A", "Class A", "Customer A", "Stockholm");

    }

    @Test
    void testCreateAssignment_Success() {
        AssignmentDTO dto = validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 5)
        );

        Assignment created = assignmentService.createAssignment(dto);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getConsultant()).isNotNull();
        assertThat(created.getConsultant().getId()).isEqualTo(consultant1.getId());
        assertThat(created.getCourse()).isNotNull();
        assertThat(created.getCourse().getId()).isEqualTo(course1.getId());
        assertThat(created.getDateStart()).isEqualTo(dto.getDateStart());
        assertThat(created.getDateEnd()).isEqualTo(dto.getDateEnd());

        assertThat(assignmentRepository.findById(created.getId())).isPresent();
    }

    @Test
    void testGetAssignmentById_Success() {
        Assignment created = assignmentService.createAssignment(validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 5)
        ));

        Optional<Assignment> found = assignmentService.getAssignmentById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(created.getId());
    }

    @Test
    void testGetAssignmentsByConsultant_Success() {
        assignmentService.createAssignment(validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 5)
        ));

        assignmentService.createAssignment(validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 3, 10),
                LocalDate.of(2026, 3, 15)
        ));

        List<Assignment> assignmentList = assignmentService.getAssignmentsByConsultant(consultant1.getId());

        assertThat(assignmentList).hasSize(2);
        assertThat(assignmentList).allMatch(a -> a.getConsultant().getId().equals(consultant1.getId()));
    }

    @Test
    void testUpdateAssignment_Success() {
        Assignment created = assignmentService.createAssignment(validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 5)
        ));
        Consultant consultant2 = persistConsultant(
                "consultant.two", "Consultant Two", "consultant.two@mail.com", "Göteborg");

        Course course2 = persistCourseWithClassGroup(
                "Course B", "Class B", "Customer B", "Göteborg");

        AssignmentDTO updateDTO = validDto(
                consultant2.getId(),
                course2.getId(),
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15)
        );

        Assignment updated = assignmentService.updateAssignment(created.getId(), updateDTO);

        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getConsultant().getId()).isEqualTo(consultant2.getId());
        assertThat(updated.getCourse().getId()).isEqualTo(course2.getId());
        assertThat(updated.getDateStart()).isEqualTo(updateDTO.getDateStart());
        assertThat(updated.getDateEnd()).isEqualTo(updateDTO.getDateEnd());
    }

    @Test
    void testDeleteAssignment_Success() {
        Assignment created = assignmentService.createAssignment(validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 5)
        ));

        assignmentService.deleteAssignment(created.getId());

        assertThat(assignmentRepository.existsById(created.getId())).isFalse();
    }

    @Test
    void testGetAssignmentByConsultant_ConsultantNotFound() {
        long consultantNotFoundId = 9999L;

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.getAssignmentsByConsultant(consultantNotFoundId));

        assertThat(exc.getMessage()).contains("Consultant");
    }

    @Test
    void testCreateAssignment_ConsultantNotFound() {
        long consultantNotFoundId = 9999L;

        AssignmentDTO dto = validDto(
                consultantNotFoundId,
                course1.getId(),
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 5)
        );

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.createAssignment(dto));

        assertThat(exc.getMessage()).contains("Consultant");
    }

    @Test
    void testCreateAssignment_CourseNotFound() {
        long courseNotFoundId = 9999L;

        AssignmentDTO dto = validDto(
                consultant1.getId(),
                courseNotFoundId,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5)
        );

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.createAssignment(dto));

        assertThat(exc.getMessage()).contains("Course");
    }

    @Test
    void testCreateAssignment_InvalidDates() {
        AssignmentDTO dto = validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 8, 5),
                LocalDate.of(2026, 8, 1)
        );

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> assignmentService.createAssignment(dto));

        assertThat(exc.getMessage()).containsIgnoringCase("Start date");
    }

    @Test
    void testCreateAssignment_MissingFields() {
        AssignmentDTO dto = new AssignmentDTO(); // alla fält null

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> assignmentService.createAssignment(dto));

        assertThat(exc.getMessage()).isNotBlank();
    }

    @Test
    void testUpdateAssignment_AssignmentNotFound() {
        long assignmentNotFoundId = 9999L;
        AssignmentDTO dto = validDto(
                consultant1.getId(),
                course1.getId(),
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 5)
        );

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.updateAssignment(assignmentNotFoundId, dto));

        assertThat(exc.getMessage()).contains("Assignment");
    }

    @Test
    void testDeleteAssignment_NotFound(){
        long notFoundId = 9999L;

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.deleteAssignment(notFoundId));

        assertThat(exc.getMessage()).contains("Assignment");
    }

    // Helpers

    private AssignmentDTO validDto(Long consultantId, Long courseId, LocalDate start, LocalDate end) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setConsultantId(consultantId);
        dto.setCourseId(courseId);
        dto.setDateStart(start);
        dto.setDateEnd(end);
        return dto;
    }

    private Consultant persistConsultant(String username, String name, String email, String city) {
        User u = new User();
        u.setUsername(username);
        u.setPassword("pw");
        u.setName(name);
        u.setEmail(email);
        u.setCity(city);
        u.setRole(UserRole.CONSULTANT);
        u = userRepository.save(u);

        Consultant c = new Consultant();
        c.setUser(u);
        c.setCity(city);
        return consultantRepository.save(c);
    }

    private Course persistCourseWithClassGroup(String courseName, String className, String customerName, String customerCity) {
        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setCity(customerCity);
        // manager får vara null
        customer = customerRepository.save(customer);

        ClassGroup cg = new ClassGroup();
        cg.setName(className);
        cg.setCustomer(customer);
        cg = classGroupRepository.save(cg);

        Course course = new Course();
        course.setName(courseName);
        course.setClassGroup(cg);
        // dateStart/dateEnd är nullable, så vi kan lämna null
        return courseRepository.save(course);
    }
}
