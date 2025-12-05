package com.zo.webapi.service;

import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Course;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final ClassGroupRepository classGroupRepository;

    public CourseService(CourseRepository courseRepository, ClassGroupRepository classGroupRepository) {
        this.courseRepository = courseRepository;
        this.classGroupRepository = classGroupRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
    }

    public List<Course> getCourseByClassGroupId(Long classGroupId) {
        return courseRepository.findByClassGroupId(classGroupId);
    }

    public Course createCourse(Long classId, Course course) {
        ClassGroup classGroup = classGroupRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class group not found with id: " + classId));

        course.setClassGroup(classGroup);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));

        course.setName(updatedCourse.getName());
        course.setDateStart(updatedCourse.getDateStart());
        course.setDateEnd(updatedCourse.getDateEnd());

        if (updatedCourse.getClassGroup() != null) {
            Long newClassGroupId = updatedCourse.getClassGroup().getId();

            ClassGroup classGroup = classGroupRepository.findById(newClassGroupId)
                    .orElseThrow(() -> new IllegalArgumentException("Class group not found with id: " + newClassGroupId));
            course.setClassGroup(classGroup);
        }

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found with id: " + id);

        }
        courseRepository.deleteById(id);
    }
}
