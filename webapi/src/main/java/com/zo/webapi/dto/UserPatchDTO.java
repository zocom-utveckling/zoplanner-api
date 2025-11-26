package com.zo.webapi.dto;

import com.zo.webapi.enums.UserRole;

public class UserPatchDTO {
    private String username;
    private String password;
    private String name;
    private UserRole role;

    public UserPatchDTO() {}

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
