package com.zo.webapi.controller;

import com.zo.webapi.model.Session;
import com.zo.webapi.service.SessionService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    @Test
    void testGetAllSessions() throws Exception {
        //Arrange
        List<Session> sessions = new ArrayList<>();
        Session session = new Session(LocalDateTime.of(2025, 11, 3, 12, 0),
                LocalDateTime.of(2025, 11, 3, 16, 0));
        session.setId(1L);
        sessions.add(session);

        when(sessionService.getAllSessions()).thenReturn(sessions);

        // Act and assert
        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetSessionById_Success () throws Exception {
        Session session = new Session(LocalDateTime.of(2026, 10, 5, 12, 0),
                LocalDateTime.of(2026, 10, 5, 16, 0));
        session.setId(1L);
        when(sessionService.getSessionById(1L)).thenReturn(Optional.of(session));
        mockMvc.perform(get("/api/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testGetSessionById_NotFound () throws Exception {
        when(sessionService.getSessionById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sessions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateSession_Success () throws Exception {
        Session session = new Session(LocalDateTime.of(2025, 11, 10, 23, 0),
                                        LocalDateTime.of(2025, 11, 10, 16, 0));

        session.setId(1L);

        when(sessionService.updateSession(eq(1L), any(Session.class))).thenReturn(session);

        mockMvc.perform(put("/api/sessions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"timeStart\":\"2025-11-10 23:00\",\"timeEnd\":\"2025-11-10 16:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testUpdateSession_NotFound () throws Exception {
        when(sessionService.updateSession(eq(999L), any(Session.class))).thenThrow(new RuntimeException());
        mockMvc.perform(put("/api/sessions/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"timeStart\":\"2025-11-03 12:00\",\"timeEnd\":\"2025-11-03 16:00\"}"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testDeleteSession_Success () throws Exception {
        when(sessionService.deleteSessionById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/sessions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSession_NotFound () throws Exception {
        when(sessionService.deleteSessionById(999L)).thenReturn(false);
        mockMvc.perform(delete("/api/sessions/999"))
                .andExpect(status().isNotFound());
    }



}
