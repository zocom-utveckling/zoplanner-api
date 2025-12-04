package com.zo.webapi.controller;

import com.zo.webapi.dto.ConsultantDTO;
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
    public ResponseEntity<List<ConsultantDTO>> getAllConsultants() {
        return ResponseEntity.ok(consultantService.getAllConsultants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultantDTO> getConsultantById(@PathVariable Long id) {
        try {
            ConsultantDTO dto = consultantService.getConsultantById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ConsultantDTO> getConsultantByUserId(@PathVariable Long userId) {
        try {
            ConsultantDTO dto = consultantService.getConsultantByUserId(userId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<ConsultantDTO>> getConsultantsByManagerId(@PathVariable Long managerId) {
        return ResponseEntity.ok(consultantService.getConsultantsByManagerId(managerId));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<ConsultantDTO>> getConsultantsByCity(@PathVariable String city) {
        return ResponseEntity.ok(consultantService.getConsultantsByCity(city));
    }

    @PostMapping
    public ResponseEntity<ConsultantDTO> createConsultant(@RequestBody ConsultantDTO consultantDTO) {
        try {
            ConsultantDTO created = consultantService.createConsultant(consultantDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultantDTO> updateConsultant(
            @PathVariable Long id,
            @RequestBody ConsultantDTO consultantDTO) {
        try {
            ConsultantDTO updated = consultantService.updateConsultant(id, consultantDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        try {
            consultantService.deleteConsultant(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
