package com.zo.webapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateClassGroupRequestDTO {

    @NotBlank(message = "Class name is required")
    @Size(max = 100, message = "Class name must not exceed 100 characters")
    private String name;

    // Constructors

    public UpdateClassGroupRequestDTO() {}

    public UpdateClassGroupRequestDTO(String name) {
        this.name = name;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
