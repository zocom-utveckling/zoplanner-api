package com.zo.webapi.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @Schema(example = "2025-11-03T12:00:00", description = "När passet börjar (format: yyyy-MM-dd'T'HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Schema(example = "2025-11-03T16:00:00", description = "När passet slutar (format: yyyy-MM-dd'T'HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "time_end")
    private LocalDateTime timeEnd;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private SessionLocation location;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    @JsonIgnoreProperties("assignment")
    @Schema(hidden = true)
    @JsonBackReference
    private Assignment assignment;

    public Session(LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public Session() {}

    // --- Getters och Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateStart() { return dateStart; }
    public void setDateStart(LocalDate dateStart) { this.dateStart = dateStart; }

    public LocalDate getDateEnd() { return dateEnd; }
    public void setDateEnd(LocalDate dateEnd) { this.dateEnd = dateEnd; }

    public LocalDateTime getTimeStart() { return timeStart; }
    public void setTimeStart(LocalDateTime timeStart) { this.timeStart = timeStart; }

    public LocalDateTime getTimeEnd() { return timeEnd; }
    public void setTimeEnd(LocalDateTime timeEnd) { this.timeEnd = timeEnd; }

    public SessionLocation getLocation() { return location; }
    public void setLocation(SessionLocation location) { this.location = location; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }
}