package service;

import com.zo.webapi.dto.ManagerResponseDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ManagerService managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createManager_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("manager");
        user.setRole(UserRole.MANAGER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(managerRepository.existsByUserId(1L)).thenReturn(false);

        ManagerResponseDTO responseDTO = managerService.createManager(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getUserId());
        assertEquals("manager", responseDTO.getUsername());
        verify(managerRepository, times(1)).save(any(Manager.class));
    }


    @Test
    void testCreateManager_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> managerService.createManager(1L));

        assertTrue(exception.getMessage().contains("User not found"));
    }


    @Test
    void testGetManagerById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("manager");
        user.setRole(UserRole.MANAGER);

        Manager manager = new Manager();
        manager.setId(10L);
        manager.setUser(user);

        when(managerRepository.findById(10L)).thenReturn(Optional.of(manager));

        ManagerResponseDTO responseDTO = managerService.getManagerById(10L);

        assertEquals("manager", responseDTO.getUsername());

    }

    @Test
    void testGetManagerById_UserNotFound() {
        when(managerRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> managerService.getManagerById(10L));
    }

    @Test
    void testGetAllManagers_Success() {
        User user = new User();
        user.setUsername("manager");
        user.setRole(UserRole.MANAGER);

        Manager manager = new Manager();
        manager.setId(10L);
        manager.setUser(user);

        when(managerRepository.findAll()).thenReturn(List.of(manager));
        List<ManagerResponseDTO> responseDTO = managerService.getAllManagers();

        assertEquals(1, responseDTO.size());
        assertEquals("manager", responseDTO.get(0).getUsername());
    }

    @Test
    void testGetManagerByUserId_Success() {
        User user = new User();
        user.setId(2L);
        user.setUsername("userA");
        user.setRole(UserRole.MANAGER);

        Manager manager = new Manager();
        manager.setId(10L);
        manager.setUser(user);

        when(managerRepository.findByUserId(2L)).thenReturn(Optional.of(manager));

        ManagerResponseDTO responseDTO = managerService.getManagerByUserId(2L);

        assertEquals("userA", responseDTO.getUsername());
    }

    @Test
    void testGetManagerByUserId_UserNotFound() {
        when(managerRepository.findByUserId(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> managerService.getManagerByUserId(999L));
    }

    @Test
    void testAssignConsultantToManager_Success() {
        Manager manager = new Manager();
        manager.setId(1L);

        Consultant consultant = new Consultant();
        consultant.setId(2L);

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(consultantRepository.findById(2L)).thenReturn(Optional.of(consultant));

        managerService.assignConsultantToManager(1L, 2L);

        assertEquals(manager, consultant.getManager());
        verify(consultantRepository).save(consultant);
    }

    @Test
    void testAssignConsultantToManager_ConsultantNotFound() {
        Manager manager = new Manager();
        manager.setId(1L);

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(consultantRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> managerService.assignConsultantToManager(1L, 2L));

    }

    @Test
    void testRemoveConsultantFromManager_Success() {
        Consultant consultant = new Consultant();
        consultant.setId(2L);

        when(managerRepository.existsById(1L)).thenReturn(true);
        when(consultantRepository.findById(2L)).thenReturn(Optional.of(consultant));

        managerService.removeConsultantFromManager(1L, 2L);

        assertNull(consultant.getManager());
        verify(consultantRepository).save(consultant);
    }

    @Test
    void testRemoveConsultantFromManager_ManagerNotFound() {
        when(managerRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> managerService.removeConsultantFromManager(1L, 2L));
    }

    @Test
    void testAssignCustomerToManager_Success() {
        Manager manager = new Manager();
        manager.setId(1L);

        Customer customer = new Customer();
        customer.setId(2L);

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));

        managerService.assignCustomerToManager(1L, 2L);

        assertEquals(manager, customer.getManager());
        verify(customerRepository).save(customer);
    }

    @Test
    void testAssignCustomerToManager_CustomerNotFound() {
        Manager manager = new Manager();
        manager.setId(1L);
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> managerService.assignCustomerToManager(1L, 2L));
    }

    @Test
    void testRemoveCustomerFromManager_Success() {
        Customer customer = new Customer();
        customer.setId(2L);

        when(managerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));

        managerService.removeCustomerFromManager(1L, 2L);

        assertNull(customer.getManager());
        verify(customerRepository).save(customer);


    }

    @Test
    void testRemoveCustomerFromManager_ManagerNotFound() {
        when(managerRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> managerService.removeCustomerFromManager(1L, 2L));
    }

    @Test
    void testUpdateManager_Success() {
        Manager manager = new Manager();
        manager.setId(1L);

        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("newUser");
        newUser.setRole(UserRole.MANAGER);

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));
        when(managerRepository.existsByUserId(2L)).thenReturn(false);

        ManagerResponseDTO responseDTO = managerService.updateManagerUser(1L, 2L);

        assertEquals("newUser", responseDTO.getUsername());


    }

    @Test
    void testUpdateManager_UserNotFound() {
        Manager manager = new Manager();
        manager.setId(1L);
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> managerService.updateManagerUser(1L, 2L));
    }

    @Test
    void testDeleteManager_Success() {
        Manager manager = new Manager();
        manager.setId(1L);

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        managerService.deleteManager(1L);

        verify(managerRepository).delete(manager);
    }

    @Test
    void testDeleteManager_ManagerNotFound() {
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> managerService.deleteManager(1L));
    }
}
