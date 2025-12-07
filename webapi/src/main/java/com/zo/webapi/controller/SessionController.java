package com.zo.webapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zo.webapi.model.Session;
import com.zo.webapi.service.SessionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id){
        Optional<Session> assignment = sessionService.getSessionById(id);
        return assignment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/assignments/{id}/")
    public ResponseEntity<List<Session>> getSessionsByAssignmentId(@PathVariable Long id){
        return ResponseEntity.ok(sessionService.getSessionsByAssignmentId(id));
    }

    @PostMapping("/{assignmentId}")
    public ResponseEntity<Session> createSession(@PathVariable Long assignmentId, @RequestBody Session session) {
        Session createdSession = sessionService.createSession(assignmentId, session);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        try {
            Session updatedSession = sessionService.updateSession(id, session);
            return ResponseEntity.ok(updatedSession);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id){
        boolean sessionDeleted = sessionService.deleteSessionById(id);

        if(!sessionDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
