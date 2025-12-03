package com.zo.webapi.controller;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Session;
import com.zo.webapi.service.AssignmentService;
import com.zo.webapi.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private AssignmentService assignmentService;

    @MockBean
    private SessionService sessionService;

    @Test
    void testGetAllAssignments_Success() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setId(1L);
        List<Assignment> assignments = List.of(assignment);

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
    void testCreateAssignment_Success() throws Exception {
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
    void testUpdateAssignment_Success() throws Exception {
        Assignment savedAssignment = new Assignment();
        savedAssignment.setId(1L);

        when(assignmentService.updateAssignment(eq(1L), any(Assignment.class))).thenReturn(savedAssignment);

        mockMvc.perform(put("/api/assignments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }



    @Test
    void testDeleteAssignment_Success() throws Exception {
        // Mock the service
        doNothing().when(assignmentService).deleteAssignment(1L);

        // Call endpoint and check
        mockMvc.perform(delete("/api/assignments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAssignmentByConsultant_Success() throws Exception {
       Assignment assignment = new Assignment();
       assignment.setId(1L);
       List<Assignment> assignments = List.of(assignment);

       when(assignmentService.getAssignmentsByConsultant(5L)).thenReturn(assignments);

       mockMvc.perform(get("/api/assignments/consultant/5"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1));
    }


    @Test
    void testGetSessionByAssignmentId_Success() throws Exception {
        Session session = new Session();
        session.setId(1L);
        List<Session> sessions = List.of(session);

        when(sessionService.getSessionsByAssignmentId(1L)).thenReturn(sessions);

        mockMvc.perform(get("/api/assignments/1/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }


    @Test
    void testAddSession_Success() throws Exception {
        // Test data
        Session session = new Session();
        session.setId(1L);

        // Mock create session to return the created session
        when(sessionService.createSession(eq(1L), any(Session.class))).thenReturn(session);

        // Simulate creating a session
        mockMvc.perform(post("/api/assignments/1/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }



    @Test
     void testGetAssignmentById_NotFound() throws Exception {
        when(assignmentService.getAssignmentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/assignments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateAssignment_NotFound() throws Exception {
        when(assignmentService.updateAssignment(eq(999L),  any(Assignment.class)))
                .thenThrow(new RuntimeException("Assignment not found with id: 999"));

        mockMvc.perform(put("/api/assignments/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":999}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAssignment_NotFound() throws Exception {
        doThrow(new RuntimeException("Assignment not found with id: 999"))
            .when(assignmentService).deleteAssignment(eq(999L));

        mockMvc.perform(delete("/api/assignments/999"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testAddSession_NotFound() throws Exception {
        // Return null to simulate assignment not existing
        when(sessionService.createSession(eq(999L), any(Session.class))).thenReturn(null);

        // Simulate create
        mockMvc.perform(post("/api/assignments/999/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":999}"))
            .andExpect(status().isNotFound());
    }


}
