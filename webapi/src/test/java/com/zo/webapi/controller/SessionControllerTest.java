package com.zo.webapi.controller;

import com.zo.webapi.model.Session;
import com.zo.webapi.model.SessionLocation;
import com.zo.webapi.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Test
    void testGetAllSessions() throws Exception {

        Session session = new Session(
                "test title",
                LocalDateTime.of(2025, 11, 3, 12, 0),
                LocalDateTime.of(2025, 11, 3, 16, 0)
        );

        session.setId(1L);
        session.setComment("Test comment");
        session.setLocation(SessionLocation.ONSITE);

        when(sessionService.getAllSessions()).thenReturn(List.of(session));

        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("test title"))
                .andExpect(jsonPath("$[0].comment").value("Test comment"))
                .andExpect(jsonPath("$[0].location").value("ONSITE"));
    }

    @Test
    void testGetSessionById_Success() throws Exception {

        Session session = new Session(
                "test title",
                LocalDateTime.of(2026, 10, 5, 12, 0),
                LocalDateTime.of(2026, 10, 5, 16, 0)
        );

        session.setId(1L);
        session.setComment("Test comment");
        session.setLocation(SessionLocation.REMOTE);

        when(sessionService.getSessionById(1L)).thenReturn(Optional.of(session));

        mockMvc.perform(get("/api/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.comment").value("Test comment"))
                .andExpect(jsonPath("$.location").value("REMOTE"));
    }

    @Test
    void testGetSessionById_NotFound() throws Exception {

        when(sessionService.getSessionById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sessions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateSession_Success() throws Exception {

        Session session = new Session(
                "test title",
                LocalDateTime.of(2025, 11, 10, 23, 0),
                LocalDateTime.of(2025, 11, 10, 16, 0)
        );

        session.setId(1L);
        session.setTitle("Updated title");
        session.setComment("Updated comment");
        session.setLocation(SessionLocation.HYBRID);

        when(sessionService.updateSession(eq(1L), any(Session.class))).thenReturn(session);

        mockMvc.perform(put("/api/sessions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timeStart\":\"2025-11-10T23:00:00\",\"timeEnd\":\"2025-11-10T16:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.comment").value("Updated comment"))
                .andExpect(jsonPath("$.location").value("HYBRID"));
    }

    @Test
    void testUpdateSession_NotFound() throws Exception {

        when(sessionService.updateSession(eq(999L), any(Session.class)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/sessions/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timeStart\":\"2025-11-03T12:00:00\",\"timeEnd\":\"2025-11-03T16:00:00\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSession_Success() throws Exception {

        when(sessionService.deleteSessionById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/sessions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSession_NotFound() throws Exception {

        when(sessionService.deleteSessionById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/sessions/999"))
                .andExpect(status().isNotFound());
    }
}