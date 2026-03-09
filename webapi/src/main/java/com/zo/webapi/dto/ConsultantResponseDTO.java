package com.zo.webapi.dto;

public class ConsultantResponseDTO {

    private Long id;
    private String city;
    private Long userId;
    private Long managerId;
    private String name;

    public ConsultantResponseDTO(Long id, String name, String city, Long userId, Long managerId) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.userId = userId;
        this.managerId = managerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
