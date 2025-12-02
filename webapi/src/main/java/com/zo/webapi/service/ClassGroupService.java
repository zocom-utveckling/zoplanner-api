package com.zo.webapi.service;

import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.dto.UpdateClassGroupRequestDTO;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClassGroupService {

    private final ClassGroupRepository classGroupRepository;
    private final CustomerRepository customerRepository;

    public ClassGroupService(ClassGroupRepository classGroupRepository, CustomerRepository customerRepository) {
        this.classGroupRepository = classGroupRepository;
        this.customerRepository = customerRepository;
    }

    // CREATE
    @Transactional
    public ClassGroupResponseDTO createClass(CreateClassGroupRequestDTO requestDTO) {

        // Validate customer
        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() ->  new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found with id: " + requestDTO.getCustomerId()
                ));

        // Check duplicate class name for the same customer
        if (classGroupRepository.existsByNameAndCustomerId(requestDTO.getName(), requestDTO.getCustomerId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Class name already exists for this customer"
            );
        }
        // Create and save class
        ClassGroup classGroup = new ClassGroup();
        classGroup.setName(requestDTO.getName());
        classGroup.setCustomer(customer);

        classGroup = classGroupRepository.save(classGroup);

        // return DTO
        return new ClassGroupResponseDTO(
                classGroup.getId(),
                classGroup.getName(),
                customer.getId(),
                customer.getName()
        );

    }

    public List<ClassGroupResponseDTO> getAllClasses() {
        List<ClassGroup> classGroups = classGroupRepository.findAll();
        List<ClassGroupResponseDTO> responseDTOs = new ArrayList<>();

        for (ClassGroup classGroup : classGroups) {
            Customer customer = classGroup.getCustomer();
            ClassGroupResponseDTO responseDTO = new ClassGroupResponseDTO(
                    classGroup.getId(),
                    classGroup.getName(),
                    customer.getId(),
                    customer.getName()
            );
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    public ClassGroupResponseDTO getClassById(Long id) {
        ClassGroup classGroup = classGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Class not found with id: " + id
                ));

        Customer customer = classGroup.getCustomer();

        return new ClassGroupResponseDTO(
                classGroup.getId(),
                classGroup.getName(),
                customer.getId(),
                customer.getName()
        );
    }

    public List<ClassGroupResponseDTO> getClassesByCustomerId(Long customerId) {

        // Validate customer
        if(!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerId);
        }

        List<ClassGroup> classGroups = classGroupRepository.findByCustomerId(customerId);
        List<ClassGroupResponseDTO> responseDTOs = new ArrayList<>();

        for (ClassGroup classGroup : classGroups) {
            Customer customer = classGroup.getCustomer();
            responseDTOs.add(new ClassGroupResponseDTO(
                    classGroup.getId(),
                    classGroup.getName(),
                    customer.getId(),
                    customer.getName()
            ));
        }
        return responseDTOs;
    }

    // UPDATE

    @Transactional
    public ClassGroupResponseDTO updateClass(Long id, UpdateClassGroupRequestDTO requestDTO) {
        ClassGroup classGroup = classGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Class not found with id: " + id
                ));
        //update name
        classGroup.setName(requestDTO.getName());
        classGroupRepository.save(classGroup);

        Customer customer = classGroup.getCustomer();
        return new ClassGroupResponseDTO(
                classGroup.getId(),
                classGroup.getName(),
                customer.getId(),
                customer.getName()
        );
    }

    // DELETE

    @Transactional
    public void deleteClass(Long id) {
        ClassGroup classGroup = classGroupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Class not found with id: " + id
                ));

        classGroupRepository.delete(classGroup);
    }

}