package com.zo.webapi.service;

import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.dto.UpdateClassGroupRequestDTO;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CustomerRepository;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassGroupServiceTest {
    @Mock
    private ClassGroupRepository classGroupRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ClassGroupService classGroupService;

    private Customer sampleCustomer;
    private ClassGroup sampleClass;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("Customer A");

        sampleClass = new ClassGroup();
        sampleClass.setId(10L);
        sampleClass.setName("Class A");
        sampleClass.setCustomer(sampleCustomer);
    }

    @Test
    void testGetAllClasses() {
        when(classGroupRepository.findAll()).thenReturn(List.of(sampleClass));

        List<ClassGroupResponseDTO> classes = classGroupService.getAllClasses();

        assertEquals(1, classes.size());
        ClassGroupResponseDTO dto = classes.get(0);
        assertEquals(10L, dto.getId());
        assertEquals("Class A", dto.getName());
        assertEquals(1L, dto.getCustomerId());
        assertEquals("Customer A", dto.getCustomerName());

        verify(classGroupRepository).findAll();
        verifyNoMoreInteractions(classGroupRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    void testGetClassById_Success() {
        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(sampleClass));

        ClassGroupResponseDTO responseDTO = classGroupService.getClassById(10L);

        assertEquals(10L, responseDTO.getId());
        assertEquals("Class A", responseDTO.getName());
        assertEquals(1L, responseDTO.getCustomerId());
        assertEquals("Customer A", responseDTO.getCustomerName());

        verify(classGroupRepository).findById(10L);
        verifyNoMoreInteractions(classGroupRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    void testGetClassById_NotFound() {
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> classGroupService.getClassById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().contains("Class not found with id: 99"));

        verify(classGroupRepository).findById(99L);
        verifyNoMoreInteractions(classGroupRepository);
        verifyNoInteractions(customerRepository);
    }


    @Test
    void testCreateClass_Success() {
        CreateClassGroupRequestDTO requestDTO = new CreateClassGroupRequestDTO("Class A", 1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(classGroupRepository.existsByNameAndCustomerId("Class A", 1L)).thenReturn(false);
        when(classGroupRepository.save(any(ClassGroup.class))).thenReturn(sampleClass);

        ClassGroupResponseDTO responseDTO = classGroupService.createClass(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(10L, responseDTO.getId());
        assertEquals("Class A", responseDTO.getName());
        assertEquals(1L, responseDTO.getCustomerId());
        assertEquals("Customer A", responseDTO.getCustomerName());

        ArgumentCaptor<ClassGroup> captor = ArgumentCaptor.forClass(ClassGroup.class);
        verify(classGroupRepository).save(captor.capture());
        verify(customerRepository).findById(1L);
        verify(classGroupRepository).existsByNameAndCustomerId("Class A", 1L);

        ClassGroup saved = captor.getValue();
        assertEquals("Class A", saved.getName());
        assertSame(sampleCustomer, saved.getCustomer());

        verifyNoMoreInteractions(classGroupRepository, customerRepository);
    }

    @Test
    void testCreateClass_CustomerNotFound() {
        CreateClassGroupRequestDTO requestDTO = new CreateClassGroupRequestDTO("Test Class", 99L);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex =  assertThrows(ResponseStatusException.class,
                () -> classGroupService.createClass(requestDTO));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("Customer not found with id: 99"));

        verify(customerRepository).findById(99L);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testDeleteClass_Success() {
        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(sampleClass));

        assertDoesNotThrow(() -> classGroupService.deleteClass(10L));

        verify(classGroupRepository).findById(10L);
        verify(classGroupRepository).delete(sampleClass);
        verifyNoMoreInteractions(classGroupRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    void testDeleteClass_NotFound() {
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex =  assertThrows(ResponseStatusException.class,
                () -> classGroupService.deleteClass(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("Class not found with id: 99"));

        verify(classGroupRepository).findById(99L);
        verify(classGroupRepository, never()).delete(any());
        verifyNoMoreInteractions(classGroupRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    void testUpdateClass_Success() {
        UpdateClassGroupRequestDTO requestDTO = new UpdateClassGroupRequestDTO("App class");

        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(sampleClass));
        when(classGroupRepository.save(any(ClassGroup.class))).thenReturn(sampleClass);

        ClassGroupResponseDTO responseDTO = classGroupService.updateClass(10L, requestDTO);

        assertEquals(10L, responseDTO.getId());
        assertEquals("App class", responseDTO.getName());
        assertEquals(1L, responseDTO.getCustomerId());
        assertEquals("Customer A", responseDTO.getCustomerName());

        ArgumentCaptor<ClassGroup> captor = ArgumentCaptor.forClass(ClassGroup.class);
        verify(classGroupRepository).findById(10L);
        verify(classGroupRepository).save(captor.capture());

        assertEquals("App class", captor.getValue().getName());
        assertSame(sampleCustomer, captor.getValue().getCustomer());

        verifyNoMoreInteractions(classGroupRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    void testCreateClass_DuplicateNameForCustomer_Conflict() {
        CreateClassGroupRequestDTO requestDTO = new CreateClassGroupRequestDTO("Class A", 1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(classGroupRepository.existsByNameAndCustomerId("Class A", 1L)).thenReturn(true);

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.createClass(requestDTO));

        assertEquals(HttpStatus.CONFLICT, exc.getStatusCode());
        assertNotNull(exc.getReason());
        assertTrue(exc.getReason().contains("Class name already exists for this customer"));

        verify(customerRepository).findById(1L);
        verify(classGroupRepository).existsByNameAndCustomerId("Class A", 1L);
        verify(classGroupRepository, never()).save(any());
        verifyNoMoreInteractions(classGroupRepository, customerRepository);
    }

    @Test
    void testGetClassesByCustomerId_CustomerNotFound() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.getClassesByCustomerId(99L));

        assertEquals(HttpStatus.NOT_FOUND, exc.getStatusCode());
        assertNotNull(exc.getReason());
        assertTrue(exc.getReason().contains("Customer not found with id: 99"));

        verify(customerRepository).existsById(99L);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(classGroupRepository);
    }

    @Test
    void testGetClassesByCustomerId_Success() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(classGroupRepository.findByCustomerId(1L)).thenReturn(List.of(sampleClass));

        List<ClassGroupResponseDTO> result = classGroupService.getClassesByCustomerId(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals("Class A", result.get(0).getName());
        assertEquals(1L, result.get(0).getCustomerId());
        assertEquals("Customer A", result.get(0).getCustomerName());

        verify(customerRepository).existsById(1L);
        verify(classGroupRepository).findByCustomerId(1L);
        verifyNoMoreInteractions(customerRepository, classGroupRepository);
    }

    @Test
    void testUpdateClass_NotFound() {
        UpdateClassGroupRequestDTO requestDTO = new UpdateClassGroupRequestDTO("New Name");
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.updateClass(99L, requestDTO));

        assertEquals(HttpStatus.NOT_FOUND, exc.getStatusCode());
        assertNotNull(exc.getReason());
        assertTrue(exc.getReason().contains("Class not found with id: 99"));

        verify(classGroupRepository).findById(99L);
        verify(classGroupRepository, never()).save(any());
        verifyNoMoreInteractions(classGroupRepository);
        verifyNoInteractions(customerRepository);

    }
}

