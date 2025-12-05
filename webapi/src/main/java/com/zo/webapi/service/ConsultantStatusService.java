package com.zo.webapi.service;

import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultantStatusService {

    private final ConsultantStatusRepository statusRepository;
    private final ConsultantRepository consultantRepository;

    // CREATE
    public ConsultantStatus createStatus(ConsultantStatusDTO dto) {

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consultant with ID " + dto.getConsultantId() + " not found"
                ));

        ConsultantStatus status = new ConsultantStatus();
        status.setConsultant(consultant);
        status.setStatus(dto.getStatus());
        status.setDateStart(dto.getDateStart());
        status.setDateEnd(dto.getDateEnd());
        status.setComment(dto.getComment());

        return statusRepository.save(status);
    }

    // UPDATE
    public ConsultantStatus updateStatus(Long id, ConsultantStatusDTO dto) {

        ConsultantStatus existing = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consultant status with ID " + id + " not found"
                ));

        existing.setStatus(dto.getStatus());
        existing.setDateStart(dto.getDateStart());
        existing.setDateEnd(dto.getDateEnd());
        existing.setComment(dto.getComment());

        return statusRepository.save(existing);
    }

    // GET BY ID
    public ConsultantStatus getStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Status with ID " + id + " not found"
                ));
    }

    // GET BY CONSULTANT
    public List<ConsultantStatus> getStatusesByConsultant(Long consultantId) {
        List<ConsultantStatus> list = statusRepository.findByConsultantId(consultantId);

        if (list.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No statuses found for consultant ID " + consultantId
            );
        }

        return list;
    }

    // GET ALL
    public List<ConsultantStatus> getAllStatuses() {
        return statusRepository.findAll();
    }

    // DELETE
    public void deleteStatus(Long id) {
        ConsultantStatus existing = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Status with ID " + id + " not found"
                ));

        statusRepository.delete(existing);
    }
}
