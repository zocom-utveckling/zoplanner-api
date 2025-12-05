package com.zo.webapi.dto;

public class ManagerResponseDTO {

    private Long id;
    private Long userId;
    private String username;
    private String role;

    public ManagerResponseDTO() {}

    public ManagerResponseDTO(Long id, Long userId, String username, String role) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }


    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
