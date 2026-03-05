package com.zo.webapi.service;

import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Course;
import com.zo.webapi.repository.AssignmentRepository;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.CourseRepository;
import com.zo.webapi.specification.AssignmentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByConsultant(Long consultantId) {
        // Verifiera att konsulten existerar
        if (!consultantRepository.existsById(consultantId)) {
            throw new ResourceNotFoundException("Consultant", "id", consultantId);
        }

        return assignmentRepository.findByConsultant_Id(consultantId);
    }

    public List<Assignment> getAssignmentsByVisibility(boolean published) {
        Specification<Assignment> spec = Specification.allOf(AssignmentSpecification.isPublished(published));
        return assignmentRepository.findAll(spec);
    }

    public List<Assignment> getAssignmentsByConsultantAndVisibility(Long consultantId, boolean published) {
        Specification<Assignment> spec = Specification.allOf(
                AssignmentSpecification.isPublished(published),
                AssignmentSpecification.hasConsultant(consultantId)
        );
        return assignmentRepository.findAll(spec);
    }

    @Transactional
    public Assignment createAssignment(AssignmentDTO dto) {
        // Validera DTO
        validateAssignmentDTO(dto);

        // Validera datum
        validateDates(dto.getDateStart(), dto.getDateEnd());

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultant", "id", dto.getConsultantId()));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        Assignment assignment = new Assignment();
        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(dto.getDateStart());
        assignment.setDateEnd(dto.getDateEnd());
        if(dto.isPublished() == null) assignment.setPublished(false); //Utkast by-default
        else assignment.setPublished(dto.isPublished());

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignment(Long id, AssignmentDTO dto) {
        // Validera DTO
        validateAssignmentDTO(dto);

        // Validera datum
        validateDates(dto.getDateStart(), dto.getDateEnd());

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", id));

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultant", "id", dto.getConsultantId()));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        assignment.setConsultant(consultant);
        assignment.setCourse(course);
        assignment.setDateStart(dto.getDateStart());
        assignment.setDateEnd(dto.getDateEnd());
        if(dto.isPublished() != null) assignment.setPublished(dto.isPublished());

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assignment", "id", id);
        }
        assignmentRepository.deleteById(id);
    }

    private void validateAssignmentDTO(AssignmentDTO dto) {
        if (dto == null) {
            throw new InvalidDataException("Assignment data cannot be null");
        }
        if (dto.getConsultantId() == null) {
            throw new InvalidDataException("Consultant ID is required");
        }
        if (dto.getCourseId() == null) {
            throw new InvalidDataException("Course ID is required");
        }
        if (dto.getDateStart() == null) {
            throw new InvalidDataException("Start date is required");
        }
        if (dto.getDateEnd() == null) {
            throw new InvalidDataException("End date is required");
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDataException("Start date cannot be after end date");
        }
    }
}