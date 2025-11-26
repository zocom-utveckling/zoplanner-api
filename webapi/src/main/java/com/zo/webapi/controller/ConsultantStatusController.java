package com.zo.webapi.controller;

import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.service.ConsultantStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

        import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consultant-status")
@RequiredArgsConstructor
public class ConsultantStatusController {

    private final ConsultantStatusService statusService;

    @PostMapping
    public ConsultantStatus createStatus(@RequestParam Long consultantId,
                                         @RequestParam ConsultantStatusType status,
                                         @RequestParam String start,
                                         @RequestParam String end,
                                         @RequestParam(required = false) String comment) {
        return statusService.createStatus(consultantId, status,
                LocalDate.parse(start), LocalDate.parse(end), comment);
    }

    @GetMapping("/{consultantId}")
    public List<ConsultantStatus> getStatuses(@PathVariable Long consultantId) {
        return statusService.getStatusesByConsultant(consultantId);
    }
}
