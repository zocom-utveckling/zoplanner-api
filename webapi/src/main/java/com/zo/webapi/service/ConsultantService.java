package com.zo.webapi.service;

import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.mapper.ConsultantMapper;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultantService {

    private final ConsultantRepository consultantRepository;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;

    @Transactional(readOnly = true)
    public List<ConsultantDTO> getAllConsultants() {
        return consultantRepository.findAll()
                .stream()
                .map(ConsultantMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsultantDTO getConsultantById(Long id) {
        return consultantRepository.findById(id)
                .map(ConsultantMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Consultant not found: " + id));
    }

    @Transactional(readOnly = true)
    public ConsultantDTO getConsultantByUserId(Long userId) {
        return consultantRepository.findByUserId(userId)
                .map(ConsultantMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Consultant not found for user: " + userId));
    }

    @Transactional(readOnly = true)
    public List<ConsultantDTO> getConsultantsByManagerId(Long managerId) {
        return consultantRepository.findByManagerId(managerId)
                .stream()
                .map(ConsultantMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultantDTO> getConsultantsByCity(String city) {
        return consultantRepository.findByCity(city)
                .stream()
                .map(ConsultantMapper::toDTO)
                .toList();
    }

    @Transactional
    public ConsultantDTO createConsultant(ConsultantDTO dto) {
        Consultant consultant = ConsultantMapper.toEntity(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Manager manager = dto.getManagerId() != null
                ? managerRepository.findById(dto.getManagerId()).orElse(null)
                : null;

        consultant.setUser(user);
        consultant.setManager(manager);

        Consultant saved = consultantRepository.save(consultant);
        return ConsultantMapper.toDTO(saved);
    }

    @Transactional
    public ConsultantDTO updateConsultant(Long id, ConsultantDTO dto) {
        Consultant existing = consultantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        existing.setCity(dto.getCity());

        if (dto.getManagerId() != null) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            existing.setManager(manager);
        } else {
            existing.setManager(null);
        }

        Consultant saved = consultantRepository.save(existing);
        return ConsultantMapper.toDTO(saved);
    }

    @Transactional
    public void deleteConsultant(Long id) {
        consultantRepository.deleteById(id);
    }
}
