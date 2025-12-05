/*package com.zo.webapi.service;

import com.zo.webapi.enums.ConsultantStatusType;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import com.zo.webapi.service.ConsultantStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zo.webapi.enums.ConsultantStatusType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultantStatusServiceTest {

    @Mock
    private ConsultantStatusRepository statusRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @InjectMocks
    private ConsultantStatusService consultantStatusService;

    private Consultant consultant;
    private ConsultantStatus status;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        consultant = new Consultant();
        consultant.setId(1L);

        status = new ConsultantStatus();
        status.setId(10L);
        status.setConsultant(consultant);
        status.setStatus(ConsultantStatusType.AVAILABLE);
        status.setDateStart(LocalDate.of(2025, 1, 1));
        status.setDateEnd(LocalDate.of(2025, 1, 31));
        status.setComment("Ready for work");
    }

    @Test
    void testCreateStatus_Success() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));
        when(statusRepository.save(any())).thenReturn(status);

        ConsultantStatus result = consultantStatusService.createStatus(
                1L,
                ConsultantStatusType.AVAILABLE,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 31),
                "Ready for work"
        );

        assertNotNull(result);
        assertEquals(ConsultantStatusType.AVAILABLE, result.getStatus());
        verify(statusRepository, times(1)).save(any());
    }

    @Test
    void testCreateStatus_ConsultantNotFound() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                consultantStatusService.createStatus(
                        1L,
                        ConsultantStatusType.AVAILABLE,
                        LocalDate.now(),
                        LocalDate.now(),
                        "test"
                )
        );

        assertTrue(ex.getMessage().contains("Consultant not found with id"));
        verify(statusRepository, never()).save(any());
    }

    @Test
    void testGetStatusesByConsultant() {
        when(statusRepository.findByConsultantId(1L))
                .thenReturn(Arrays.asList(status));

        List<ConsultantStatus> list = consultantStatusService.getStatusesByConsultant(1L);

        assertEquals(1, list.size());
        assertEquals(ConsultantStatusType.AVAILABLE, list.get(0).getStatus());
        verify(statusRepository, times(1)).findByConsultantId(1L);
    }
}

 */
