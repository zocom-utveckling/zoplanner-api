package com.zo.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.ConsultantResponseDTO;
import com.zo.webapi.dto.CustomerDTO;
import com.zo.webapi.dto.ManagerResponseDTO;
import com.zo.webapi.service.ManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerService managerService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testCreateManager_Success() throws Exception {
        ManagerResponseDTO dto = new ManagerResponseDTO(1L, 10L, "John", "MANAGER");

        when(managerService.createManager(10L)).thenReturn(dto);

        mockMvc.perform(post("/api/managers")
                .param("userId", "10"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testGetManagerById_Success() throws Exception {
        ManagerResponseDTO response = new ManagerResponseDTO(1L, 10L, "John", "MANAGER");
        when(managerService.getManagerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/managers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10L));

    }

    @Test
    void testGetAllManagers_Success() throws Exception {
        when( managerService.getAllManagers()).thenReturn(List.of(
                new ManagerResponseDTO(1L, 10L, "John", "MANAGER")
        ));

        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));


    }

    @Test
    void testGetManagerByUserId_Success() throws Exception {
        ManagerResponseDTO dto =  new ManagerResponseDTO(1L, 10L, "John", "MANAGER");

        when(managerService.getManagerByUserId(10L)).thenReturn(dto);

        mockMvc.perform(get("/api/managers/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));

    }


    @Test
    void testGetConsultantsForManager_Success() throws Exception {
        when(managerService.getConsultantsForManager(1L))
                .thenReturn(List.of(new ConsultantResponseDTO(1L, "Bob", "Paris", 1L, 1L)));

        mockMvc.perform(get("/api/managers/1/consultants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

    }

    @Test
    void testGetCustomersForManager_Success() throws Exception {
        when(managerService.getCustomersForManager(1L))
                .thenReturn(List.of(new CustomerDTO(1L, "Bob", "Paris")));

        mockMvc.perform(get("/api/managers/1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testAssignConsultant_Success() throws Exception {
        mockMvc.perform(put("/api/managers/1/consultants/2"))
                .andExpect(status().isOk());

    }

    @Test
    void testAssignCustomer_Success() throws Exception {
        mockMvc.perform(put("/api/managers/1/customers/3"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateManagerUser_Success() throws Exception {
        ManagerResponseDTO dto = new ManagerResponseDTO(1L, 10L, "newuser", "MANAGER");

        when(managerService.updateManagerUser(1L, 10L)).thenReturn(dto);

        mockMvc.perform(put("/api/managers/1/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10L));

    }

    @Test
    void testDeleteManagerUser_Success() throws Exception {
        mockMvc.perform(delete("/api/managers/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void testCreateManager_NotFound() throws Exception {
        when(managerService.createManager(10L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        mockMvc.perform(post("/api/managers")
                .param("userId", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetManagerById_NotFound() throws Exception {
        when(managerService.getManagerById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mockMvc.perform(get("/api/managers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllManagers_NotFound() throws Exception {
        when(managerService.getAllManagers()).thenReturn(List.of());

        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void testGetManagerByUserId_NotFound() throws Exception {
        when(managerService.getManagerByUserId(10L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mockMvc.perform(get("/api/managers/user/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetConsultantsForManager_NotFound() throws Exception {
        when(managerService.getConsultantsForManager(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mockMvc.perform(get("/api/managers/1/consultants"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCustomersForManager_NotFound() throws Exception {
        when(managerService.getCustomersForManager(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mockMvc.perform(get("/api/managers/1/customers"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAssignConsultant_Failure() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"))
                .when(managerService).assignConsultantToManager(1L, 2L);

        mockMvc.perform(put("/api/managers/1/consultants/2"))
                .andExpect(status().isNotFound());


    }


    @Test
    void testAssignCustomer_Failure() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "fail"))
                .when(managerService).assignCustomerToManager(1L, 5L);

        mockMvc.perform(put("/api/managers/1/customers/5"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testUpdateManagerUser_Failure() throws Exception {
        when(managerService.updateManagerUser(1L, 10L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mockMvc.perform(put("/api/managers/1/user/10"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testDeleteManager_NotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"))
                .when(managerService).deleteManager(1L);

        mockMvc.perform(delete("/api/managers/1"))
                .andExpect(status().isNotFound());

    }
}
