package com.zo.webapi.service;

import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.UserService;
import com.zo.webapi.dto.FileResponse;
import com.zo.webapi.dto.ProfilePictureResponseDTO;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zo.webapi.enums.UserRole;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User(1L, "john_doe", "password123", "John Doe", "johndoe@mail.com", "Malmö", UserRole.CONSULTANT);
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
        assertEquals("User not found with id: 1", exception.getMessage());
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

    @Test
    void testEmailExists() {
        when(userRepository.existsByEmail("johndoe@mail.com")).thenReturn(true);
        assertTrue(userService.emailExists("johndoe@mail.com"));
        verify(userRepository, times(1)).existsByEmail("johndoe@mail.com");
    }

    @Test
    void testUploadProfilePicture_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        FileResponse fileResponse = new FileResponse(10, "/files/10", "avatar.png", (long) file.getBytes().length);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(fileService.uploadFile(file)).thenReturn(fileResponse);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProfilePictureResponseDTO result = userService.uploadProfilePicture(1L, file);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("/files/10", result.getProfilePicture());
        assertEquals("/files/10", sampleUser.getProfilePicture());

        verify(userRepository, times(1)).findById(1L);
        verify(fileService, times(1)).uploadFile(file);
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testUploadProfilePicture_UserNotFound() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exc = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.uploadProfilePicture(999L, file)
        );

        assertEquals("User not found with id: '999'", exc.getMessage());

        verify(userRepository, times(1)).findById(999L);
        verifyNoInteractions(fileService);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUploadProfilePicture_InvalidContentType_ThrowsInvalidData() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                "fake-pdf-content".getBytes()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        InvalidDataException exc = assertThrows(
                InvalidDataException.class,
                () -> userService.uploadProfilePicture(1L, file)
        );

        assertEquals("Only image files are allowed", exc.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(fileService);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUploadProfilePicture_FileUploadFails_ThrowsIOException() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(fileService.uploadFile(file)).thenThrow(new IOException("Upload failed"));

        IOException exc = assertThrows(
                IOException.class,
                () -> userService.uploadProfilePicture(1L, file)
        );

        assertEquals("Upload failed", exc.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(fileService, times(1)).uploadFile(file);
        verify(userRepository, never()).save(any(User.class));
    }
}
