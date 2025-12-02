
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
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.web.server.ResponseStatusException;


public class ClassGroupServiceTest {
    @Mock
    private ClassGroupRepository classGroupRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ClassGroupService classGroupService;

    private Customer customer;
    private ClassGroup classGroup;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Tau Training");

        classGroup = new ClassGroup();
        classGroup.setId(10L);
        classGroup.setName("Python with AI");
        classGroup.setCustomer(customer);

    }


    @Test
    void testGetAllClasses() {

        when(classGroupRepository.findAll()).thenReturn(List.of(classGroup));

        // Act
        List<ClassGroupResponseDTO> response = classGroupService.getAllClasses();

        // Assert
        assertEquals(1, response.size());
        assertEquals("Python with AI", response.get(0).getName());
        assertEquals(1L, response.get(0).getCustomerId());
        assertEquals("Tau Training", response.get(0).getCustomerName());
        verify(classGroupRepository, times(1)).findAll();
    }

    @Test
    void testGetClassById() {

        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(classGroup));

        ClassGroupResponseDTO response = classGroupService.getClassById(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Python with AI", response.getName());
        assertEquals("Tau Training", response.getCustomerName());
    }



    @Test
    void testGetClassById_NotFound() {
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> classGroupService.getClassById(99L));

        assertEquals("Class not found with id: 99", exception.getReason());
        verify(classGroupRepository, times(1)).findById(99L);
    }


    @Test
    void testCreateClass() {
        CreateClassGroupRequestDTO requestDTO = new CreateClassGroupRequestDTO();
        requestDTO.setName("Python with AI");
        requestDTO.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(classGroupRepository.existsByNameAndCustomerId("Python with AI", 1L)).thenReturn(false);
        when(classGroupRepository.save(any(ClassGroup.class))).thenReturn(classGroup);

        ClassGroupResponseDTO response = classGroupService.createClass(requestDTO);

        assertNotNull(response);
        assertEquals("Python with AI", response.getName());
        assertEquals(1L, response.getCustomerId());
        assertEquals("Tau Training", response.getCustomerName());
    }

    @Test
    void testUpdateClass() {
        UpdateClassGroupRequestDTO requestDTO = new UpdateClassGroupRequestDTO();
        requestDTO.setName("Updated Name");

        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(classGroup));
        when(classGroupRepository.save(any(ClassGroup.class))).thenReturn(classGroup);

        ClassGroupResponseDTO response = classGroupService.updateClass(10L, requestDTO);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Updated Name", response.getName());
        verify(classGroupRepository, times(1)).save(any(ClassGroup.class));
    }

    @Test
    void testGetClassesByCustomerId() {

        when(customerRepository.existsById(1L)).thenReturn(true);
        when(classGroupRepository.findByCustomerId(1L)).thenReturn(List.of(classGroup));

        // Act
        List<ClassGroupResponseDTO> response = classGroupService.getClassesByCustomerId(1L);

        // Assert
        assertEquals(1, response.size());
        assertEquals("Python with AI", response.get(0).getName());
        assertEquals(1L, response.get(0).getCustomerId());
        assertEquals("Tau Training", response.get(0).getCustomerName());
    }

    @Test
    void testGetClassesByCustomerId_notFound() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> classGroupService.getClassesByCustomerId(99L));
    }

    @Test
    void testDeleteClass() {
        when(classGroupRepository.findById(1L)).thenReturn(Optional.of(classGroup));
        classGroupService.deleteClass(1L);
        verify(classGroupRepository, times(1)).delete(classGroup);
    }

    @Test
    void testDeleteClass_NotFound() {
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> classGroupService.deleteClass(99L));

        assertEquals("Class not found with id: 99", exception.getReason());
    }
}

