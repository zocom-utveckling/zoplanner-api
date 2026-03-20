package com.zo.webapi.service;

import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.LeaveDay;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.LeaveDayRepository;
import com.zo.webapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LeaveDayServiceIntegrationTest {

    @Autowired
    private LeaveDayService leaveDayService;

    @Autowired
    private LeaveDayRepository leaveDayRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private UserRepository userRepository;

    private Consultant consultant;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("testuser" + System.currentTimeMillis());
        user.setPassword("password123");
        user.setName("Test User");
        user.setEmail("test" + System.currentTimeMillis() + "@test.se");
        user.setCity("Stockholm");
        user.setRole(UserRole.CONSULTANT);
        user = userRepository.save(user);

        consultant = new Consultant();
        consultant.setUser(user);
        consultant = consultantRepository.save(consultant);
    }

    // =====================
    // addLeaveDay
    // =====================

    @Test
    void addLeaveDay_success_savedToDatabase() {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Vacation");

        LeaveDay result = leaveDayService.addLeaveDay(consultant.getId(), leaveDay);

        assertNotNull(result.getId());
        assertEquals("Vacation", result.getReason());
        assertEquals(consultant.getId(), result.getConsultantId());
    }

    @Test
    void addLeaveDay_dateInPast_throwsException() {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setDate(LocalDate.now().minusDays(1));
        leaveDay.setReason("Vacation");

        assertThrows(IllegalArgumentException.class,
                () -> leaveDayService.addLeaveDay(consultant.getId(), leaveDay));
    }

    @Test
    void addLeaveDay_duplicateDate_throwsException() {
        LeaveDay first = new LeaveDay();
        first.setDate(LocalDate.now().plusDays(5));
        first.setReason("Vacation");
        leaveDayService.addLeaveDay(consultant.getId(), first);

        LeaveDay duplicate = new LeaveDay();
        duplicate.setDate(LocalDate.now().plusDays(5));
        duplicate.setReason("Other reason");

        assertThrows(IllegalArgumentException.class,
                () -> leaveDayService.addLeaveDay(consultant.getId(), duplicate));
    }

    @Test
    void addLeaveDay_consultantNotFound_throwsException() {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setDate(LocalDate.now().plusDays(5));

        assertThrows(EntityNotFoundException.class,
                () -> leaveDayService.addLeaveDay(999L, leaveDay));
    }

    // =====================
    // getLeaveDays
    // =====================

    @Test
    void getLeaveDays_returnsCorrectLeaveDays() {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Vacation");
        leaveDayService.addLeaveDay(consultant.getId(), leaveDay);

        List<LeaveDay> result = leaveDayService.getLeaveDays(consultant.getId());

        assertEquals(1, result.size());
        assertEquals("Vacation", result.get(0).getReason());
    }

    @Test
    void getLeaveDays_emptyList_whenNoLeaveDays() {
        List<LeaveDay> result = leaveDayService.getLeaveDays(consultant.getId());
        assertTrue(result.isEmpty());
    }

    // =====================
    // deleteLeaveDay
    // =====================

    @Test
    void deleteLeaveDay_removedFromDatabase() {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Vacation");
        LeaveDay saved = leaveDayService.addLeaveDay(consultant.getId(), leaveDay);

        leaveDayService.deleteLeaveDay(saved.getId());

        List<LeaveDay> result = leaveDayService.getLeaveDays(consultant.getId());
        assertTrue(result.isEmpty());
    }

    // =====================
    // updateLeaveDay
    // =====================

    @Test
    void updateLeaveDay_updatesDateAndReason() {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Vacation");
        LeaveDay saved = leaveDayService.addLeaveDay(consultant.getId(), leaveDay);

        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().plusDays(10));
        updated.setReason("Sick");

        LeaveDay result = leaveDayService.updateLeaveDay(saved.getId(), updated);

        assertEquals(LocalDate.now().plusDays(10), result.getDate());
        assertEquals("Sick", result.getReason());
    }

    @Test
    void updateLeaveDay_dateInPast_throwsException() {
        LeaveDay leaveDay = new LeaveDay();
        leaveDay.setDate(LocalDate.now().plusDays(5));
        leaveDay.setReason("Vacation");
        LeaveDay saved = leaveDayService.addLeaveDay(consultant.getId(), leaveDay);

        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().minusDays(1));

        assertThrows(IllegalArgumentException.class,
                () -> leaveDayService.updateLeaveDay(saved.getId(), updated));
    }

    @Test
    void updateLeaveDay_notFound_throwsException() {
        LeaveDay updated = new LeaveDay();
        updated.setDate(LocalDate.now().plusDays(5));

        assertThrows(EntityNotFoundException.class,
                () -> leaveDayService.updateLeaveDay(999L, updated));
    }
}