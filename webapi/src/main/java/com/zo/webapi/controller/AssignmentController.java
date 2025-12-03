package com.zo.webapi.controller;

import com.zo.webapi.dto.AssignmentDTO;
import com.zo.webapi.model.Assignment;
import com.zo.webapi.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
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
        return assignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByConsultant(@PathVariable Long consultantId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByConsultant(consultantId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody AssignmentDTO assignmentDto) {
        Assignment createdAssignment = assignmentService.createAssignment(assignmentDto);
        return ResponseEntity.ok(createdAssignment);
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
