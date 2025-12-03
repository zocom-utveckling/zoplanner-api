package com.zo.webapi.service;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import com.zo.webapi.enums.ConsultantStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultantStatusService {

    private final ConsultantStatusRepository statusRepository;
    private final ConsultantRepository consultantRepository;

    @Transactional
    public ConsultantStatus createStatus(Long consultantId, ConsultantStatusType status,
                                         LocalDate start, LocalDate end, String comment) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("Consultant not found with id: " + consultantId));

        ConsultantStatus consultantStatus = new ConsultantStatus();
        consultantStatus.setConsultant(consultant);
        consultantStatus.setStatus(status);
        consultantStatus.setDateStart(start);
        consultantStatus.setDateEnd(end);
        consultantStatus.setComment(comment);

        return statusRepository.save(consultantStatus);
    }

    @Transactional(readOnly = true)
    public List<ConsultantStatus> getStatusesByConsultant(Long consultantId) {
        return statusRepository.findByConsultantId(consultantId);
    }
}
