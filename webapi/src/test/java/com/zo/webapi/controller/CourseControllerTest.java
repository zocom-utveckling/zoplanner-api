package com.zo.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zo.webapi.model.Course;
import com.zo.webapi.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
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
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Course 1"));

        verify(courseService).getAllCourses();
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testGetCourseById_Success() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(sampleCourse);

        mockMvc.perform(get("/api/courses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Course 1"));

        verify(courseService).getCourseById(1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testGetCourseByClassId_Success() throws Exception {
        when(courseService.getCourseByClassGroupId(5L))
                .thenReturn(List.of(sampleCourse));

        mockMvc.perform(get("/api/courses/class/{classId}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(courseService).getCourseByClassGroupId(5L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testCreateCourse_Success() throws Exception {
        when(courseService.createCourse(eq(5L), any(Course.class)))
                .thenReturn(sampleCourse);

        mockMvc.perform(post("/api/courses/class/{classId}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Course 1"));

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).createCourse(eq(5L), captor.capture());

        Course sent = captor.getValue();
        assertEquals("Course 1", sent.getName());
        assertNotNull(sent.getDateStart());
        assertNotNull(sent.getDateEnd());

        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testUpdateCourse_Success() throws Exception {
        when(courseService.updateCourse(eq(1L), any(Course.class)))
        .thenReturn(sampleCourse);

        mockMvc.perform(put("/api/courses/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Course 1"));

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).updateCourse(eq(1L), captor.capture());

        Course sent = captor.getValue();
        assertEquals("Course 1", sent.getName());

        verifyNoMoreInteractions(courseService);
    }


    @Test
    void testDeleteCourse_Success() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/{id}", 1))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourse(1L);
        verifyNoMoreInteractions(courseService);
    }


    @Test
    void testGetCourseById_NotFound() throws Exception {
        when(courseService.getCourseById(99L))
                .thenThrow(new IllegalArgumentException("Course not found"));

        mockMvc.perform(get("/api/courses/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Course not found")));

        verify(courseService).getCourseById(99L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testCreateCourse_BadRequest() throws Exception {
        when(courseService.createCourse(eq(5L), any(Course.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/api/courses/class/{classId}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error creating course:")))
                .andExpect(content().string(containsString("Invalid data")));

        verify(courseService).createCourse(eq(5L), any(Course.class));
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testUpdateCourse_NotFound() throws Exception {
        when(courseService.updateCourse(eq(99L), any(Course.class)))
        .thenThrow(new IllegalArgumentException("Course not found"));

        mockMvc.perform(put("/api/courses/{id}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Course not found")));

        verify(courseService).updateCourse(eq(99L), any(Course.class));
        verifyNoMoreInteractions(courseService);

    }

    @Test
    void testDeleteCourse_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Course not found"))
                .when(courseService).deleteCourse(99L);

        mockMvc.perform(delete("/api/courses/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Course not found")));

        verify(courseService).deleteCourse(99L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testCreateCourse_InternalServerError() throws Exception {
        when(courseService.createCourse(eq(5L), any(Course.class)))
                .thenThrow(new RuntimeException("TestCreateCourse error"));

        mockMvc.perform(post("/api/courses/class/{classId}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Unexpected error:")))
                .andExpect(content().string(containsString("TestCreateCourse error")));

        verify(courseService).createCourse(eq(5L), any(Course.class));
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testUpdateCourse_InternalServerError() throws Exception {
        when(courseService.updateCourse(eq(1L), any(Course.class)))
                .thenThrow(new RuntimeException("TestUpdateCourse error"));

        mockMvc.perform(put("/api/courses/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Unexpected error:")))
                .andExpect(content().string(containsString("TestUpdateCourse error")));

        verify(courseService).updateCourse(eq(1L), any(Course.class));
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testDeleteCourse_InternalServerError() throws Exception {
        doThrow(new RuntimeException("TestDeleteCourse error"))
                .when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/{id}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Unexpected error:")))
                .andExpect(content().string(containsString("TestDeleteCourse error")));

        verify(courseService).deleteCourse(1L);
        verifyNoMoreInteractions(courseService);
    }
}
