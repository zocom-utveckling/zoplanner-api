package com.zo.webapi.controller;

import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.dto.UpdateClassGroupRequestDTO;
import com.zo.webapi.service.ClassGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/classes")
public class ClassGroupController {

    private final ClassGroupService classGroupService;

    public ClassGroupController(ClassGroupService classGroupService) {
        this.classGroupService = classGroupService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassGroupResponseDTO createClass(@Valid @RequestBody CreateClassGroupRequestDTO requestDTO) {
        return classGroupService.createClass(requestDTO);
    }

    // READ
    @GetMapping("/{id}")
    public ClassGroupResponseDTO getClassById(@PathVariable Long id) {
        return classGroupService.getClassById(id);
    }

    @GetMapping
    public List<ClassGroupResponseDTO> getAllClasses() {
        return classGroupService.getAllClasses();
    }

    @GetMapping("/customer/{customerId}")
    public List<ClassGroupResponseDTO> getClassesByCustomerId(@PathVariable Long customerId){
        return classGroupService.getClassesByCustomerId(customerId);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ClassGroupResponseDTO updateClass(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClassGroupRequestDTO requestDTO) {
        return classGroupService.updateClass(id, requestDTO);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClass(@PathVariable Long id) {
        classGroupService.deleteClass(id);
    }
}
