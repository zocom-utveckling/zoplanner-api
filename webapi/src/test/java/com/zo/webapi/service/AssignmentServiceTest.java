package com.zo.webapi.service;

import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Course;
import com.zo.webapi.repository.AssignmentRepository;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.CourseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
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
        verifyNoMoreInteractions(assignmentRepository);

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
        verifyNoMoreInteractions(assignmentRepository);
    }

    @Test
    void testGetAssignmentByConsultant_Success() {
        when(consultantRepository.existsById(10L)).thenReturn(true);
        when(assignmentRepository.findByConsultant_Id(10L))
                .thenReturn(List.of(new Assignment()));

        List<Assignment> result = assignmentService.getAssignmentsByConsultant(10L);

        assertEquals(1, result.size());
        verify(consultantRepository).existsById(10L);
        verify(assignmentRepository).findByConsultant_Id(10L);
        verifyNoMoreInteractions(consultantRepository, assignmentRepository);
    }

    @Test
    void testCreateAssignment_Success() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));

        Assignment created = new Assignment();
        created.setId(123L);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(created);

        Assignment result = assignmentService.createAssignment(assignmentDTO);

        assertNotNull(result);
        assertEquals(123L, result.getId());

        ArgumentCaptor<Assignment> captor = ArgumentCaptor.forClass(Assignment.class);
        verify(assignmentRepository).save(captor.capture());
        verify(consultantRepository).findById(1L);
        verify(courseRepository).findById(2L);

        Assignment toSave = captor.getValue();
        assertSame(consultant, toSave.getConsultant());
        assertSame(course, toSave.getCourse());
        assertEquals(assignmentDTO.getDateStart(), toSave.getDateStart());
        assertEquals(assignmentDTO.getDateEnd(), toSave.getDateEnd());

        verifyNoMoreInteractions(assignmentRepository, consultantRepository, courseRepository);
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

        assertNotNull(updated);
        assertSame(existingAssignment, updated);
        assertSame(consultant, existingAssignment.getConsultant());
        assertSame(course, existingAssignment.getCourse());
        assertEquals(assignmentDTO.getDateStart(), existingAssignment.getDateStart());
        assertEquals(assignmentDTO.getDateEnd(), existingAssignment.getDateEnd());

        verify(assignmentRepository).findById(1L);
        verify(assignmentRepository).save(existingAssignment);
        verify(consultantRepository).findById(1L);
        verify(courseRepository).findById(2L);
        verifyNoMoreInteractions(assignmentRepository, consultantRepository, courseRepository);

    }

    @Test
    void testDeleteAssignment_Success() {
        when(assignmentRepository.existsById(1L)).thenReturn(true);

        assignmentService.deleteAssignment(1L);

        verify(assignmentRepository).existsById(1L);
        verify(assignmentRepository).deleteById(1L);
        verifyNoMoreInteractions(assignmentRepository);
    }



    @Test
    void testGetAssignmentById_NotFound() {
        when(assignmentRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Assignment> result = assignmentService.getAssignmentById(999L);

        assertTrue(result.isEmpty());

        verify(assignmentRepository).findById(999L);
        verifyNoMoreInteractions(assignmentRepository);
    }


    @Test
    void testGetAssignmentByConsultant_ConsultantNotFound() {
        when(consultantRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                assignmentService.getAssignmentsByConsultant(10L));

        verify(consultantRepository).existsById(10L);
        verifyNoInteractions(assignmentRepository, courseRepository);

    }


    @Test
    void testCreateAssignment_MissingConsultantId() {
        assignmentDTO.setConsultantId(null);

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> assignmentService.createAssignment(assignmentDTO));

        assertTrue(exc.getMessage().contains("Consultant ID"));

        verifyNoInteractions(consultantRepository, courseRepository, assignmentRepository);
    }

    @Test
    void testCreateAssignment_MissingCourseId() {
        assignmentDTO.setCourseId(null);

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> assignmentService.createAssignment(assignmentDTO));

        assertTrue(exc.getMessage().contains("Course ID"));
        verifyNoInteractions(consultantRepository, courseRepository, assignmentRepository);

    }


    @Test
    void testCreateAssignment_MissingStartDate() {
        assignmentDTO.setDateStart(null);

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> assignmentService.createAssignment(assignmentDTO));

        assertTrue(exc.getMessage().toLowerCase().contains("start date"));

        verifyNoInteractions(consultantRepository, courseRepository, assignmentRepository);
    }

    @Test
    void testCreateAssignment_InvalidDates() {
        assignmentDTO.setDateStart(LocalDate.of(2025, 10, 1));
        assignmentDTO.setDateEnd(LocalDate.of(2025, 1, 1));

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> assignmentService.createAssignment(assignmentDTO));

        assertTrue(exc.getMessage().toLowerCase().contains("start date"));

        verifyNoInteractions(consultantRepository, courseRepository, assignmentRepository);
    }

    @Test
    void testUpdateAssignment_NotFound() {
       when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

       assertThrows(ResourceNotFoundException.class, () ->
                assignmentService.updateAssignment(1L, assignmentDTO));

       verify(assignmentRepository, never()).save(any());
       verify(assignmentRepository).findById(1L);
       verifyNoMoreInteractions(assignmentRepository);
       verifyNoInteractions(consultantRepository, courseRepository);
    }

    @Test
    void testUpdateAssignment_InvalidDates() {
        assignmentDTO.setDateStart(LocalDate.of(2025, 10, 1));
        assignmentDTO.setDateEnd(LocalDate.of(2025, 1, 1));

        assertThrows(InvalidDataException.class, () ->
                assignmentService.updateAssignment(1L, assignmentDTO));

        verifyNoInteractions(consultantRepository, courseRepository, assignmentRepository);
    }

    @Test
    void testUpdateAssignment_MissingConsultant() {
        Assignment existingAssignment = new Assignment();
        existingAssignment.setId(1L);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(existingAssignment));
        when(consultantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.updateAssignment(1L, assignmentDTO));

        assertTrue(exc.getMessage().contains("Consultant"));

        verify(assignmentRepository).findById(1L);
        verify(consultantRepository).findById(1L);
        verifyNoInteractions(courseRepository);
        verify(assignmentRepository, never()).save(any());
        verifyNoMoreInteractions(assignmentRepository, consultantRepository);
    }

    @Test
    void testDeleteAssignment_NotFound() {
        when(assignmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                assignmentService.deleteAssignment(1L));

        verify(assignmentRepository).existsById(1L);
        verify(assignmentRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(assignmentRepository);
        verifyNoInteractions(consultantRepository, courseRepository);
    }

    @Test
    void testCreateAssignment_NullDto() {
        assertThrows(InvalidDataException.class, () -> assignmentService.createAssignment(null));
        verifyNoInteractions(assignmentRepository, consultantRepository, courseRepository);
    }

    @Test
    void testCreateAssignment_MissingEndDate() {
        assignmentDTO.setDateEnd(null);

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> assignmentService.createAssignment(assignmentDTO));

        assertTrue(exc.getMessage().toLowerCase().contains("end date"));
        verifyNoInteractions(assignmentRepository, consultantRepository, courseRepository);
    }
}
