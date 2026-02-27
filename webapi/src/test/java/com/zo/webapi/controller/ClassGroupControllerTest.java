package com.zo.webapi.controller;

import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.dto.UpdateClassGroupRequestDTO;
import com.zo.webapi.service.ClassGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassGroupController.class)
public class ClassGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClassGroupService classGroupService;

    @Test
    void testGetAllClasses() throws Exception {
        ClassGroupResponseDTO responseDTO =
                new ClassGroupResponseDTO(1L, "Java Class", 1L, "Customer A");

        when(classGroupService.getAllClasses()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Java Class"))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Customer A"));

        verify(classGroupService).getAllClasses();
        verifyNoMoreInteractions(classGroupService);
    }

    @Test
    void testGetClassById_Success() throws Exception {
        ClassGroupResponseDTO responseDTO =
                new ClassGroupResponseDTO(1L, "Java Class", 1L, "Customer A");

        when(classGroupService.getClassById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/classes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Class"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Customer A"));

        verify(classGroupService).getClassById(1L);
        verifyNoMoreInteractions(classGroupService);
    }

    @Test
    void testGetClassById_NotFound() throws Exception {
        when(classGroupService.getClassById(999L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found with id: 999"));

        mockMvc.perform(get("/api/classes/{id}", 999))
                .andExpect(status().isNotFound());

        verify(classGroupService).getClassById(999L);
        verifyNoMoreInteractions(classGroupService);
    }



    @Test
    void testCreateClass_Success() throws Exception {
        ClassGroupResponseDTO responseDTO =
                new ClassGroupResponseDTO(1L, "Java Class", 1L, "Customer A");

        when(classGroupService.createClass(any(CreateClassGroupRequestDTO.class))).thenReturn(responseDTO);

        CreateClassGroupRequestDTO request = new CreateClassGroupRequestDTO("Java Class", 1L);

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Class"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Customer A"));

        ArgumentCaptor<CreateClassGroupRequestDTO> captor =
            ArgumentCaptor.forClass(CreateClassGroupRequestDTO.class);

        verify(classGroupService).createClass(captor.capture());

        CreateClassGroupRequestDTO sent = captor.getValue();
        assertEquals("Java Class", sent.getName());
        assertEquals(1L, sent.getCustomerId());

        verifyNoMoreInteractions(classGroupService);
    }

    @Test
    void testCreateClass_CustomerNotFound() throws Exception {
        when(classGroupService.createClass(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: 99"));

        CreateClassGroupRequestDTO request = new CreateClassGroupRequestDTO("Java Class", 99L);

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(classGroupService).createClass(any(CreateClassGroupRequestDTO.class));
        verifyNoMoreInteractions(classGroupService);
    }

    @Test
    void testDeleteClass() throws Exception {
        // Mock service call - successful deletion
        doNothing().when(classGroupService).deleteClass(1L);

        // Act and assert - Simulate DELETE
        mockMvc.perform(delete("/api/classes/{id}", 1))
                .andExpect(status().isNoContent());

        verify(classGroupService).deleteClass(1L);
        verifyNoMoreInteractions(classGroupService);
    }

    @Test
    void testGetClassesByCustomerId_Success() throws Exception {
        ClassGroupResponseDTO responseDTO =
                new ClassGroupResponseDTO(1L, "Java Class", 2L, "Customer A");

        when(classGroupService.getClassesByCustomerId(2L)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/classes/customer/{customerId}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Java Class"))
                .andExpect(jsonPath("$[0].customerId").value(2))
                .andExpect(jsonPath("$[0].customerName").value("Customer A"));

        verify(classGroupService).getClassesByCustomerId(2L);
        verifyNoMoreInteractions(classGroupService);
    }

    @Test
    void testUpdateClass_Success() throws Exception {
        ClassGroupResponseDTO responseDTO =
                new ClassGroupResponseDTO(1L, "Updated Name", 1L, "Customer A");

        when(classGroupService.updateClass(eq(1L), any(UpdateClassGroupRequestDTO.class)))
                .thenReturn(responseDTO);

        UpdateClassGroupRequestDTO request = new UpdateClassGroupRequestDTO("Updated Name");

        mockMvc.perform(put("/api/classes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Customer A"));

        ArgumentCaptor<UpdateClassGroupRequestDTO> captor =
                ArgumentCaptor.forClass(UpdateClassGroupRequestDTO.class);

        verify(classGroupService).updateClass(eq(1L), captor.capture());

        UpdateClassGroupRequestDTO sent = captor.getValue();
        assertEquals("Updated Name", sent.getName());

        verifyNoMoreInteractions(classGroupService);
    }

    @Test
    void testDeleteClass_NotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found with id: 999"))
                .when(classGroupService).deleteClass(999L);

        mockMvc.perform(delete("/api/classes/{id}", 999))
                .andExpect(status().isNotFound());

        verify(classGroupService).deleteClass(999L);
        verifyNoMoreInteractions(classGroupService);
    }

}