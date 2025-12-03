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
    public ResponseEntity<ClassGroupResponseDTO> createClass(@RequestBody CreateClassGroupRequestDTO requestDTO) {
        ClassGroupResponseDTO response = classGroupService.createClass(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ BY id
    @GetMapping("/{id}")
    public ResponseEntity<ClassGroupResponseDTO> getClassById(@PathVariable Long id) {
        return ResponseEntity.ok(classGroupService.getClassById(id));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ClassGroupResponseDTO>> getAllClasses() {
        return ResponseEntity.ok(classGroupService.getAllClasses());
    }

    // READ BY customerId
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ClassGroupResponseDTO>> getClassesByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(classGroupService.getClassesByCustomerId(customerId));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ClassGroupResponseDTO> updateClass(
            @PathVariable Long id,
            @RequestBody UpdateClassGroupRequestDTO requestDTO) {
        return ResponseEntity.ok(classGroupService.updateClass(id, requestDTO));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classGroupService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
