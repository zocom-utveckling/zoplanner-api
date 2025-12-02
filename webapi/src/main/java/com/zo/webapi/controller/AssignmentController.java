package com.zo.webapi.controller;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Session;
import com.zo.webapi.service.SessionService;
import com.zo.webapi.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final SessionService sessionService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService, SessionService sessionService) {
        this.assignmentService = assignmentService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        return assignment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByConsultant(@PathVariable Long consultantId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByConsultant(consultantId));
    }


    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        Assignment created = assignmentService.createAssignment(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long id,
            @RequestBody Assignment assignment) {
        try {
            Assignment updated = assignmentService.updateAssignment(id, assignment);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Sessions
    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<Session>> getSessionsByAssignmentId(@PathVariable Long id){
        return ResponseEntity.ok(sessionService.getSessionsByAssignmentId(id));
    }

    @PostMapping("/{id}/sessions")
    public ResponseEntity<Session> addSession(@PathVariable Long id, Session session){
        Session result = sessionService.createSession(id, session);
        if(result != null){
            return ResponseEntity.ok(result);
        } else return ResponseEntity.notFound().build();
    }
}