package com.zo.webapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "consultant_id")
    private Long consultantId;

    @Column(name = "date_start", nullable = false)
    private LocalDate dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDate dateEnd;


    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnoreProperties("sessions")
    @JsonManagedReference
    private final List<Session> sessions = new ArrayList<>();

    // Constructors
    public Assignment() {}

    public Assignment(Course course, Long consultantId, LocalDate dateStart, LocalDate dateEnd) {
        this.course = course;
        this.consultantId = consultantId;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


    public Long getConsultantId() {
        return consultantId;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setConsultantId(Long consultantId) {
        this.consultantId = consultantId;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void addSession(Session session) {
        sessions.add(session);
    }
}
