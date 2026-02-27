package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Course;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CourseRepository;
import com.zo.webapi.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class CourseServiceIntegrationTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ClassGroupRepository classGroupRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private ClassGroup classGroup1;
    private ClassGroup classGroup2;
    private Course course1;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        classGroupRepository.deleteAll();
        customerRepository.deleteAll();

        customer1 = new Customer();
        customer1.setName("Customer A");
        customer1.setCity("Stockholm");
        customer1 = customerRepository.save(customer1);

        classGroup1 = new ClassGroup();
        classGroup1.setName("Class A");
        classGroup1.setCustomer(customer1);
        classGroup1 = classGroupRepository.save(classGroup1);

        classGroup2 = new ClassGroup();
        classGroup2.setName("Class B");
        classGroup2.setCustomer(customer1);
        classGroup2 = classGroupRepository.save(classGroup2);

        course1 = new Course();
        course1.setName("Course 1");
        course1.setClassGroup(classGroup1);
        course1.setDateStart(LocalDate.of(2026, 1, 1));
        course1.setDateEnd(LocalDate.of(2026, 1, 10));
        course1 = courseRepository.save(course1);
    }

    @Test
    void testGetAllCourses_Success() {
        List<Course> allCourses = courseService.getAllCourses();

        assertThat(allCourses).isNotEmpty();
        assertThat(allCourses).extracting(Course::getId).contains(course1.getId());
    }

    @Test
    void testGetCourseById_Success() {
        Course found = courseService.getCourseById(course1.getId());

        assertThat(found.getId()).isEqualTo(course1.getId());
        assertThat(found.getName()).isEqualTo("Course 1");
        assertThat(found.getClassGroup()).isNotNull();
        assertThat(found.getClassGroup().getId()).isEqualTo(classGroup1.getId());
    }

    @Test
    void testGetCourseById_NotFound() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> courseService.getCourseById(9999L));

        assertThat(exc.getMessage()).contains("Course not found with id: 9999");
    }

    @Test
    void testGetCourseByClassGroupId_Success() {
        List<Course> courses = courseService.getCourseByClassGroupId(classGroup1.getId());

        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getId()).isEqualTo(course1.getId());
    }

    @Test
    void testCreateCourse_Success() {
        Course newCourse = new Course();
        newCourse.setName("New Course");
        newCourse.setDateStart(LocalDate.of(2026, 2, 1));
        newCourse.setDateEnd(LocalDate.of(2026, 2, 5));

        Course created = courseService.createCourse(classGroup2.getId(), newCourse);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("New Course");
        assertThat(created.getClassGroup()).isNotNull();
        assertThat(created.getClassGroup().getId()).isEqualTo(classGroup2.getId());
        assertThat(courseRepository.findById(created.getId())).isPresent();
    }

    @Test
    void testCreateCourse_ClassNotFound() {
        Course newCourse = new Course();
        newCourse.setName("New Course");
        newCourse.setDateStart(LocalDate.of(2026, 3, 1));
        newCourse.setDateEnd(LocalDate.of(2026, 3, 5));

        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> courseService.createCourse(9999L, newCourse));

        assertThat(exc.getMessage()).contains("Class group not found with id: 9999");
    }

    @Test
    void testUpdateCourse_Success() {
        Course course = new Course();
        course.setName("Course 1 Updated");
        course.setDateStart(LocalDate.of(2026, 4, 1));
        course.setDateEnd(LocalDate.of(2026, 4, 5));

        Course updated = courseService.updateCourse(course1.getId(), course);

        assertThat(updated.getId()).isEqualTo(course1.getId());
        assertThat(updated.getName()).isEqualTo("Course 1 Updated");
        assertThat(updated.getDateStart()).isEqualTo(LocalDate.of(2026, 4, 1));
        assertThat(updated.getDateEnd()).isEqualTo(LocalDate.of(2026, 4, 5));
        assertThat(updated.getClassGroup().getId()).isEqualTo(classGroup1.getId());
    }


    @Test
    void testUpdateCourse_ChangeClassGroup_Success() {
        Course course = new Course();
        course.setName("Course 1 moved");
        course.setDateStart(LocalDate.of(2026, 5, 1));
        course.setDateEnd(LocalDate.of(2026, 5, 5));

        ClassGroup newGroup = new ClassGroup();
        newGroup.setId(classGroup2.getId());
        course.setClassGroup(newGroup);

        Course updated = courseService.updateCourse(course1.getId(), course);

        assertThat(updated.getName()).isEqualTo("Course 1 moved");
        assertThat(updated.getClassGroup().getId()).isEqualTo(classGroup2.getId());
    }

    @Test
    void testUpdateCourse_NotFound() {
        Course course = new Course();
        course.setName("Not found");

        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> courseService.updateCourse(9999L, course));

        assertThat(exc.getMessage()).contains("Course not found with id: 9999");
    }

    @Test
    void testDeleteCourse_Success() {
        courseService.deleteCourse(course1.getId());

        assertThat(courseRepository.findById(course1.getId())).isEmpty();
    }

    @Test
    void testDeleteCourse_NotFound() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> courseService.deleteCourse(9999L));

        assertThat(exc.getMessage()).contains("Course not found with id: 9999");
    }
}
