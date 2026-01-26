package service;

import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.dto.UpdateClassGroupRequestDTO;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.service.ClassGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
        MockitoAnnotations.openMocks(this);

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
        assertEquals("Class A", classes.get(0).getName());


    }

    @Test
    void testGetClassById_Success() {
        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(sampleClass));

        ClassGroupResponseDTO responseDTO = classGroupService.getClassById(10L);

        assertEquals(10L, responseDTO.getId());
        assertEquals("Class A", responseDTO.getName());
    }

    @Test
    void testGetClassById_NotFound() {
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> classGroupService.getClassById(99L));

        assertEquals("404 NOT_FOUND \"Class not found with id: 99\"", exception.getMessage());

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

        verify(classGroupRepository, times(1)).save(any(ClassGroup.class));
    }

    @Test
    void testCreateClass_CustomerNotFound() {
        CreateClassGroupRequestDTO requestDTO = new CreateClassGroupRequestDTO("Test Class", 99L);

        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex =  assertThrows(ResponseStatusException.class, () -> classGroupService.createClass(requestDTO));

        assertEquals("404 NOT_FOUND \"Customer not found with id: 99\"", ex.getMessage());
    }

    @Test
    void testDeleteClass_Success() {
        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(sampleClass));

        assertDoesNotThrow(() -> classGroupService.deleteClass(10L));
        verify(classGroupRepository, times(1)).delete(sampleClass);

    }

    @Test
    void testDeleteClass_NotFound() {
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex =  assertThrows(ResponseStatusException.class, () -> classGroupService.deleteClass(99L));
        assertEquals("404 NOT_FOUND \"Class not found with id: 99\"", ex.getMessage());

    }

    @Test
    void testUpdateClass_Success() {
        UpdateClassGroupRequestDTO requestDTO = new UpdateClassGroupRequestDTO("App class");

        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(sampleClass));
        when(classGroupRepository.save(any(ClassGroup.class))).thenReturn(sampleClass);

        ClassGroupResponseDTO responseDTO = classGroupService.updateClass(10L, requestDTO);

        assertEquals("App class", responseDTO.getName());
        verify(classGroupRepository, times(1)).save(sampleClass);
    }
}

