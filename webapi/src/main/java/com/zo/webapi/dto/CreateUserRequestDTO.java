package com.zo.webapi.dto;

import com.zo.webapi.enums.UserRole;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequestDTO {
    @NotBlank(message = "Username is requiered")
    private String username;

    @NotBlank(message = "Password is requiered")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private UserRole role = UserRole.CONSULTANT;

    public CreateUserRequestDTO() {}

    public CreateUserRequestDTO(String username, String password, String name, UserRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }


}
