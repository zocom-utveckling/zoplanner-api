package com.zo.webapi.model;

import com.zo.webapi.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Column(name = "email", unique = true)
    private String email;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.CONSULTANT;


    //Constructors
    public User(Long id, String username, String password, String name, String email, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role != null ? role:  UserRole.CONSULTANT;
    }

    public User() {}

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

}