package com.zo.webapi.controller;

import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.ConsultantResponseDTO;
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
    public ResponseEntity<List<ConsultantResponseDTO>> getAllConsultants() {
        List<ConsultantResponseDTO> consultants = consultantService.getAllConsultants();
        return ResponseEntity.ok(consultants);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ConsultantDTO>> searchConsultants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Long manager) {
        return ResponseEntity.ok(consultantService.searchConsultants(name, city, manager));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultantResponseDTO> getConsultantById(@PathVariable Long id) {
        ConsultantResponseDTO dto = consultantService.getConsultantById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ConsultantResponseDTO> getConsultantByUserId(@PathVariable Long userId) {
        ConsultantResponseDTO dto = consultantService.getConsultantByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<ConsultantResponseDTO>> getConsultantsByManagerId(@PathVariable Long managerId) {
        List<ConsultantResponseDTO> consultants = consultantService.getConsultantsByManagerId(managerId);
        return ResponseEntity.ok(consultants);
    }

    @PostMapping
    public ResponseEntity<ConsultantResponseDTO> createConsultant(@RequestBody ConsultantDTO consultantDTO) {
        ConsultantResponseDTO created = consultantService.createConsultant(consultantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultantResponseDTO> updateConsultant(
            @PathVariable Long id,
            @RequestBody ConsultantDTO consultantDTO) {
        ConsultantResponseDTO updated = consultantService.updateConsultant(id, consultantDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        consultantService.deleteConsultant(id);
        return ResponseEntity.noContent().build();
    }
}