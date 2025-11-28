package service;

import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Course;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CourseRepository;
import com.zo.webapi.service.CourseService;
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

/*@ExtendWith(MockitoExtension.class)
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

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));
        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCourseById_Success() {
        Course course = new Course("Java Basics", null, LocalDate.now(), LocalDate.now().plusDays(5));
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Course result = courseService.getCourseById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Java Basics", result.getName());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseService.getCourseById(999L);
        });

        assertTrue(exception.getMessage().contains("Course not found"));


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
        assertEquals(classGroup, created.getClassGroup());
        verify(classGroupRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testCreateCourse_ClassNotFound() {
        Course course = new Course("Web Applications", null, LocalDate.now(), LocalDate.now().plusDays(5));
        when(classGroupRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseService.createCourse(999L, course);
        });

        assertTrue(exception.getMessage().contains("Classgroup not found"));
    }

    @Test
    void testUpdateCourse_Success() {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);

        Course existing = new Course("Old Name", classGroup, LocalDate.now(), LocalDate.now().plusDays(5));
        existing.setId(1L);

        Course updatedDetails = new Course("Updated Details", classGroup, LocalDate.now(), LocalDate.now().plusDays(10));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(existing)).thenReturn(existing);

        Course result = courseService.updateCourse(1L, updatedDetails);

        assertEquals("Updated Details", result.getName());
        assertEquals(existing.getId(), result.getId());
        assertEquals(classGroup, result.getClassGroup());
        verify(courseRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateCourse_NotFound() {
        Course updatedDetails = new Course("Updated Details", null, LocalDate.now(), LocalDate.now().plusDays(10));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseService.updateCourse(999L, updatedDetails);
        });

        assertTrue(exception.getMessage().contains("Course not found"));
    }

    @Test
    void testDeleteCourse_Success() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCourse_NotFound() {
        when(courseRepository.existsById(999L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseService.deleteCourse(999L);
        });

        assertTrue(exception.getMessage().contains("Course not found"));
    }

    @Test
    void testGetCoursesByClassId() {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1L);

        Course course1 = new Course("Java Basics", null, LocalDate.now(), LocalDate.now().plusDays(5));
        Course course2 = new Course("Testing 101", null, LocalDate.now(), LocalDate.now().plusDays(3));

        when(courseRepository.findByClassGroupId(1L)).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseService.getCourseByClassGroupId(1L);

        assertEquals(2, result.size());
        assertEquals("Java Basics", result.get(0).getName());
        assertEquals("Testing 101", result.get(1).getName());
        verify(courseRepository, times(1)).findByClassGroupId(1L);
    }

}*/
