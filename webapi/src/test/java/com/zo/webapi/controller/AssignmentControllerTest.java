package com.zo.webapi.controller;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.service.AssignmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssignmentService assignmentService;

    @Test
    void testGetAllAssignments() throws Exception {
        // Create test data
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment = new Assignment();
        assignment.setId(1L);
        assignments.add(assignment);

        // Mock service
        when(assignmentService.getAllAssignments()).thenReturn(assignments);

        mockMvc.perform(get("/api/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

    }

    @Test
    void testGetAssignmentById_Success() throws Exception {
        // Create test data
        Assignment assignment = new Assignment();
        assignment.setId(1L);

        // Mock service
        when(assignmentService.getAssignmentById(1L)).thenReturn(Optional.of(assignment));

        // Call the endpoint and check response
        mockMvc.perform(get("/api/assignments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetAssignmentById_NotFound() throws Exception {
        // Mock service to return empty
        when(assignmentService.getAssignmentById(999L)).thenReturn(Optional.empty());

        // Call the endpoint and check response
        mockMvc.perform(get("/api/assignments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAssignment() throws Exception {
        // Create test data
        Assignment savedAssignment = new Assignment();
        savedAssignment.setId(1L);

        // Mock service
        when(assignmentService.createAssignment(any(Assignment.class))).thenReturn(savedAssignment);

        // Call endpoint and check response
        mockMvc.perform(post("/api/assignments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":null}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testDeleteAssignment() throws Exception {
        // Mock the service
        doNothing().when(assignmentService).deleteAssignment(1L);

        // Call endpoint and check
        mockMvc.perform(delete("/api/assignments/1"))
                .andExpect(status().isNoContent());
    }
}
