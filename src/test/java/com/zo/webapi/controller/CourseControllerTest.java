package com.zo.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.model.Course;
import com.zo.webapi.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private Course sampleCourse;

    @BeforeEach
    void setup() {
        sampleCourse = new Course();
        sampleCourse.setId(1L);
        sampleCourse.setName("Course 1");
        sampleCourse.setDateStart(LocalDate.now());
        sampleCourse.setDateEnd(LocalDate.now().plusDays(5));

    }

    @Test
    void testGetAllCourses() throws Exception {
        when(courseService.getAllCourses()).thenReturn(List.of(sampleCourse));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));


    }

    @Test
    void testGetCourseById_Success() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(sampleCourse);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Course 1"));

    }

    @Test
    void testGetCourseByClassId_Success() throws Exception {
        when(courseService.getCourseByClassGroupId(5L))
                .thenReturn(List.of(sampleCourse));

        mockMvc.perform(get("/api/courses/class/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testCreateCourse_Success() throws Exception {
        when(courseService.createCourse(eq(5L), any(Course.class)))
                .thenReturn(sampleCourse);

        mockMvc.perform(post("/api/courses/class/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateCourse_Success() throws Exception {
        when(courseService.updateCourse(eq(1L), any(Course.class)))
        .thenReturn(sampleCourse);

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Course 1"));
    }


    @Test
    void testDeleteCourse_Success() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void testGetCourseById_NotFound() throws Exception {
        when(courseService.getCourseById(99L))
                .thenThrow(new IllegalArgumentException("Course not found"));

        mockMvc.perform(get("/api/courses/99"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testCreateCourse_BadRequest() throws Exception {
        when(courseService.createCourse(eq(5L), any(Course.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/api/courses/class/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isBadRequest());


    }

    @Test
    void testUpdateCourse_NotFound() throws Exception {
        when(courseService.updateCourse(eq(99L), any(Course.class)))
        .thenThrow(new IllegalArgumentException("Course not found"));

        mockMvc.perform(put("/api/courses/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isNotFound());

    }

    @Test
    void testDeleteCourse_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Course not found"))
                .when(courseService).deleteCourse(99L);

        mockMvc.perform(delete("/api/courses/99"))
                .andExpect(status().isNotFound());
    }
}
