/*
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
        // Arrange test data
        List<ClassGroup> classes = new ArrayList<>();
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);
        classes.add(classGroup);

        // Mock service call
        when(classGroupService.getAllClass()).thenReturn(classes);

        // Act and assert - Simulate GET
        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetClassById_Success() throws Exception {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);

        // Mock service call
        when(classGroupService.getClassById(1L)).thenReturn(Optional.of(classGroup));

        // Simulate getting class
        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testGetClassById_NotFound() throws Exception {
        when(classGroupService.getClassById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/classes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetClassesByCustomer() throws Exception {
        // Arrange test data
        List<ClassGroup> classes = new ArrayList<>();
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);
        classes.add(classGroup);

        // Mock service call
        when(classGroupService.getClassByCustomer(5L)).thenReturn(classes);

        // Simulate getting class by customer id
        mockMvc.perform(get("/api/classes/customer/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testCreateClass() throws Exception {
        // Arrange test data
        ClassGroup savedClass = new ClassGroup();
        savedClass.setId(1L);

        //Mock service call
        when(classGroupService.createClass(any(ClassGroup.class))).thenReturn(savedClass);

        // Act and assert
        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteClass() throws Exception {
        // Mock service call - successful deletion
        doNothing().when(classGroupService).deleteClass(1L);

        // Act and assert - Simulate DELETE
        mockMvc.perform(delete("/api/classes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Class deleted successfully"));
    }


}
*/
