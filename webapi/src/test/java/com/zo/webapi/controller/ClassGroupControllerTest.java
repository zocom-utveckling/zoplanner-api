package com.zo.webapi.controller;

import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.service.ClassGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
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
        List<ClassGroup> classes = new ArrayList<>();
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);
        classes.add(classGroup);

        when(classGroupService.getAllClass()).thenReturn(classes);

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetClassById_Success() throws Exception {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);

        when(classGroupService.getClassById(1L)).thenReturn(Optional.of(classGroup));

        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testCreateClass() throws Exception {
        ClassGroup savedClass = new ClassGroup();
        savedClass.setId(1L);

        when(classGroupService.createClass(any(ClassGroup.class))).thenReturn(savedClass);

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteClass() throws Exception {
        doNothing().when(classGroupService).deleteClass(1L);

        mockMvc.perform(delete("/api/classes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Class deleted successfully"));
    }

    void testGetClassById_NotFound() throws Exception {
        when(classGroupService.getClassById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/classes/999"))
                .andExpect(status().isNotFound());
    }

}
