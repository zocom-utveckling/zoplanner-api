package com.zo.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.model.LeaveDay;
import com.zo.webapi.service.LeaveDayService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeaveDayController.class)
class LeaveDayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaveDayService leaveDayService;

    @Autowired
    private ObjectMapper objectMapper;

    // =====================
    // POST /consultants/{id}/leave-days
    // =====================

    @Test
    void addLeaveDay_returns200() throws Exception {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setId(1L);
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Semester");
        leaveDay.setConsultantId(1L);

        when(leaveDayService.addLeaveDay(eq(1L), any())).thenReturn(leaveDay);

        mockMvc.perform(post("/consultants/1/leave-days")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaveDay)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Semester"));
    }

    // =====================
    // GET /consultants/{id}/leave-days
    // =====================

    @Test
    void getLeaveDays_returns200WithList() throws Exception {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setId(1L);
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Semester");
        leaveDay.setConsultantId(1L);

        when(leaveDayService.getLeaveDays(1L)).thenReturn(List.of(leaveDay));

        mockMvc.perform(get("/consultants/1/leave-days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reason").value("Semester"));
    }

    // =====================
    // DELETE /consultants/leave-days/{id}
    // =====================

    @Test
    void deleteLeaveDay_returns200() throws Exception {
        doNothing().when(leaveDayService).deleteLeaveDay(1L);

        mockMvc.perform(delete("/consultants/leave-days/1"))
                .andExpect(status().isOk());
    }

    // =====================
    // PUT /consultants/leave-days/{id}
    // =====================

    @Test
    void updateLeaveDay_returns200() throws Exception {
        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().plusDays(10));
        updated.setReason("Sjuk");

        when(leaveDayService.updateLeaveDay(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/consultants/leave-days/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Sjuk"));
    }

    @Test
    void updateLeaveDay_invalidDate_returns400() throws Exception {
        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().minusDays(1));

        when(leaveDayService.updateLeaveDay(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Leave day cannot be in the past"));

        mockMvc.perform(put("/consultants/leave-days/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLeaveDay_notFound_returns500() throws Exception {
        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().plusDays(5));

        when(leaveDayService.updateLeaveDay(eq(1L), any()))
                .thenThrow(new EntityNotFoundException("LeaveDay not found"));

        mockMvc.perform(put("/consultants/leave-days/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is5xxServerError());
    }
}