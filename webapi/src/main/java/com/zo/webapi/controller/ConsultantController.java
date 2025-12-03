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
        return ResponseEntity.ok(consultantService.getConsultantById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ConsultantDTO> getConsultantByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(consultantService.getConsultantByUserId(userId));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<ConsultantDTO>> getConsultantsByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(consultantService.getConsultantsByManagerId(managerId));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<ConsultantDTO>> getConsultantsByCity(@PathVariable String city) {
        return ResponseEntity.ok(consultantService.getConsultantsByCity(city));
    }

    @PostMapping
    public ResponseEntity<ConsultantDTO> createConsultant(@RequestBody ConsultantDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultantService.createConsultant(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultantDTO> updateConsultant(
            @PathVariable Long id,
            @RequestBody ConsultantDTO dto) {
        return ResponseEntity.ok(consultantService.updateConsultant(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        consultantService.deleteConsultant(id);
        return ResponseEntity.noContent().build();
    }
}