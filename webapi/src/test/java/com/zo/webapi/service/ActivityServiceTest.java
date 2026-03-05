package com.zo.webapi.service;

import com.zo.webapi.dto.ActivityCreateRequestDTO;
import com.zo.webapi.dto.ActivityResponseDTO;
import com.zo.webapi.dto.ActivityUpdateRequestDTO;
import com.zo.webapi.enums.ActivityType;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Activity;
import com.zo.webapi.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    private ActivityCreateRequestDTO validCreateDto;

    @BeforeEach
    void setUp() {
        validCreateDto = new ActivityCreateRequestDTO(
                "Möte",
                ActivityType.MEETING,
                "2026-02-10",
                "09:00",
                "10:00",
                "Beskrivning"
        );
    }

    @Test
    void createActivity_Success() {

        when(activityRepository.save(any(Activity.class))).thenAnswer(inv -> {
            Activity a = inv.getArgument(0);
            a.setId(1L);
            a.setCreatedAt(OffsetDateTime.parse("2026-02-01T10:15:30+01:00"));
            return a;
        });

        ActivityResponseDTO resp = activityService.createActivity(validCreateDto);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("Möte", resp.getTitle());
        assertEquals(ActivityType.MEETING, resp.getType());
        assertEquals("2026-02-10", resp.getDate());
        assertEquals("09:00", resp.getStartTime());
        assertEquals("10:00", resp.getEndTime());
        assertEquals("Beskrivning", resp.getDescription());
        assertTrue(resp.getCreatedAt().startsWith("2026-02-01T10:15:30+01:00"));

        ArgumentCaptor<Activity> captor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository).save(captor.capture());

        Activity saved = captor.getValue();
        assertEquals("Möte", saved.getTitle());
        assertEquals(ActivityType.MEETING, saved.getType());
        assertEquals(LocalDate.of(2026, 2, 10), saved.getDate());
        assertEquals(LocalTime.of(9, 0), saved.getStartTime());
        assertEquals(LocalTime.of(10, 0), saved.getEndTime());
        assertEquals("Beskrivning", saved.getDescription());

        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void createActivity_InvalidDateFormat_ThrowsInvalidData() {
        validCreateDto.setDate("10-02-2026"); // Fel format

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> activityService.createActivity(validCreateDto));

        assertTrue(exc.getMessage().toLowerCase().contains("invalid date"));
        verifyNoInteractions(activityRepository);
    }

    @Test
    void createActivity_InvalidTimeFormat_ThrowsInvalidData() {
        validCreateDto.setStartTime("9:00"); // Fel format, ska vara HH:mm

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> activityService.createActivity(validCreateDto));

        assertTrue(exc.getMessage().toLowerCase().contains("starttime"));
        verifyNoInteractions(activityRepository);
    }

    @Test
    void createActivity_StartTimeNotBeforeEndTime_ThrowsInvalidData() {
        validCreateDto.setStartTime("10:00");
        validCreateDto.setEndTime("10:00");

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> activityService.createActivity(validCreateDto));

        assertTrue(exc.getMessage().toLowerCase().contains("starttime must be before endtime"));
        verifyNoInteractions(activityRepository);
    }

    @Test
    void getAllActivities_NoParams_ReturnsAllOrdered() {
        Activity a1 = activity(1L, "A", "2026-01-01", "09:00", "10:00");
        Activity a2 = activity(2L, "B", "2026-01-02", "09:00", "10:00");

        when(activityRepository.findAllByOrderByDateAscStartTimeAsc()).thenReturn(List.of(a1, a2));

        List<ActivityResponseDTO> result = activityService.getAllActivities(null, null);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(activityRepository).findAllByOrderByDateAscStartTimeAsc();
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void getAllActivities_FromToProvided_ReturnsBetweenOrdered() {
        Activity a1 = activity(1L, "A", "2026-01-10", "09:00", "10:00");

        when(activityRepository.findByDateBetweenOrderByDateAscStartTimeAsc(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31))).thenReturn(List.of(a1));

        List<ActivityResponseDTO> result = activityService.getAllActivities("2026-01-01", "2026-01-31");

        assertEquals(1, result.size());
        assertEquals("2026-01-10", result.get(0).getDate());

        verify(activityRepository).findByDateBetweenOrderByDateAscStartTimeAsc(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
        );
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void getAllActivities_OnlyFromProvided_ThrowsInvalidData() {
        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> activityService.getAllActivities("2026-01-01", null));

        assertTrue(exc.getMessage().toLowerCase().contains("both 'from' and 'to'"));
        verifyNoInteractions(activityRepository);
    }

    @Test
    void getAllActivities_FromAfterTo_ThrowsInvalidData() {
        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> activityService.getAllActivities("2026-02-01", "2026-01-01"));

        assertTrue(exc.getMessage().toLowerCase().contains("from"));
        verifyNoInteractions(activityRepository);
    }

    @Test
    void updateActivity_Success_PartialUpdate() {
        Activity existing = activity(10L, "Befintlig title", "2026-01-01", "09:00", "10:00");
        existing.setType(ActivityType.OTHER);
        existing.setDescription("Gammal beskrivning");

        when(activityRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(activityRepository.save(any(Activity.class))).thenAnswer(inv -> inv.getArgument(0));

        ActivityUpdateRequestDTO dto = new ActivityUpdateRequestDTO();
        dto.setTitle("Ny");
        dto.setStartTime("08:30");

        ActivityResponseDTO resp = activityService.updateActivity(10L, dto);

        assertEquals(10L, resp.getId());
        assertEquals("Ny", resp.getTitle());
        assertEquals("08:30", resp.getStartTime()); // Oförändrad
        assertEquals("10:00", resp.getEndTime());   // Oförändrad
        assertEquals("2026-01-01", resp.getDate()); // Oförändrad

        verify(activityRepository).findById(10L);
        verify(activityRepository).save(existing);
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void updateActivity_NotFound_ThrowsResourceNotFound() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> activityService.updateActivity(99L, new ActivityUpdateRequestDTO()));

        verify(activityRepository).findById(99L);
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void updateActivity_InvalidTimeRange_ThrowsInvalidData() {
        Activity existing = activity(10L, "Befintlig title", "2026-01-01", "09:00", "10:00");
        when(activityRepository.findById(10L)).thenReturn(Optional.of(existing));

        ActivityUpdateRequestDTO dto = new ActivityUpdateRequestDTO();
        dto.setStartTime("11:00"); // Gör tidsintervall ogiltig då start är före end

        InvalidDataException exc = assertThrows(InvalidDataException.class,
                () -> activityService.updateActivity(10L, dto));

        assertTrue(exc.getMessage().toLowerCase().contains("starttime must be before endtime"));

        verify(activityRepository).findById(10L);
        verify(activityRepository, never()).save(any());
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void deleteActivity_Success() {
        when(activityRepository.existsById(1L)).thenReturn(true);

        activityService.deleteActivity(1L);

        verify(activityRepository).existsById(1L);
        verify(activityRepository).deleteById(1L);
        verifyNoMoreInteractions(activityRepository);
    }

    @Test
    void deleteActivity_NotFound_ThrowsResourceNotFound() {
        when(activityRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> activityService.deleteActivity(999L));

        verify(activityRepository).existsById(999L);
        verify(activityRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(activityRepository);
    }

    // Helper
    private Activity activity(Long id, String title, String date, String start, String end) {
        Activity a = new Activity();
        a.setId(id);
        a.setTitle(title);
        a.setType(ActivityType.MEETING);
        a.setDate(LocalDate.parse(date));
        a.setStartTime(LocalTime.parse(start));
        a.setEndTime(LocalTime.parse(end));
        a.setCreatedAt(OffsetDateTime.parse("2026-02-01T10:15:30+01:00"));
        return a;
    }
}
