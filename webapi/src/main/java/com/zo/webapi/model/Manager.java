package com.zo.webapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name="managers")

public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Relationship: One manager has ONE user(NOT NULL in db)
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false, unique = true,foreignKey = @ForeignKey(name = "fk_manager_user"))
    private User user;

    // A manager can have many consultants
    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private List<Consultant> consultants;

    // A manager can have multiple customers
    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private List<Customer> customers;

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private List<Assignment> assignments;

    //Constructors

    public Manager() {
    }

    public Manager(User user) {
        this.user = user;
    }

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Consultant> getConsultants() {
        return consultants;
    }

    public void setConsultants(List<Consultant> consultants) {
        this.consultants = consultants;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
