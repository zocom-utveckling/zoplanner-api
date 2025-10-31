package com.zo.webapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "classes")

public class ClassGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "customer_id")
    private Long customerId;

    // Constructor

    public ClassGroup() {}

    public ClassGroup(String name, Long customerId) {
        this.name = name;
        this.customerId = customerId;
    }

    //Getters & Setters
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
}
