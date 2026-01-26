package com.zo.webapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "consultants")
@Data
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column(nullable = false)
    private String city;

    @JsonIgnore
    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    @JsonIgnore
    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    private List<ConsultantStatus> statuses;
}