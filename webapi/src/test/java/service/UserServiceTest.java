/*package service;

import com.zo.webapi.model.User;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zo.webapi.enums.UserRole;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User(1L, "john_doe", "password123", "John Doe", UserRole.CONSULTANT);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(sampleUser));
        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("john_doe", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User created = userService.createUser(sampleUser);

        assertNotNull(created);
        assertEquals("john_doe", created.getUsername());
        assertEquals(UserRole.CONSULTANT, created.getRole());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        User found = userService.getUserById(1L);

        assertEquals("john_doe", found.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with id 1", exception.getMessage());
    }

    @Test
    void testDeleteUser_Exists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with id 1", exception.getMessage());
    }

    @Test
    void testUsernameExists() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);
        assertTrue(userService.usernameExists("john_doe"));
        verify(userRepository, times(1)).existsByUsername("john_doe");
    }

    @Test
    void testGetUserByUsername_Found() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(sampleUser));
        User found = userService.getUserByUsername("john_doe");
        assertEquals("john_doe", found.getUsername());
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserByUsername("john_doe"));
        assertEquals("User not found with name john_doe", exception.getMessage());
    }
}
*/
