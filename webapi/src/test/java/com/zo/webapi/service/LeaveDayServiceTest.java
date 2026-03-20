package com.zo.webapi.service;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.LeaveDay;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.LeaveDayRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveDayServiceTest {

    @Mock
    private LeaveDayRepository leaveDayRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @InjectMocks
    private LeaveDayService leaveDayService;

    private Consultant consultant;
    private LeaveDay leaveDay;

    @BeforeEach
    void setUp() {
        consultant = new Consultant();
        consultant.setId(1L);

        leaveDay = new LeaveDay();
        leaveDay.setId(1L);
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Semester");
        leaveDay.setConsultantId(1L);
    }

    // =====================
    // addLeaveDay
    // =====================

    @Test
    void addLeaveDay_success() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));
        when(leaveDayRepository.findByConsultantId(1L)).thenReturn(List.of());
        when(leaveDayRepository.save(leaveDay)).thenReturn(leaveDay);

        LeaveDay result = leaveDayService.addLeaveDay(1L, leaveDay);

        assertNotNull(result);
        assertEquals(1L, result.getConsultantId());
        verify(leaveDayRepository).save(leaveDay);
    }

    @Test
    void addLeaveDay_consultantNotFound_throwsException() {
        when(consultantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> leaveDayService.addLeaveDay(99L, leaveDay));
    }

    @Test
    void addLeaveDay_dateInPast_throwsException() {
        leaveDay.setDate(LocalDate.now().minusDays(1));
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));

        assertThrows(IllegalArgumentException.class,
                () -> leaveDayService.addLeaveDay(1L, leaveDay));
    }

    @Test
    void addLeaveDay_duplicateDate_throwsException() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));
        when(leaveDayRepository.findByConsultantId(1L)).thenReturn(List.of(leaveDay));

        LeaveDay duplicate = new LeaveDay();
        duplicate.setDate(leaveDay.getDate());

        assertThrows(IllegalArgumentException.class,
                () -> leaveDayService.addLeaveDay(1L, duplicate));
    }

    // =====================
    // getLeaveDays
    // =====================

    @Test
    void getLeaveDays_returnsListForConsultant() {
        when(leaveDayRepository.findByConsultantId(1L)).thenReturn(List.of(leaveDay));

        List<LeaveDay> result = leaveDayService.getLeaveDays(1L);

        assertEquals(1, result.size());
        assertEquals(leaveDay.getDate(), result.get(0).getDate());
    }

    // =====================
    // deleteLeaveDay
    // =====================

    @Test
    void deleteLeaveDay_callsRepository() {
        leaveDayService.deleteLeaveDay(1L);
        verify(leaveDayRepository).deleteById(1L);
    }

    // =====================
    // updateLeaveDay
    // =====================

    @Test
    void updateLeaveDay_success() {
        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().plusDays(10));
        updated.setReason("Sjuk");

        when(leaveDayRepository.findById(1L)).thenReturn(Optional.of(leaveDay));
        when(leaveDayRepository.findByConsultantId(1L)).thenReturn(List.of(leaveDay));
        when(leaveDayRepository.save(any())).thenReturn(leaveDay);

        LeaveDay result = leaveDayService.updateLeaveDay(1L, updated);

        assertNotNull(result);
        verify(leaveDayRepository).save(leaveDay);
    }

    @Test
    void updateLeaveDay_notFound_throwsException() {
        when(leaveDayRepository.findById(99L)).thenReturn(Optional.empty());

        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().plusDays(5));

        assertThrows(EntityNotFoundException.class,
                () -> leaveDayService.updateLeaveDay(99L, updated));
    }

    @Test
    void updateLeaveDay_dateInPast_throwsException() {
        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().minusDays(1));

        when(leaveDayRepository.findById(1L)).thenReturn(Optional.of(leaveDay));

        assertThrows(IllegalArgumentException.class,
                () -> leaveDayService.updateLeaveDay(1L, updated));
    }
}
