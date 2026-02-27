package com.zo.webapi.service;

import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Course;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CourseRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ClassGroupRepository classGroupRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void testGetAllCourses() {
        Course course1 = new Course("Java Basics", null, LocalDate.now(), LocalDate.now().plusDays(5));
        Course course2 = new Course("Testing 101", null, LocalDate.now(), LocalDate.now().plusDays(10));

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository).findAll();
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testGetCourseById_Success() {
        Course course = new Course("Java Basics", null, LocalDate.now(), LocalDate.now().plusDays(5));
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Course result = courseService.getCourseById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Java Basics", result.getName());
        verify(courseRepository).findById(1L);
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> courseService.getCourseById(999L));

        assertEquals("Course not found with id: 999", exception.getMessage());

        verify(courseRepository).findById(999L);
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);

    }

    @Test
    void testCreateCourse_Success() {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);

        Course course = new Course("Web Applications", null, LocalDate.now(), LocalDate.now().plusDays(5));

        when(classGroupRepository.findById(1L)).thenReturn(Optional.of(classGroup));
        when(courseRepository.save(course)).thenReturn(course);

        Course created = courseService.createCourse(1L, course);

        assertEquals("Web Applications", created.getName());
        assertSame(classGroup, created.getClassGroup());

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(captor.capture());
        verify(classGroupRepository).findById(1L);

        Course saved = captor.getValue();
        assertSame(classGroup, saved.getClassGroup());
        verifyNoMoreInteractions(courseRepository, classGroupRepository);
    }

    @Test
    void testCreateCourse_ClassNotFound() {
        Course course = new Course("Web Applications", null, LocalDate.now(), LocalDate.now().plusDays(5));
        when(classGroupRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> courseService.createCourse(999L, course));

        assertEquals("Class group not found with id: 999", exception.getMessage());

        verify(classGroupRepository).findById(999L);
        verifyNoInteractions(courseRepository);
        verifyNoMoreInteractions(classGroupRepository);
    }

    @Test
    void testUpdateCourse_Success() {
        ClassGroup oldGroup = new ClassGroup();
        oldGroup.setId(1L);

        Course existing = new Course("Old Name", oldGroup, LocalDate.now(), LocalDate.now().plusDays(5));
        existing.setId(1L);

        Course updatedDetails = new Course("Updated Details", null, LocalDate.now(), LocalDate.now().plusDays(10));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(existing)).thenReturn(existing);

        Course result = courseService.updateCourse(1L, updatedDetails);

        assertEquals("Updated Details", result.getName());
        assertEquals(existing.getId(), result.getId());
        assertSame(oldGroup, result.getClassGroup());
        assertEquals(updatedDetails.getDateStart(), result.getDateStart());
        assertEquals(updatedDetails.getDateEnd(), result.getDateEnd());

        verify(courseRepository).findById(1L);
        verify(courseRepository).save(existing);
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testUpdateCourse_ChangeClassGroup() {
        ClassGroup oldGroup = new ClassGroup();
        oldGroup.setId(1L);

        ClassGroup newGroup = new ClassGroup();
        newGroup.setId(2L);

        Course existing = new Course("Old Name", oldGroup, LocalDate.now(), LocalDate.now().plusDays(5));
        existing.setId(1L);

        Course updated = new Course("Updated Name", newGroup, LocalDate.now(), LocalDate.now().plusDays(10));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(classGroupRepository.findById(2L)).thenReturn(Optional.of(newGroup));
        when(courseRepository.save(existing)).thenReturn(existing);

        Course result = courseService.updateCourse(1L, updated);

        assertEquals("Updated Name", result.getName());
        assertSame(newGroup, result.getClassGroup());

        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(captor.capture());
        Course saved = captor.getValue();
        assertSame(newGroup, saved.getClassGroup());

        verify(courseRepository).findById(1L);
        verify(classGroupRepository).findById(2L);
        verifyNoMoreInteractions(courseRepository, classGroupRepository);
    }

    @Test
    void testUpdateCourse_NotFound() {
        Course updatedDetails = new Course("Updated Details", null, LocalDate.now(), LocalDate.now().plusDays(10));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> courseService.updateCourse(999L, updatedDetails));

        assertEquals("Course not found with id: 999", exception.getMessage());

        verify(courseRepository).findById(999L);
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testDeleteCourse_Success() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        courseService.deleteCourse(1L);

        verify(courseRepository).existsById(1L);
        verify(courseRepository).deleteById(1L);
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testDeleteCourse_NotFound() {
        when(courseRepository.existsById(999L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> courseService.deleteCourse(999L));

        assertEquals("Course not found with id: 999", exception.getMessage());

        verify(courseRepository).existsById(999L);
        verify(courseRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testGetCoursesByClassId() {
        Course course1 = new Course("Java Basics", null, LocalDate.now(), LocalDate.now().plusDays(5));
        Course course2 = new Course("Testing 101", null, LocalDate.now(), LocalDate.now().plusDays(3));

        when(courseRepository.findByClassGroupId(1L)).thenReturn(List.of(course1, course2));

        List<Course> result = courseService.getCourseByClassGroupId(1L);

        assertEquals(2, result.size());
        assertEquals("Java Basics", result.get(0).getName());
        assertEquals("Testing 101", result.get(1).getName());

        verify(courseRepository).findByClassGroupId(1L);
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testUpdateCourse_ChangeClassGroup_NotFound() {
        ClassGroup oldGroup = new ClassGroup();
        oldGroup.setId(1L);

        ClassGroup newGroup = new ClassGroup();
        newGroup.setId(2L);

        Course existing = new Course("Old Name", oldGroup, LocalDate.now(), LocalDate.now().plusDays(5));
        existing.setId(1L);

        Course updated = new Course("Updated Name", newGroup, LocalDate.now(), LocalDate.now().plusDays(10));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(classGroupRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> courseService.updateCourse(1L, updated));

        assertEquals("Class group not found with id: 2", exc.getMessage());

        verify(courseRepository).findById(1L);
        verify(classGroupRepository).findById(2L);
        verify(courseRepository, never()).save(any());
        verifyNoMoreInteractions(courseRepository, classGroupRepository);
    }

    @Test
    void testCreateCourse_NullCourse_Throws () {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);
        when(classGroupRepository.findById(1L)).thenReturn(Optional.of(classGroup));

        assertThrows(NullPointerException.class,
                () -> courseService.createCourse(1L, null));

        verify(classGroupRepository).findById(1L);
        verifyNoInteractions(courseRepository);
    }

}
