package com.zo.webapi.controller;

import com.zo.webapi.model.User;
import com.zo.webapi.enums.UserRole;

import com.zo.webapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testShowAllUsers() throws Exception{
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole(UserRole.MANAGER);
        user.setName("Test User");
        users.add(user);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].name").value("Test User"));


    }

    @Test
    void testGetUserById_Success() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole(UserRole.MANAGER);
        user.setName("Test User");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"));
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
        User user = new User();
        user.setId(1L);
        user.setUsername("newuser");
        user.setPassword("password123");
        user.setRole(UserRole.BOTH);
        user.setName("New User");

        when(userService.usernameExists("newuser")).thenReturn(false);
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newuser\",\"password\":\"password123\",\"role\":\"BOTH\",\"name\":\"New User\"}"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.name").value("New User"));
    }
}


