package service;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.repository.AssignmentRepository;
import com.zo.webapi.service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.support.AbstractInterruptibleBatchPreparedStatementSetter;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/*public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;
    @InjectMocks
    private AssignmentService assignmentService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAssignments() {
        when(assignmentRepository.findAll()).thenReturn(List.of(new Assignment(), new Assignment()));
        List<Assignment> result =  assignmentService.getAllAssignments();

        assertEquals(2,  result.size());
        verify(assignmentRepository, times(1)).findAll();

    }

    @Test
    void testGetAssignmentById() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        Optional<Assignment> result = assignmentService.getAssignmentById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L,result.get().getId());
        verify(assignmentRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateAssignment() {
        Assignment assignment = new Assignment();
        when(assignmentRepository.save(assignment)).thenReturn(assignment);
        Assignment saved = assignmentService.createAssignment(assignment);
        assertNotNull(saved);
        verify(assignmentRepository).save(assignment);
    }

    @Test
    void testUpdateAssignment() {
        Assignment existing = new Assignment();
        existing.setId(1L);
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(assignmentRepository.save(any())).thenReturn(existing);

        Assignment updatedDetails = new Assignment();
        updatedDetails.setCourseName("Math");
        updatedDetails.setConsultantId(2L);

        Assignment updated = assignmentService.updateAssignment(1L, updatedDetails);
        assertEquals("Math", updated.getCourseName());
        verify(assignmentRepository).save(existing);
    }

    @Test
    void testDeleteAssignment() {
        when(assignmentRepository.existsById(1L)).thenReturn(true);
        assignmentService.deleteAssignment(1L);
        verify(assignmentRepository).deleteById(1L);
    }

    @Test
    void testDeleteAssignment_NotFound() {
        when(assignmentRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> assignmentService.deleteAssignment(1L));
    }
}*/
