package com.zo.webapi.service;

import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultantStatusService {

    private final ConsultantStatusRepository statusRepository;
    private final ConsultantRepository consultantRepository;

    public ConsultantStatus createStatus(Long consultantId, ConsultantStatusType status,
                                         LocalDate start, LocalDate end, String comment) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("Consultant not found"));

        ConsultantStatus cs = new ConsultantStatus();
        cs.setConsultant(consultant);
        cs.setStatus(status);
        cs.setDateStart(start);
        cs.setDateEnd(end);
        cs.setComment(comment);

        return statusRepository.save(cs);
    }

    public List<ConsultantStatus> getStatusesByConsultant(Long consultantId) {
        return statusRepository.findByConsultantId(consultantId);
    }
}
