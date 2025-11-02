package com.zo.webapi.controller;

import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.service.ClassGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/classes")
public class ClassGroupController {

    private ClassGroupService classGroupService;

    @Autowired
    public ClassGroupController(ClassGroupService classGroupService) {
        this.classGroupService = classGroupService;
    }

    @GetMapping
    public ResponseEntity<List<ClassGroup>> getAllClass() {

        return ResponseEntity.ok(classGroupService.getAllClass());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassGroup> getClassById(@PathVariable Long id) {
        Optional<ClassGroup> cls = classGroupService.getClassById(id);  // ✅ CORRECT
        return cls.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ClassGroup>> getClassesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(classGroupService.getClassByCustomer(customerId));
    }

    @PostMapping
    public ResponseEntity<ClassGroup> createClass(@RequestBody ClassGroup newClass) {
        ClassGroup created = classGroupService.createClass(newClass);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassGroup> updateClass(@PathVariable Long id, @RequestBody ClassGroup classDetails) {
        ClassGroup updated = classGroupService.updateClass(id, classDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClass(@PathVariable Long id) {
        classGroupService.deleteClass(id);
        return ResponseEntity.ok("Class deleted successfully");
    }

}
