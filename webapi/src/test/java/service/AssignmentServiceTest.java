package service;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.repository.AssignmentRepository;
import com.zo.webapi.service.AssignmentService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.support.AbstractInterruptibleBatchPreparedStatementSetter;

import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAssignments_Success() {
        Assignment assignment1 = new Assignment();
        Assignment assignment2 = new Assignment();
        when(assignmentRepository.findAll()).thenReturn(List.of(assignment1, assignment2));

        List<Assignment> result = assignmentService.getAllAssignments();

        assertEquals(2, result.size());
        verify(assignmentRepository, times(1)).findAll();

    }

    @Test
    void testGetAssignmentById_Success() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        Optional<Assignment> result = assignmentService.getAssignmentById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L,result.get().getId());
        verify(assignmentRepository).findById(1L);
    }

    @Test
    void testGetAssignmentByConsultant_Success() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        when(assignmentRepository.findByConsultantId(5L)).thenReturn(List.of(assignment));

        List<Assignment> result = assignmentService.getAssignmentsByConsultant(5L);

        assertEquals(1, result.size());
        assertEquals(1L,result.get(0).getId());
        verify(assignmentRepository).findByConsultantId(5L);
    }

    @Test
    void testCreateAssignment_Success() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        when(assignmentRepository.save(assignment)).thenReturn(assignment);

        Assignment saved = assignmentService.createAssignment(assignment);

        assertNotNull(saved);
        assertEquals(1L,saved.getId());
        verify(assignmentRepository).save(assignment);
    }

    @Test
    void testUpdateAssignment_Success() {
        Assignment existing = new Assignment();
        existing.setId(1L);

        Assignment updatedAssignment = new Assignment();
        updatedAssignment.setConsultantId(2L);
        updatedAssignment.setDateStart(LocalDate.now());
        updatedAssignment.setDateEnd(LocalDate.now().plusDays(5));

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(assignmentRepository.save(existing)).thenReturn(existing);

        Assignment updated = assignmentService.updateAssignment(1L, updatedAssignment);

        assertEquals(2L, updated.getConsultantId());
        verify(assignmentRepository).save(existing);
    }

    @Test
    void testDeleteAssignment_Success() {
        when(assignmentRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> assignmentService.deleteAssignment(1L));

        verify(assignmentRepository).deleteById(1L);
    }

    @Test
    void testGetAssignmentById_NotFound() {
        when(assignmentRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Assignment> result = assignmentService.getAssignmentById(999L);

        assertTrue(result.isEmpty());
    }


    @Test
    void testUpdateAssignment_NotFound() {
        when(assignmentRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> assignmentService.updateAssignment(999L, new Assignment()));

        assertEquals("Assignment not found with id: 999", exception.getMessage());
    }



    @Test
    void testDeleteAssignment_NotFound() {
        when(assignmentRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> assignmentService.deleteAssignment(999L));

        assertEquals("Assignment not found with id: 999", exception.getMessage());
    }
}
