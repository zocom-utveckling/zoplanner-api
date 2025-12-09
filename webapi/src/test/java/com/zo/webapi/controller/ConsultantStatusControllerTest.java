package com.zo.webapi.controller;

import org.aspectj.lang.annotation.Before;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.enums.ConsultantStatusType;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.service.ConsultantStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.List;

@WebMvcTest(ConsultantStatusController.class)
public class ConsultantStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultantStatusService consultantStatusService;

    @Autowired
    private ObjectMapper objectMapper;

    private Consultant consultant;
    private ConsultantStatus status;
    private ConsultantStatusDTO dto;


    @BeforeEach
    public void setup() {
        consultant = new Consultant();
        consultant.setId(1L);

        dto = new ConsultantStatusDTO();
        dto.setConsultantId(1L);
        dto.setStatus(ConsultantStatusType.AVAILABLE);
        dto.setDateStart(LocalDate.now());
        dto.setDateEnd(LocalDate.now().plusDays(5));
        dto.setComment("Test comment");

        status = new ConsultantStatus(
                10L,
                consultant,
                dto.getStatus(),
                dto.getDateStart(),
                dto.getDateEnd(),
                dto.getComment()
        );


    }

    @Test
    void testCreateStatus_Success() throws Exception {
        when(consultantStatusService.createStatus(any())).thenReturn(status);

        mockMvc.perform(post("/api/consultant-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void testCreateStatus_NotFound() throws Exception {
        when(consultantStatusService.createStatus(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/api/consultant-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateStatus_Success() throws Exception {
        when(consultantStatusService.updateStatus(eq(10L), any())).thenReturn(status);

        mockMvc.perform(put("/api/consultant-status/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

    }

    @Test
    void testUpdateStatus_NotFound() throws Exception {
        when(consultantStatusService.updateStatus(eq(10L), any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/consultant-status/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStatuses_Success() throws Exception {
        when(consultantStatusService.getStatusesByConsultant(1L)).thenReturn(List.of(status));

        mockMvc.perform(get("/api/consultant-status/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));

    }

    @Test
    void testGetStatuses_NotFound() throws Exception {
        when(consultantStatusService.getStatusesByConsultant(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/consultant-status/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStatusById_Success() throws Exception {
        when(consultantStatusService.getStatusById(10L)).thenReturn(status);

        mockMvc.perform(get("/api/consultant-status/status/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

    }

    @Test
    void testGetStatusById_NotFound() throws Exception {
        when(consultantStatusService.getStatusById(10L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/consultant-status/status/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllStatuses_Success() throws Exception {
        when(consultantStatusService.getAllStatuses()).thenReturn(List.of(status));

        mockMvc.perform(get("/api/consultant-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));
    }

    @Test
    void testGetAllStatuses_EmptySuccess() throws Exception {
        when(consultantStatusService.getAllStatuses()).thenReturn(List.of());

        mockMvc.perform(get("/api/consultant-status"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void testDeleteStatusById_Success() throws Exception {
        mockMvc.perform(delete("/api/consultant-status/10"))
                .andExpect(status().isOk());

        verify(consultantStatusService, times(1)).deleteStatus(10L);
    }

    @Test
    void testDeleteStatusById_NotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(consultantStatusService).deleteStatus(10L);

        mockMvc.perform(delete("/api/consultant-status/10"))
                .andExpect(status().isNotFound());
    }
}
