package com.zo.webapi.service;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public List<Assignment> getAssignmentsByConsultant(Long consultantId) {
        return assignmentRepository.findByConsultantId(consultantId);
    }

    public List<Assignment> getAssignmentsByClass(Long classId) {
        return assignmentRepository.findByClassId(classId);
    }


    @Transactional
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignment(Long id, Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));

        assignment.setCourseName(assignmentDetails.getCourseName());
        assignment.setConsultantId(assignmentDetails.getConsultantId());
        assignment.setDateStart(assignmentDetails.getDateStart());
        assignment.setDateEnd(assignmentDetails.getDateEnd());
        assignment.setClassId(assignmentDetails.getClassId());

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
