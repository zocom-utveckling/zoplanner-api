package com.zo.webapi.service;

import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.ConsultantResponseDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.mapper.ConsultantMapper;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.ConsultantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultantServiceTest {
    @Mock
    private ConsultantRepository consultantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ConsultantService consultantService;

    @Test
    void testGetAllConsultants() {
        Consultant consultant = new Consultant();
        consultant.setId(1L);
        User consultantUser = new User();
        consultant.setUser(consultantUser);

        Consultant consultant2 = new Consultant();
        consultant2.setId(2L);
        User consultantUser2 = new User();
        consultant2.setUser(consultantUser2);

        when(consultantRepository.findAll()).thenReturn(List.of(consultant, consultant2));

        List<ConsultantResponseDTO> result = consultantService.getAllConsultants();

        assertEquals(2, result.size());
    }


    @Test
    void testGetConsultantById_Success() {
        Consultant consultant = new Consultant();
        consultant.setId(1L);
        consultant.setUser(new User());
        consultant.getUser().setId(10L);
        consultant.getUser().setCity("London");

        when(consultantRepository.findById(1L)).thenReturn(Optional.of(consultant));

        ConsultantResponseDTO result = consultantService.getConsultantById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("London", result.getCity());
        assertEquals(10L, result.getUserId());
    }


    @Test
    void testCreateConsultant_Success() {
        ConsultantDTO dto = new ConsultantDTO(null, 10L, null);
        User user = new User();
        user.setId(10L);
        user.setCity("London");

        when(userRepository.findById(10L)).thenReturn(Optional.empty());
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(consultantRepository.save(any(Consultant.class)))
                .thenAnswer(invocation -> {
                    Consultant c = invocation.getArgument(0);
                    c.setId(1L);
                    return c;
                });

        ConsultantResponseDTO result = consultantService.createConsultant(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("London", result.getCity());
        assertEquals(10L, result.getUserId());
    }

    @Test
    void testGetConsultantByUserId_Success() {
        Consultant consultant = new Consultant();
        consultant.setId(1L);
        consultant.setUser(new User());
        consultant.getUser().setId(10L);

        when(consultantRepository.findByUserId(10L)).thenReturn(Optional.of(consultant));

        ConsultantResponseDTO result = consultantService.getConsultantByUserId(10L);
        assertEquals(10L, result.getUserId());
    }


    @Test
    void testGetConsultantByManagerId_Success() {
        Consultant consultant = new Consultant();
        consultant.setId(1L);
        consultant.setUser(new User());
        consultant.getUser().setId(1L);
        when(managerRepository.existsById(1L)).thenReturn(true);
        when(consultantRepository.findByManagerId(1L)).thenReturn(List.of(consultant));

        List<ConsultantResponseDTO> result = consultantService.getConsultantsByManagerId(1L);
        assertEquals(1, result.size());
    }



    @Test
    void testUpdateConsultant_Success() {
        User consultantUser = new User(1L, "fredrik", "password123", "fredrik", "fredrik@mail.se", "Bjärred", UserRole.CONSULTANT);
        Consultant existing = new Consultant();
        existing.setUser(consultantUser);
        existing.setId(1L);

        Manager manager = new Manager();
        manager.setId(2L);

        ConsultantDTO dto = new ConsultantDTO(1L, 1L, 2L);

        when(consultantRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(managerRepository.findById(2L)).thenReturn(Optional.of(manager));
        when(consultantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ConsultantResponseDTO result = consultantService.updateConsultant(1L, dto);
        System.out.println(result);
        assertEquals(1L, result.getId());
        assertEquals(2L, result.getManagerId());
    }

    @Test
    void testDeleteConsultantById_Success() {
        when(consultantRepository.existsById(1L)).thenReturn(true);
        consultantService.deleteConsultant(1L);
        verify(consultantRepository, times(1)).deleteById(1L);
    }



    @Test
    void testGetConsultantById_NotFound() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> consultantService.getConsultantById(1L));
    }

    @Test
    void testGetConsultantByUserId_NotFound() {
        when(consultantRepository.findByUserId(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> consultantService.getConsultantByUserId(10L));
    }

    @Test
    void testGetConsultantByManagerId_NotFound() {
        when(managerRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> consultantService.getConsultantsByManagerId(1L));
    }

    @Test
    void testCreateConsultant_UserAlreadyExists() {
        ConsultantDTO dto = new ConsultantDTO(null, 10L, null);
        when(consultantRepository.findByUserId(10L)).thenReturn(Optional.of(new Consultant()));
        assertThrows(InvalidDataException.class, () -> consultantService.createConsultant(dto));
    }


    @Test
    void testUpdateConsultant_NotFound() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.empty());

        ConsultantDTO dto = new ConsultantDTO(null, null, null);
        assertThrows(ResourceNotFoundException.class, () -> consultantService.updateConsultant(1L, dto));
    }

    @Test
    void testDeleteConsultantById_NotFound() {
        when(consultantRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () ->
                consultantService.deleteConsultant(1L));
    }
}
