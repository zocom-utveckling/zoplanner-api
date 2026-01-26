package service;

import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Course;
import com.zo.webapi.repository.AssignmentRepository;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.CourseRepository;
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

    @Mock
    private ConsultantRepository consultantRepository;

    @Mock
    private CourseRepository courseRepository;

    private AssignmentDTO assignmentDTO;
    private Consultant consultant;
    private Course course;

    @InjectMocks
    private AssignmentService assignmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        assignmentDTO = new AssignmentDTO();
        assignmentDTO.setConsultantId(1L);
        assignmentDTO.setCourseId(2L);
        assignmentDTO.setDateStart(LocalDate.of(2025, 1, 1));
        assignmentDTO.setDateEnd(LocalDate.of(2025, 1, 5));

        consultant = new Consultant();
        consultant.setId(1L);

        course = new Course();
        course.setId(2L);
    }

    @Test
    void testGetAllAssignments_Success() {
        when(assignmentRepository.findAll()).thenReturn(List.of(new Assignment()));

        List<Assignment> result = assignmentService.getAllAssignments();

        assertEquals(1, result.size());
        verify(assignmentRepository).findAll();

    }

    @Test
    void testGetAssignmentById_Success() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        Optional<Assignment> result = assignmentService.getAssignmentById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L,result.get().getId());
    }

    @Test
    void testGetAssignmentByConsultant_Success() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        when(consultantRepository.existsById(10L)).thenReturn(true);
        when(assignmentRepository.findByConsultant_Id(10L))
                .thenReturn(List.of(assignment));

        List<Assignment> result = assignmentService.getAssignmentsByConsultant(10L);

        assertEquals(1, result.size());
        verify(assignmentRepository).findByConsultant_Id(10L);
    }

    @Test
    void testCreateAssignment_Success() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Assignment created = assignmentService.createAssignment(assignmentDTO);

        assertNotNull(created);
        assertEquals(consultant, created.getConsultant());
        assertEquals(course, created.getCourse());
        verify(assignmentRepository).save(any());
    }

    @Test
    void testUpdateAssignment_Success() {
        Assignment existingAssignment = new Assignment();
        existingAssignment.setId(1L);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(existingAssignment));
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(existingAssignment)).thenReturn(existingAssignment);

        Assignment updated = assignmentService.updateAssignment(1L, assignmentDTO);

        assertEquals(consultant, updated.getConsultant());
        assertEquals(course, updated.getCourse());
        verify(assignmentRepository).save(existingAssignment);

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
    void testGetAssignmentByConsultant_ConsultantNotFound() {
        when(consultantRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                assignmentService.getAssignmentsByConsultant(10L));

    }


    @Test
    void testCreateAssignment_MissingConsultantId() {
        assignmentDTO.setConsultantId(null);

        assertThrows(InvalidDataException.class, () ->
                assignmentService.createAssignment(assignmentDTO));
    }

    @Test
    void testCreateAssignment_MissingCourseId() {
        assignmentDTO.setCourseId(null);

        assertThrows(InvalidDataException.class, () ->
                assignmentService.updateAssignment(1L, assignmentDTO));
    }


    @Test
    void testCreateAssignment_MissingStartDate() {
        assignmentDTO.setDateStart(null);

        assertThrows(InvalidDataException.class, () ->
                assignmentService.createAssignment(assignmentDTO));
    }

    @Test
    void testCreateAssignment_InvalidDates() {
        assignmentDTO.setDateStart(LocalDate.of(2025, 10, 1));
        assignmentDTO.setDateEnd(LocalDate.of(2025, 1, 1));

        assertThrows(InvalidDataException.class, () ->
                assignmentService.createAssignment(assignmentDTO));
    }

    @Test
    void testUpdateAssignment_NotFound() {
       when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

       assertThrows(ResourceNotFoundException.class, () ->
                assignmentService.updateAssignment(1L, assignmentDTO));
    }

    @Test
    void testUpdateAssignment_InvalidDates() {
        assignmentDTO.setDateStart(LocalDate.of(2025, 10, 1));
        assignmentDTO.setDateEnd(LocalDate.of(2025, 1, 1));

        assertThrows(InvalidDataException.class, () ->
                assignmentService.updateAssignment(1L, assignmentDTO));
    }

    @Test
    void testUpdateAssignment_MissingConsultant() {
        Assignment existingAssignment = new Assignment();
        existingAssignment.setId(1L);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(existingAssignment));
        when(consultantRepository.findById(10L)).thenReturn(Optional.of(consultant));

        assertThrows(ResourceNotFoundException.class, () ->
                assignmentService.updateAssignment(1L, assignmentDTO));
    }

    @Test
    void testDeleteAssignment_NotFound() {
        when(assignmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                assignmentService.deleteAssignment(1L));
    }
}
