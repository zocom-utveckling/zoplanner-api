package com.zo.webapi.controller;

import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.service.ClassGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassGroupController.class)
public class ClassGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClassGroupService classGroupService;

    @Test
    void testGetAllClasses() throws Exception {
        ClassGroupResponseDTO responseDTO = new ClassGroupResponseDTO(1L, "Java Class", 1L, "Customer A");

        when(classGroupService.getAllClasses()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Java Class"));

    }

    @Test
    void testGetClassById_Success() throws Exception {
        ClassGroupResponseDTO responseDTO = new ClassGroupResponseDTO(1L, "Java Class", 1L, "Customer A");
        when(classGroupService.getClassById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Class"));

    }

    @Test
    void testGetClassById_NotFound() throws Exception {
        when(classGroupService.getClassById(999L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found with id: 999"));

        mockMvc.perform(get("/api/classes/999"))
                .andExpect(status().isNotFound());
    }



    @Test
    void testCreateClass_Success() throws Exception {
        ClassGroupResponseDTO responseDTO = new ClassGroupResponseDTO(1L, "Java Class", 1L, "Customer A");

        when(classGroupService.createClass(any(CreateClassGroupRequestDTO.class))).thenReturn(responseDTO);

        String jsonRequest = "{\"name\":\"Java Class\",\"customerId\":1}";

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Class"));
    }

    @Test
    void testCreateClass_CustomerNotFound() throws Exception {
        String jsonRequest = "{\"name\":\"Java Class\",\"customerId\":99}";

        when(classGroupService.createClass(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: 99"));

        mockMvc.perform(post("/api/classes")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClass() throws Exception {
        // Mock service call - successful deletion
        doNothing().when(classGroupService).deleteClass(1L);

        // Act and assert - Simulate DELETE
        mockMvc.perform(delete("/api/classes/1"))
                .andExpect(status().isNoContent());
    }


}

