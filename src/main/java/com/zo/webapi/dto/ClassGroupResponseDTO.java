package com.zo.webapi.dto;

public class ClassGroupResponseDTO {

    private Long id;
    private String name;
    private Long customerId;
    private String customerName;

    public ClassGroupResponseDTO() {}

    public ClassGroupResponseDTO(Long id, String name, Long customerId, String customerName) {
        this.id = id;
        this.name = name;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
