package com.zo.webapi.controller;

import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        Assignment assignment = assignmentService.getAssignmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment", "id", id));
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByConsultant(@PathVariable Long consultantId, @RequestParam(required = false) Boolean published) {
        List<Assignment> assignments;
        if(published != null) {
            assignments = assignmentService.getAssignmentsByConsultantAndVisibility(consultantId, published);
        }
        else {
            assignments = assignmentService.getAssignmentsByConsultant(consultantId);
        }
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/visibility")
    public ResponseEntity<List<Assignment>> getAssignmentsByVisibility(@RequestParam(required = false) boolean published) {
        List<Assignment> assignments = assignmentService.getAssignmentsByVisibility(published);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody AssignmentDTO assignmentDto) {
        Assignment createdAssignment = assignmentService.createAssignment(assignmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssignment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id,
                                                       @RequestBody AssignmentDTO assignmentDto) {
        Assignment updatedAssignment = assignmentService.updateAssignment(id, assignmentDto);
        return ResponseEntity.ok(updatedAssignment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}