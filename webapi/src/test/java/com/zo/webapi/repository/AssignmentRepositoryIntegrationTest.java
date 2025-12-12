package com.zo.webapi.repository;

import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AssignmentRepositoryIntegrationTest {
    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ClassGroupRepository classGroupRepository;
    @Autowired private ConsultantRepository consultantRepository;
    @Autowired private  UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;

    private Consultant consultant;
    private Course course;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("consultant1");
        user.setPassword("password");
        user.setName("Consultant 1");
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

        ClassGroup group = new ClassGroup("Class a", customer);
        classGroupRepository.save(group);

        course = new Course("Course 1", group, LocalDate.now(), LocalDate.now().plusDays(5));
        courseRepository.save(course);
    }

    @Test
    void testSaveAndFindById() {
        Assignment assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(LocalDate.now());
        assignment.setDateEnd(LocalDate.now().plusDays(3));
        assignmentRepository.save(assignment);

        Optional<Assignment> found = assignmentRepository.findById(assignment.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getConsultant().getId()).isEqualTo(consultant.getId());
    }

    @Test
    void testFindByConsultantId() {
        Assignment assignment1 = new Assignment();
        assignment1.setConsultant(consultant);
        assignment1.setCourse(course);
        assignment1.setDateStart(LocalDate.now());
        assignment1.setDateEnd(LocalDate.now().plusDays(3));


        Assignment assignment2 = new Assignment();
        assignment2.setConsultant(consultant);
        assignment2.setCourse(course);
        assignment2.setDateStart(LocalDate.now());
        assignment2.setDateEnd(LocalDate.now().plusDays(1));
        assignmentRepository.save(assignment1);
        assignmentRepository.save(assignment2);


        List<Assignment> assignments = assignmentRepository.findByConsultant_Id(consultant.getId());

        assertThat(assignments).hasSize(2);

    }

    @Test
    void testDeleteAssignment() {
        Assignment assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(LocalDate.now());
        assignment.setDateEnd(LocalDate.now().plusDays(3));
        assignmentRepository.save(assignment);

        assignmentRepository.deleteById(assignment.getId());
        Optional<Assignment> deleted = assignmentRepository.findById(assignment.getId());
        assertThat(deleted).isEmpty();

    }

    @Test
    void testFindByConsultantIdNonExistent() {
        List<Assignment> assignments = assignmentRepository.findByConsultant_Id(999L);
        assertThat(assignments).isEmpty();

    }




}
