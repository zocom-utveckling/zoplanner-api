package com.zo.webapi.controller;

import com.zo.webapi.model.LeaveDay;
import com.zo.webapi.service.LeaveDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultants")
public class LeaveDayController {

    @Autowired
    private LeaveDayService leaveDayService;

    @PostMapping("/{id}/leave-days")
    public LeaveDay addLeaveDay(@PathVariable Long id,
                                @RequestBody LeaveDay leaveDay) {
        return leaveDayService.addLeaveDay(id, leaveDay);
    }

    @GetMapping("/{id}/leave-days")
    public List<LeaveDay> getLeaveDays(@PathVariable Long id) {
        return leaveDayService.getLeaveDays(id);
    }

    @DeleteMapping("/leave-days/{leaveDayId}")
    public void deleteLeaveDay(@PathVariable Long leaveDayId) {
        leaveDayService.deleteLeaveDay(leaveDayId);
    }

    // =====================
    // Update leave day
    // =====================
    @PutMapping("/leave-days/{leaveDayId}")
    public ResponseEntity<?> updateLeaveDay(
            @PathVariable Long leaveDayId,
            @RequestBody LeaveDay leaveDay
    ) {
        try {
            LeaveDay updated = leaveDayService.updateLeaveDay(leaveDayId, leaveDay);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }
}