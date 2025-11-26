package com.zo.webapi.controller;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.service.ConsultantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultants")
@RequiredArgsConstructor
public class ConsultantController {

    private final ConsultantService consultantService;

    @GetMapping
    public ResponseEntity<List<Consultant>> getAllConsultants() {
        return ResponseEntity.ok(consultantService.getAllConsultants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultant> getConsultantById(@PathVariable Long id) {
        return consultantService.getConsultantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Consultant> getConsultantByUserId(@PathVariable Long userId) {
        return consultantService.getConsultantByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Consultant>> getConsultantsByManagerId(@PathVariable Long managerId) {
        return ResponseEntity.ok(consultantService.getConsultantsByManagerId(managerId));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Consultant>> getConsultantsByCity(@PathVariable String city) {
        return ResponseEntity.ok(consultantService.getConsultantsByCity(city));
    }

    @PostMapping
    public ResponseEntity<Consultant> createConsultant(@RequestBody Consultant consultant) {
        try {
            Consultant created = consultantService.createConsultant(consultant);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consultant> updateConsultant(
            @PathVariable Long id,
            @RequestBody Consultant consultant) {
        try {
            Consultant updated = consultantService.updateConsultant(id, consultant);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        try {
            consultantService.deleteConsultant(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}