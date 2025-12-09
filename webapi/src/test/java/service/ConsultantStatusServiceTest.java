package com.zo.webapi.service;

import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ConsultantStatusRepository;
import com.zo.webapi.enums.ConsultantStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

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
    private ConsultantStatusDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        consultant = new Consultant();
        consultant.setId(1L);

        dto =  new ConsultantStatusDTO();
        dto.setConsultantId(1L);
        dto.setStatus(ConsultantStatusType.AVAILABLE);
        dto.setDateStart(LocalDate.now());
        dto.setDateEnd(LocalDate.now().plusDays(5));
        dto.setComment("Test comment");

        status = new ConsultantStatus();
        status.setId(10L);
        status.setConsultant(consultant);
        status.setStatus(dto.getStatus());
        status.setDateStart(dto.getDateStart());
        status.setDateEnd(dto.getDateEnd());
        status.setComment(dto.getComment());

    }

    @Test
    void testCreateStatus_Success() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));
        when(statusRepository.save(any())).thenReturn(status);

        ConsultantStatus result = consultantStatusService.createStatus(dto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(statusRepository, times(1)).save(any());

    }

    @Test
    void testCreateStatus_ConsultantNotFound() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () ->  consultantStatusService.createStatus(dto));

    }

    @Test
    void testUpdateStatus_Success() {
        when(statusRepository.findById(10L)).thenReturn(Optional.of(status));
        when(statusRepository.save(any())).thenReturn(status);

        ConsultantStatus result = consultantStatusService.updateStatus(10L, dto);

        assertNotNull(result);
        verify(statusRepository).save(status);

    }


    @Test
    void testUpdateStatus_NotFound() {
        when(statusRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.updateStatus(10L, dto));


    }


    @Test
    void testGetStatusById_Success() {
        when(statusRepository.findById(10L)).thenReturn(Optional.of(status));

        ConsultantStatus result = consultantStatusService.getStatusById(10L);

        assertEquals(status, result);

    }

    @Test
    void testGetStatusById_NotFound() {
        when(statusRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.getStatusById(10L));
    }



    @Test
    void testGetStatusesByConsultant_Success() {
        when(statusRepository.findByConsultantId(1L))
            .thenReturn(List.of(status));

        List<ConsultantStatus> result = consultantStatusService.getStatusesByConsultant(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetStatusesByConsultant_NotFound() {
        when(statusRepository.findByConsultantId(1L))
            .thenReturn(List.of());

        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.getStatusesByConsultant(1L));

    }

    @Test
    void testGetAllStatuses_Success() {
        when(statusRepository.findAll()).thenReturn(List.of(status));

        List<ConsultantStatus> result = consultantStatusService.getAllStatuses();

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllStatuses_EmptyListAllowed() {
        when(statusRepository.findAll()).thenReturn(List.of());

        List<ConsultantStatus> result = consultantStatusService.getAllStatuses();

        assertTrue(result.isEmpty());

    }


    @Test
    void testDeleteStatusById_Success() {
        when(statusRepository.findById(10L)).thenReturn(Optional.of(status));

        consultantStatusService.deleteStatus(10L);

        verify(statusRepository, times(1)).delete(status);
    }

    @Test
    void testDeleteStatusById_NotFound() {
        when(statusRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> consultantStatusService.deleteStatus(10L));
    }
}


