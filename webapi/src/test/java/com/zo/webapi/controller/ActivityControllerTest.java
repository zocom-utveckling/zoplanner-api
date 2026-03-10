package com.zo.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.dto.ActivityCreateRequestDTO;
import com.zo.webapi.dto.ActivityResponseDTO;
import com.zo.webapi.dto.ActivityUpdateRequestDTO;
import com.zo.webapi.enums.ActivityType;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.mockito.ArgumentCaptor;


import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateActivity_Success() throws Exception {
        ActivityCreateRequestDTO req = new ActivityCreateRequestDTO(
                "Möte med kursledare",
                ActivityType.MEETING,
                7L,
                "2026-02-10",
                "09:00",
                "10:00",
                "Att veta inför praktik"
        );

        ActivityResponseDTO resp = new ActivityResponseDTO(
                1L,
                req.getTitle(),
                req.getType(),
                7L,
                req.getDate(),
                req.getStartTime(),
                req.getEndTime(),
                req.getDescription(),
                "2026-02-01T10:15:30+01:00"
        );

        when(activityService.createActivity(any(ActivityCreateRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Möte med kursledare"))
                .andExpect(jsonPath("$.type").value("meeting"))
                .andExpect(jsonPath("$.consultantId").value(7))
                .andExpect(jsonPath("$.date").value("2026-02-10"))
                .andExpect(jsonPath("$.startTime").value("09:00"))
                .andExpect(jsonPath("$.endTime").value("10:00"))
                .andExpect(jsonPath("$.description").value("Att veta inför praktik"))
                .andExpect(jsonPath("$.createdAt").value("2026-02-01T10:15:30+01:00"));

        ArgumentCaptor<ActivityCreateRequestDTO> captor = ArgumentCaptor.forClass(ActivityCreateRequestDTO.class);
        verify(activityService).createActivity(captor.capture());

        ActivityCreateRequestDTO sent = captor.getValue();
        assertEquals("Möte med kursledare", sent.getTitle());
        assertEquals(ActivityType.MEETING, sent.getType());
        assertEquals(7L, sent.getConsultantId());
        assertEquals("2026-02-10", sent.getDate());
        assertEquals("09:00", sent.getStartTime());
        assertEquals("10:00", sent.getEndTime());
        assertEquals("Att veta inför praktik", sent.getDescription());

        verifyNoMoreInteractions(activityService);

    }

    @Test
    void testGetAllActivities_NoParams_Success() throws Exception {
        ActivityResponseDTO a1 = new ActivityResponseDTO(
                1L, "A", ActivityType.LESSON, 7L, "2026-02-10",
                "09:00", "10:00", null, "2026-02-01T10:15:30+01:00"
        );
        when(activityService.getAllActivities(null, null,null)).thenReturn(List.of(a1));

        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("lesson"));

        verify(activityService).getAllActivities(null, null, null);
        verifyNoMoreInteractions(activityService);
    }

    @Test
    void testGetAllActivities_WithDates_Success() throws Exception {
        when(activityService.getAllActivities(null, "2026-02-10", "2026-02-20"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/activities")
                .param("from", "2026-02-10")
                .param("to", "2026-02-20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(activityService).getAllActivities(null, "2026-02-10", "2026-02-20");
        verifyNoMoreInteractions(activityService);
    }

    @Test
    void testGetAllActivities_WithConsultantId_Success() throws Exception {
        when(activityService.getAllActivities(7L, null, null)).thenReturn(List.of());

        mockMvc.perform(get("/api/activities")
                        .param("consultantId", "7"))
                .andExpect(status().isOk());

        verify(activityService).getAllActivities(7L, null, null);
    }

    @Test
    void testUpdateActivity_Success() throws Exception {
        ActivityUpdateRequestDTO req = new ActivityUpdateRequestDTO();
        req.setTitle("Ny title");
        req.setType(ActivityType.REVIEW);
        req.setConsultantId(8L);
        req.setDate("2026-02-15");
        req.setStartTime("10:00");
        req.setEndTime("11:00");
        req.setDescription("Ny beskrivning");

        ActivityResponseDTO resp = new ActivityResponseDTO(
                99L, "Ny title", ActivityType.REVIEW, 8L, "2026-02-15",
                "10:00", "11:00", "Ny beskrivning","2026-02-01T10:15:30+01:00"
        );

        when(activityService.updateActivity(eq(99L), any(ActivityUpdateRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(patch("/api/activities/{id}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.title").value("Ny title"))
                .andExpect(jsonPath("$.type").value("review"));

        ArgumentCaptor<ActivityUpdateRequestDTO> captor = ArgumentCaptor.forClass(ActivityUpdateRequestDTO.class);
        verify(activityService).updateActivity(eq(99L), captor.capture());

        ActivityUpdateRequestDTO sent = captor.getValue();
        assertEquals("Ny title", sent.getTitle());
        assertEquals(ActivityType.REVIEW, sent.getType());
        assertEquals(8L, sent.getConsultantId());
        assertEquals("2026-02-15", sent.getDate());
        assertEquals("10:00", sent.getStartTime());
        assertEquals("11:00", sent.getEndTime());
        assertEquals("Ny beskrivning", sent.getDescription());

        verifyNoMoreInteractions(activityService);
    }

    @Test
    void testDeleteActivity_Success() throws Exception {
        mockMvc.perform(delete("/api/activities/{id}", 5))
                .andExpect(status().isNoContent());

        verify(activityService).deleteActivity(5L);
        verifyNoMoreInteractions(activityService);
    }

    @Test
    void testCreateActivity_ValidationError_BadRequest() throws Exception {
        // blank title och fel format på date & time
        ActivityCreateRequestDTO badReq = new ActivityCreateRequestDTO(
                "  ",
                ActivityType.MEETING,
                7L,
                "2026/02/10",
                "9:00",
                "10:0",
                null
        );

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badReq)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(activityService);
    }

    @Test
    void testUpdateActivity_NotFound() throws Exception {
        ActivityUpdateRequestDTO req = new ActivityUpdateRequestDTO();
        req.setTitle("Uppdaterad title");

        when(activityService.updateActivity(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Activity", "id", 99L));

        mockMvc.perform(patch("/api/activities/{id}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());

        verify(activityService).updateActivity(eq(99L), any());
    }

    @Test
    void testUpdateActivity_BlankTitle_ReturnsBadRequest() throws Exception{
        ActivityUpdateRequestDTO req = new ActivityUpdateRequestDTO();
        req.setTitle("   ");

        mockMvc.perform(patch("/api/activities/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(activityService);
    }

    @Test
    void testGetAllActivities_OnlyOneDateProvided_ReturnsBadRequest() throws Exception {

        when(activityService.getAllActivities(null, "2026-02-01", null))
                .thenThrow(new InvalidDataException("Both 'from' and 'to' must be provided together"));

        mockMvc.perform(get("/api/activities")
                .param("from", "2026-02-01"))
                .andExpect(status().isBadRequest());

        verify(activityService).getAllActivities(null, "2026-02-01", null);
    }

    @Test
    void testDeleteActivity_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Activity", "id", 99L))
                .when(activityService).deleteActivity(99L);

        mockMvc.perform(delete("/api/activities/{id}", 99))
                .andExpect(status().isNotFound());

        verify(activityService).deleteActivity(99L);
    }

}