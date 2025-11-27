package com.zo.webapi.model;

import enums.ConsultantStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "consultant_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    private Consultant consultant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultantStatusType status;

    @Column(name = "date_start", nullable = false)
    private LocalDate dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDate dateEnd;

    @Column(columnDefinition = "TEXT")
    private String comment;
}
