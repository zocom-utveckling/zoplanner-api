package com.zo.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.ConsultantResponseDTO;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.service.ConsultantService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultantController.class)
public class ConsultantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultantService consultantService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testGetAllConsultants_Success() throws Exception {
        ConsultantResponseDTO dto = new ConsultantResponseDTO(1L, "fredrik", "Paris", 10L, 1L);

        when(consultantService.getAllConsultants()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/consultants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }


    @Test
    void testGetConsultantById_Success() throws Exception {
        ConsultantResponseDTO dto = new ConsultantResponseDTO(1L, "fredrik", "Paris", 10L, 1L);

        when(consultantService.getConsultantById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/consultants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetConsultantByUserId_Success() throws Exception {
        ConsultantResponseDTO dto = new ConsultantResponseDTO(1L, "fredrik", "Paris", 10L, 1L);

        when(consultantService.getConsultantByUserId(10L)).thenReturn(dto);

        mockMvc.perform(get("/api/consultants/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10));
    }


    @Test
    void testGetConsultantByManagerId_Success() throws Exception {
        ConsultantResponseDTO dto = new ConsultantResponseDTO(1L, "fredrik", "Paris", 5L, 5L);

        when(consultantService.getConsultantsByManagerId(5L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/consultants/manager/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].managerId").value(5));
    }


    @Test
    void testCreateConsultant_Success() throws Exception {
        ConsultantDTO request = new ConsultantDTO(null, 10L, 1L);
        ConsultantResponseDTO created = new ConsultantResponseDTO(1L, "fredrik", "Bjärred", 10L, 1L);

        when(consultantService.createConsultant(any(ConsultantDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/consultants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    void testUpdateConsultant_Success() throws Exception {
        ConsultantDTO request = new ConsultantDTO(null, 10L, null);
        ConsultantResponseDTO updated = new ConsultantResponseDTO(1L, "fredrik", "Bjärred", 10L, 1L);

        when(consultantService.updateConsultant(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/consultants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managerId").value(1L));
    }


    @Test
    void testDeleteConsultant_Success() throws Exception {
        mockMvc.perform(delete("/api/consultants/1"))
                .andExpect(status().isNoContent());

        verify(consultantService, times(1)).deleteConsultant(1L);
    }


    @Test
    void testGetConsultantById_NotFound() throws Exception {
        when(consultantService.getConsultantById(999L))
                .thenThrow(new ResourceNotFoundException("Consultant", "id", 999L));

        mockMvc.perform(get("/api/consultants/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetConsultantByManagerId_NotFound() throws Exception {
        when(consultantService.getConsultantsByManagerId(999L))
                .thenThrow(new ResourceNotFoundException("Manager", "id", 999L));

        mockMvc.perform(get("/api/consultants/manager/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetConsultantByUserId_NotFound() throws Exception {
        when(consultantService.getConsultantByUserId(999L))
                .thenThrow(new ResourceNotFoundException("Consultant", "userId", 999L));

        mockMvc.perform(get("/api/consultants/user/999"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testCreateConsultant_Failure() throws Exception {
        ConsultantDTO request = new ConsultantDTO(null, 10L, null);

        when(consultantService.createConsultant(any()))
                .thenThrow(new InvalidDataException("User already is consultant"));

        mockMvc.perform(post("/api/consultants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateConsultant_NotFound() throws Exception {
        ConsultantDTO request = new ConsultantDTO(null, 10L, null);

        when(consultantService.updateConsultant(eq(999L), any()))
                .thenThrow(new ResourceNotFoundException("Consultant", "id", 999L));

        mockMvc.perform(put("/api/consultants/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    @Test
    void testDeleteConsultant_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Consultant", "id", 999L))
            .when(consultantService).deleteConsultant(999L);

        mockMvc.perform(delete("/api/consultants/999"))
                .andExpect(status().isNotFound());
    }
}

