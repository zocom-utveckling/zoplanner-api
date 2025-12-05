package com.zo.webapi.controller;

import com.zo.webapi.model.User;
import com.zo.webapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;
import com.zo.webapi.enums.UserRole;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testShowAllUsers() throws Exception{
        List<User> users = new ArrayList<>();
        User user = new User(1L, "testuser", "testpassword", "Test User", UserRole.MANAGER);

        users.add(user);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].role").value("MANAGER"));
    }

    @Test
    void testGetUserById_Success() throws Exception{
        User user = new User(1L, "testuser", "testpassword", "Test User", UserRole.MANAGER);

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
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
    void testDeleteUser_Success() throws Exception{
      doNothing().when(userService).deleteUser(1L);

      mockMvc.perform(delete("/api/users/1"))
              .andExpect(status().isNoContent());

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
        User user = new User(1L, "testuser", "testpassword", "Test User", UserRole.CONSULTANT);

        when(userService.usernameExists("testuser")).thenReturn(false);
        when(userService.createUser(any(User.class))).thenReturn(user);

        String requestJson = """
            {
                "username": "testuser",
                "password": "testpassword",
                "name": "Test User",
                "role": "CONSULTANT"
            }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.role").value("CONSULTANT"));
    }
}

