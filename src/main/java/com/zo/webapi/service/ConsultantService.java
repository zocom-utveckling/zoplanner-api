package com.zo.webapi.service;

import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Consultant", "id", id));
    }

    @Transactional(readOnly = true)
    public ConsultantDTO getConsultantByUserId(Long userId) {
        return consultantRepository.findByUserId(userId)
                .map(ConsultantMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Consultant", "userId", userId));
    }

    @Transactional(readOnly = true)
    public List<ConsultantDTO> getConsultantsByManagerId(Long managerId) {
        // Verifiera att managern existerar
        if (!managerRepository.existsById(managerId)) {
            throw new ResourceNotFoundException("Manager", "id", managerId);
        }

        return consultantRepository.findByManagerId(managerId)
                .stream()
                .map(ConsultantMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultantDTO> getConsultantsByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new InvalidDataException("City cannot be null or empty");
        }

        return consultantRepository.findByCityIgnoreCase(city)
                .stream()
                .map(ConsultantMapper::toDTO)
                .toList();
    }

    @Transactional
    public ConsultantDTO createConsultant(ConsultantDTO dto) {
        // Validering
        validateConsultantDTO(dto);

        // Kontrollera om användaren redan har en konsult
        if (consultantRepository.findByUserId(dto.getUserId()).isPresent()) {
            throw new InvalidDataException("User already has a consultant profile");
        }

        Consultant consultant = ConsultantMapper.toEntity(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getUserId()));

        Manager manager = null;
        if (dto.getManagerId() != null) {
            manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", dto.getManagerId()));
        }

        consultant.setUser(user);
        consultant.setManager(manager);

        Consultant saved = consultantRepository.save(consultant);
        return ConsultantMapper.toDTO(saved);
    }

    @Transactional
    public ConsultantDTO updateConsultant(Long id, ConsultantDTO dto) {
        // Validering
        if (dto.getCity() == null || dto.getCity().trim().isEmpty()) {
            throw new InvalidDataException("City cannot be null or empty");
        }

        Consultant existing = consultantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultant", "id", id));

        existing.setCity(dto.getCity());

        if (dto.getManagerId() != null) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", dto.getManagerId()));
            existing.setManager(manager);
        } else {
            existing.setManager(null);
        }

        Consultant saved = consultantRepository.save(existing);
        return ConsultantMapper.toDTO(saved);
    }

    @Transactional
    public void deleteConsultant(Long id) {
        if (!consultantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consultant", "id", id);
        }
        consultantRepository.deleteById(id);
    }

    private void validateConsultantDTO(ConsultantDTO dto) {
        if (dto == null) {
            throw new InvalidDataException("Consultant data cannot be null");
        }
        if (dto.getUserId() == null) {
            throw new InvalidDataException("User ID is required");
        }
        if (dto.getCity() == null || dto.getCity().trim().isEmpty()) {
            throw new InvalidDataException("City is required");
        }
    }
}