package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.repository.AssignmentRepository;
import com.zo.webapi.service.AssignmentService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional //Rollback DB changes after each test
public class AssignmentServiceIntegrationTest {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentService assignmentService;

    private Assignment existingAssignment;

    @BeforeEach
    void setUp() {
        assignmentRepository.deleteAll();

        existingAssignment = new Assignment(
                "Java", 1L,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 31),
                100L
        );

        existingAssignment = assignmentService.createAssignment(existingAssignment);
    }

    @Test
    void testCreateAssignment_Success() {
        Assignment newAssignment = new Assignment(
                "Testing", 2L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                200L
        );
        Assignment savedAssignment = assignmentService.createAssignment(newAssignment);

        assertThat(savedAssignment.getId()).isNotNull();
        assertThat(savedAssignment.getCourseName()).isEqualTo("Testing");

    }

    @Test
    void testGetAssignmentById_Success() {
        Optional<Assignment> found = assignmentService.getAssignmentById(existingAssignment.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCourseName()).isEqualTo(existingAssignment.getCourseName());
    }

    @Test
    void testUpdateAssignment_notFound() {
        Assignment updatedAssignment = new Assignment(
                "Java OOP", 3L,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 10),
                300L
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                assignmentService.updateAssignment(999L, updatedAssignment)
        );
        assertThat(exception.getMessage()).contains("Assignment not found with id: 999");
    }

    @Test
    void testDeleteAssignment_NotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                assignmentService.deleteAssignment(999L));
        assertThat(exception.getMessage()).contains("Assignment not found with id: 999");
    }
}*/
