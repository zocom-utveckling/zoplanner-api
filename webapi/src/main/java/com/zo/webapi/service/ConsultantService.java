package com.zo.webapi.service;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultantService {

    private final ConsultantRepository consultantRepository;

    @Transactional(readOnly = true)
    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Consultant> getConsultantById(Long id) {
        return consultantRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Consultant> getConsultantByUserId(Long userId) {
        return consultantRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Consultant> getConsultantsByManagerId(Long managerId) {
        return consultantRepository.findByManagerId(managerId);
    }

    @Transactional(readOnly = true)
    public List<Consultant> getConsultantsByCity(String city) {
        return consultantRepository.findByCity(city);
    }

    @Transactional
    public Consultant createConsultant(Consultant consultant) {
        if (consultantRepository.existsByUserId(consultant.getUser().getId())) {
            throw new IllegalArgumentException("Consultant with this user already exists");
        }
        return consultantRepository.save(consultant);
    }

    @Transactional
    public Consultant updateConsultant(Long id, Consultant consultantDetails) {
        Consultant consultant = consultantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consultant not found with id: " + id));

        consultant.setCity(consultantDetails.getCity());
        consultant.setManager(consultantDetails.getManager());

        return consultantRepository.save(consultant);
    }

    @Transactional
    public void deleteConsultant(Long id) {
        if (!consultantRepository.existsById(id)) {
            throw new IllegalArgumentException("Consultant not found with id: " + id);
        }
        consultantRepository.deleteById(id);
    }
}