package com.zo.webapi.service;

import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultantStatusService {

    private final ConsultantStatusRepository statusRepository;
    private final ConsultantRepository consultantRepository;

    @Transactional
    public ConsultantStatus createStatus(ConsultantStatusDTO dto) {
        validateDates(dto);

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new IllegalArgumentException("Consultant not found"));

        ConsultantStatus status = new ConsultantStatus();
        status.setConsultant(consultant);
        status.setStatus(dto.getStatus());
        status.setDateStart(dto.getDateStart());
        status.setDateEnd(dto.getDateEnd());
        status.setComment(dto.getComment());

        return statusRepository.save(status);
    }

    @Transactional
    public ConsultantStatus updateStatus(Long id, ConsultantStatusDTO dto) {
        validateDates(dto);

        ConsultantStatus existing = statusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status not found"));

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new IllegalArgumentException("Consultant not found"));

        existing.setConsultant(consultant);
        existing.setStatus(dto.getStatus());
        existing.setDateStart(dto.getDateStart());
        existing.setDateEnd(dto.getDateEnd());
        existing.setComment(dto.getComment());

        return statusRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<ConsultantStatus> getStatusesByConsultant(Long consultantId) {
        return statusRepository.findByConsultantId(consultantId);
    }

    @Transactional(readOnly = true)
    public ConsultantStatus getStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status not found"));
    }

    @Transactional(readOnly = true)
    public List<ConsultantStatus> getAllStatuses() {
        return statusRepository.findAll();
    }

    @Transactional
    public void deleteStatus(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new IllegalArgumentException("Status not found");
        }
        statusRepository.deleteById(id);
    }

    private void validateDates(ConsultantStatusDTO dto) {
        if (dto.getDateEnd().isBefore(dto.getDateStart())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
}
