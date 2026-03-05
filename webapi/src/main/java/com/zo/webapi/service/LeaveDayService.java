package com.zo.webapi.service;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.LeaveDay;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.LeaveDayRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveDayService {

    @Autowired
    private LeaveDayRepository leaveDayRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    public LeaveDay addLeaveDay(Long consultantId, LeaveDay leaveDay) {

        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new EntityNotFoundException("Consultant not found"));

        checkDateNotInPast(leaveDay.getDate());
        checkDuplicateDate(consultantId, leaveDay.getDate(), null);

        leaveDay.setConsultantId(consultant.getId());

        return leaveDayRepository.save(leaveDay);
    }

    public List<LeaveDay> getLeaveDays(Long consultantId) {
        return leaveDayRepository.findByConsultantId(consultantId);
    }

    public void deleteLeaveDay(Long id) {
        leaveDayRepository.deleteById(id);
    }

    public LeaveDay updateLeaveDay(Long id, LeaveDay updatedLeaveDay) {
        LeaveDay existing = leaveDayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LeaveDay not found"));

        checkDateNotInPast(updatedLeaveDay.getDate());
        checkDuplicateDate(existing.getConsultantId(), updatedLeaveDay.getDate(), id);

        existing.setDate(updatedLeaveDay.getDate());
        existing.setReason(updatedLeaveDay.getReason());

        return leaveDayRepository.save(existing);
    }

    // ===================
    // Private helper methods
    // ===================

    private void checkDateNotInPast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Leave day cannot be in the past");
        }
    }

    private void checkDuplicateDate(Long consultantId, LocalDate date, Long leaveDayId) {
        List<LeaveDay> leaveDays = leaveDayRepository.findByConsultantId(consultantId);
        for (LeaveDay ld : leaveDays) {
            if (ld.getDate().equals(date) && (leaveDayId == null || !ld.getId().equals(leaveDayId))) {
                throw new IllegalArgumentException("Consultant already has a leave day on this date");
            }
        }
    }
}