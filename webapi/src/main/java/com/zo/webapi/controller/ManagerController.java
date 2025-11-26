package com.zo.webapi.controller;

import com.zo.webapi.dto.ManagerResponseDTO;
import com.zo.webapi.model.Manager;
import com.zo.webapi.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")

public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {

        this.managerService = managerService;
    }

    // CREATE
    @PostMapping
    public ManagerResponseDTO createManager(@RequestParam Long userId) {

        return managerService.createManager(userId);
    }

    // GET manager by id
    @GetMapping("/{id}")
    public ManagerResponseDTO getManagerById(@PathVariable Long id) {

        return managerService.getManagerById(id);
    }

    // GET all managers
    @GetMapping
    public List<ManagerResponseDTO> getAllManagers() {

        return managerService.getAllManagers();
    }

    // GET manager by user id
    @GetMapping("/user/{userId}")
    public ManagerResponseDTO getManagerByUserId(@PathVariable Long userId) {
        return managerService.getManagerByUserId(userId);
    }

    // GET associated consultants
    @GetMapping("/{id}/consultants")
    public List<?> getConsultantsForManager(@PathVariable Long id) {

        return managerService.getConsultantsForManager(id);
    }

    // GET associated customers
    @GetMapping("/{id}/customers")
    public List<?> getCustomersForManager(@PathVariable Long id) {

        return managerService.getCustomersForManager(id);
    }

    // UPDATE change associated user to this manager
    @PutMapping("/{id}/user/{newUserId}")
    public ManagerResponseDTO updateManagerUser(@PathVariable Long id, @PathVariable Long newUserId) {
        return managerService.updateManagerUser(id, newUserId);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
    }
}


