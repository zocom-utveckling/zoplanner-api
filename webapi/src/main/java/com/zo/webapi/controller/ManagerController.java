package com.zo.webapi.controller;

import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.ConsultantResponseDTO;
import com.zo.webapi.dto.CustomerDTO;
import com.zo.webapi.dto.ManagerResponseDTO;
import com.zo.webapi.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ManagerResponseDTO> createManager(@RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(managerService.createManager(userId));

        //return managerService.createManager(userId);
    }

    // GET manager by id
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> getManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManagerById(id));
    }

    // GET all managers
    @GetMapping
    public ResponseEntity<List<ManagerResponseDTO>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    // GET manager by user id
    @GetMapping("/user/{userId}")
    public ResponseEntity<ManagerResponseDTO> getManagerByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(managerService.getManagerByUserId(userId));
    }

    // GET associated consultants
    @GetMapping("/{id}/consultants")
    public ResponseEntity<List<ConsultantResponseDTO>> getConsultantsForManager(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getConsultantsForManager(id));
    }

    // GET associated customers
    @GetMapping("/{id}/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomersForManager(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getCustomersForManager(id));
    }

    // ASSIGN consultant to manager
    @PutMapping("/{managerId}/consultants/{consultantId}")
    public ResponseEntity<Void> assignConsultant(@PathVariable Long managerId,
                                                 @PathVariable Long consultantId) {
        managerService.assignConsultantToManager(managerId, consultantId);
        return ResponseEntity.ok().build(); // 200 OK
    }

    // ASSIGN customer to manager
    @PutMapping("/{managerId}/customers/{customerId}")
    public ResponseEntity<Void> assignCustomer(@PathVariable Long managerId,
                                               @PathVariable Long customerId) {
        managerService.assignCustomerToManager(managerId, customerId);
        return ResponseEntity.ok().build(); // 200 OK
    }

    // REMOVE consultant from manager
    @DeleteMapping("/{managerId}/consultants/{consultantId}")
    public ResponseEntity<Void> removeConsultant(@PathVariable Long managerId,
                                                 @PathVariable Long consultantId) {
        managerService.removeConsultantFromManager(managerId, consultantId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // REMOVE customer from manager
    @DeleteMapping("/{managerId}/customers/{customerId}")
    public ResponseEntity<Void> removeCustomer(@PathVariable Long managerId,
                                               @PathVariable Long customerId) {
        managerService.removeCustomerFromManager(managerId, customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // UPDATE change associated user to this manager
    @PutMapping("/{id}/user/{newUserId}")
    public ResponseEntity<ManagerResponseDTO> updateManagerUser(@PathVariable Long id,
                                                                @PathVariable Long newUserId) {
        return ResponseEntity.ok(managerService.updateManagerUser(id, newUserId));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}


