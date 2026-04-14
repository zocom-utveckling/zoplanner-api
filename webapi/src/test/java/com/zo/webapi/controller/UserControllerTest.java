package com.zo.webapi.controller;

import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.service.UserService;
import com.zo.webapi.dto.ProfilePictureResponseDTO;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;
import com.zo.webapi.enums.UserRole;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ManagerRepository managerRepository;

    @MockBean
    private ConsultantRepository consultantRepository;

    @MockBean
    private GenericResponseService genericResponseService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User(1L, "testuser", "testpassword", "Test User","testuser@mail.com", "Malmö", UserRole.MANAGER);
    }

    @Test
    void testShowAllUsers() throws Exception{
        List<User> users = new ArrayList<>();

        users.add(user);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("testuser@mail.com"))
                .andExpect(jsonPath("$[0].role").value("MANAGER"));
    }

    @Test
    void testGetUserById_Success() throws Exception{
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("testuser@mail.com"))
                .andExpect(jsonPath("$.role").value("MANAGER"));

    }

    @Test
    void testGetUserById_NotFound() throws Exception{
        when(userService.getUserById(999L))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserByUsername_Success() throws Exception {
        when(userService.getUserByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("testuser@mail.com"))
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    @Test
    void testGetUserByUsername_NotFound() throws Exception {
        when(userService.getUserByUsername("user2"))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/api/users/username/user2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Success() throws Exception{
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUser_NotFound() throws Exception{
        doThrow(new IllegalArgumentException("User not found"))
                .when(userService).deleteUser(999L);
        mockMvc.perform(delete("/api/users/999"))
               .andExpect(status().isNotFound());


    }

    @Test
    void testCreateUser_Success() throws Exception{
        when(userService.usernameExists("testuser")).thenReturn(false);
        when(userService.createUser(any(User.class))).thenReturn(user);

        String requestJson = """
            {
                "username": "testuser",
                "password": "testpassword",
                "name": "Test User",
                "email": "testuser@mail.com",
                "city": "Malmö",
                "role": "MANAGER"
            }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("testuser@mail.com"))
                .andExpect(jsonPath("$.city").value("Malmö"))
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    @Test
    void testCreateUser_AlreadyExists() throws Exception {
        when(userService.usernameExists("testuser")).thenReturn(true);
        String requestJson = """
            {
                "username": "testuser",
                "password": "testpassword",
                "name": "Test User",
                "email": "testuser@mail.com",
                "city": "Malmö",
                "role": "MANAGER"
            }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict());
        verify(userService, never()).createUser(user);
    }

    @Test
    void testUploadProfilePicture_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        ProfilePictureResponseDTO responseDTO =
                new ProfilePictureResponseDTO(1L, "/files/10");

        when(userService.uploadProfilePicture(eq(1L), any())).thenReturn(responseDTO);

        mockMvc.perform(multipart("/api/users/{id}/profile-picture", 1L)
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.profilePicture").value("/files/10"));

        verify(userService, times(1)).uploadProfilePicture(eq(1L), any());
    }

    @Test
    void testUploadProfilePicture_UserNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        when(userService.uploadProfilePicture(eq(999L), any()))
                .thenThrow(new ResourceNotFoundException("User", "id", 999L));

        mockMvc.perform(multipart("/api/users/{id}/profile-picture", 999L)
                .file(file))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).uploadProfilePicture(eq(999L), any());
    }

    @Test
    void testUploadProfilePicture_EmptyFile_BadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.png",
                "image/png",
                new byte[0]
        );

        when(userService.uploadProfilePicture(eq(1L), any()))
                .thenThrow(new InvalidDataException("File must not be empty"));

        mockMvc.perform(multipart("/api/users/{id}/profile-picture", 1L)
                .file(file))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).uploadProfilePicture(eq(1L), any());
    }

    @Test
    void testUploadProfilePicture_InvalidContentType_BadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                "fake-pdf-content".getBytes()
        );

        when(userService.uploadProfilePicture(eq(1L), any()))
                .thenThrow(new InvalidDataException("Only image files are allowed"));

        mockMvc.perform(multipart("/api/users/{id}/profile-picture", 1L)
                .file(file))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).uploadProfilePicture(eq(1L), any());
    }

    @Test
    void testUploadProfilePicture_IOException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        when(userService.uploadProfilePicture(eq(1L), any()))
                .thenThrow(new java.io.IOException("Disk error"));

        mockMvc.perform(multipart("/api/users/{id}/profile-picture", 1L)
                .file(file))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).uploadProfilePicture(eq(1L), any());
    }
}

