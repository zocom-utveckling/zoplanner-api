package com.zo.webapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public class CreateClassGroupRequestDTO {

    @NotBlank(message = "Class group name must not be blank")
    @Size(max = 100, message = "Class name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Customer ID must not be null")
    private Long customerId;

    public CreateClassGroupRequestDTO() {}

    public CreateClassGroupRequestDTO(String name, Long customerId) {
        this.name = name;
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
