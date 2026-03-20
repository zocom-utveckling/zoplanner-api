package com.zo.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Assignment;

import com.zo.webapi.service.AssignmentService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignmentService assignmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllAssignments_Success() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setId(1L);
        List<Assignment> assignments = List.of(assignment);

        when(assignmentService.getAllAssignments()).thenReturn(assignments);

        mockMvc.perform(get("/api/assignments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(assignmentService).getAllAssignments();
        verifyNoMoreInteractions(assignmentService);
    }


    @Test
    void testGetAssignmentById_Success() throws Exception {
        // Create test data
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        // Mock service
        when(assignmentService.getAssignmentById(1L)).thenReturn(Optional.of(assignment));

        // Call the endpoint and check response
        mockMvc.perform(get("/api/assignments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(assignmentService).getAssignmentById(1L);
        verifyNoMoreInteractions(assignmentService);
    }


    @Test
    void testCreateAssignment_Success() throws Exception {
        Assignment created = new Assignment();
        created.setId(1L);

        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setConsultantId(1L);
        assignmentDTO.setCourseId(2L);
        assignmentDTO.setPublished(true);
        assignmentDTO.setManagerId(3L);

        when(assignmentService.createAssignment(any(AssignmentDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<AssignmentDTO> captor = ArgumentCaptor.forClass(AssignmentDTO.class);
        verify(assignmentService).createAssignment(captor.capture());

        AssignmentDTO sent = captor.getValue();
        assertEquals(1L, sent.getConsultantId());
        assertEquals(2L, sent.getCourseId());
        assertTrue(sent.isPublished());
        assertEquals(3L, sent.getManagerId());

        verifyNoMoreInteractions(assignmentService);
    }


    @Test
    void testUpdateAssignment_Success() throws Exception {
        Assignment updated = new Assignment();
        updated.setId(1L);

        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setConsultantId(1L);
        assignmentDTO.setCourseId(2L);
        assignmentDTO.setPublished(false);
        assignmentDTO.setManagerId(3L);

        when(assignmentService.updateAssignment(eq(1L), any(AssignmentDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/assignments/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignmentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<AssignmentDTO> captor = ArgumentCaptor.forClass(AssignmentDTO.class);
        verify(assignmentService).updateAssignment(eq(1L), captor.capture());

        AssignmentDTO sent = captor.getValue();
        assertEquals(1L, sent.getConsultantId());
        assertEquals(2L, sent.getCourseId());
        assertFalse(sent.isPublished());
        assertEquals(3L, sent.getManagerId());

        verifyNoMoreInteractions(assignmentService);
    }



    @Test
    void testDeleteAssignment_Success() throws Exception {
        mockMvc.perform(delete("/api/assignments/{id}", 1))
                .andExpect(status().isNoContent());

        verify(assignmentService).deleteAssignment(1L);
        verifyNoMoreInteractions(assignmentService);
    }

    @Test
    void testGetAssignmentByConsultant_Success() throws Exception {
       Assignment assignment = new Assignment();
       assignment.setId(1L);

       when(assignmentService.getAssignmentsByConsultant(5L)).thenReturn(List.of(assignment));

       mockMvc.perform(get("/api/assignments/consultant/{consultantId}", 5))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(1))
               .andExpect(jsonPath("$[0].id").value(1));

       verify(assignmentService).getAssignmentsByConsultant(5L);
       verifyNoMoreInteractions(assignmentService);
    }

    @Test
    void testGetAssignmentByManager_Success() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        when(assignmentService.getAssignmentsByManager(5L)).thenReturn(List.of(assignment));

        mockMvc.perform(get("/api/assignments/manager/{managerId}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(assignmentService).getAssignmentsByManager(5L);
        verifyNoMoreInteractions(assignmentService);
    }

    @Test
     void testGetAssignmentById_NotFound() throws Exception {
        when(assignmentService.getAssignmentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/assignments/{id}", 999))
                .andExpect(status().isNotFound());

        verify(assignmentService).getAssignmentById(999L);
        verifyNoMoreInteractions(assignmentService);
    }

    @Test
    void testUpdateAssignment_NotFound() throws Exception {
        when(assignmentService.updateAssignment(eq(999L), any(AssignmentDTO.class)))
                .thenThrow(new ResourceNotFoundException("Assignment", "id", 999L));

        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setConsultantId(1L);
        assignmentDTO.setCourseId(3L);

        mockMvc.perform(put("/api/assignments/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignmentDTO)))
                .andExpect(status().isNotFound());

        verify(assignmentService).updateAssignment(eq(999L), any(AssignmentDTO.class));
        verifyNoMoreInteractions(assignmentService);
    }

    @Test
    void testDeleteAssignment_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Assignment", "id", 999L))
                .when(assignmentService).deleteAssignment(999L);

        mockMvc.perform(delete("/api/assignments/{id}", 999))
                .andExpect(status().isNotFound());

        verify(assignmentService).deleteAssignment(999L);
        verifyNoMoreInteractions(assignmentService);
    }



}
