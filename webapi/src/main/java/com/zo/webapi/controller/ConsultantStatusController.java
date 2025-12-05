package com.zo.webapi.controller;

import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.service.ConsultantStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultant-status")
@RequiredArgsConstructor
public class ConsultantStatusController {

    private final ConsultantStatusService statusService;

    @PostMapping
    public ConsultantStatus createStatus(@Valid @RequestBody ConsultantStatusDTO dto) {
        return statusService.createStatus(dto);
    }

    @PutMapping("/{id}")
    public ConsultantStatus updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ConsultantStatusDTO dto) {
        return statusService.updateStatus(id, dto);
    }

    @GetMapping("/{consultantId}")
    public List<ConsultantStatus> getStatuses(@PathVariable Long consultantId) {
        return statusService.getStatusesByConsultant(consultantId);
    }

    @GetMapping("/status/{id}")
    public ConsultantStatus getStatusById(@PathVariable Long id) {
        return statusService.getStatusById(id);
    }

    @GetMapping
    public List<ConsultantStatus> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
    }
}
