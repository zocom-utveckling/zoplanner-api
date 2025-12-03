package com.zo.webapi.service;

import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Course;
import com.zo.webapi.repository.AssignmentRepository;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ConsultantRepository consultantRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository,
                             ConsultantRepository consultantRepository,
                             CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.consultantRepository = consultantRepository;
        this.courseRepository = courseRepository;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public List<Assignment> getAssignmentsByConsultant(Long consultantId) {
        return assignmentRepository.findByConsultant_Id(consultantId);
    }

    @Transactional
    public Assignment createAssignment(AssignmentDTO dto) {
        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Assignment assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(dto.getDateStart());
        assignment.setDateEnd(dto.getDateEnd());

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignment(Long id, AssignmentDTO dto) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(dto.getDateStart());
        assignment.setDateEnd(dto.getDateEnd());

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }
}