package com.zo.webapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zo.webapi.dto.ManagerResponseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "city", nullable = false, length = 255)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = true, foreignKey = @ForeignKey(name = "fk_customer_manager"))
    private Manager manager;

    //Constructors
    public Customer(Long id, String name, String city, Manager manager) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.manager = manager;
    }
    public Customer() {}

    //Getters and Setters

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

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}