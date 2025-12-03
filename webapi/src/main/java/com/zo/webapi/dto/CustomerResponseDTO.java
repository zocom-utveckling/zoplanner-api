package com.zo.webapi.dto;

public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String city;
    private ManagerResponseToCustomerDTO manager;

    public CustomerResponseDTO(Long id, String name, String city, ManagerResponseToCustomerDTO manager) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.manager = manager;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ManagerResponseToCustomerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerResponseToCustomerDTO manager) {
        this.manager = manager;
    }
}