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

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "consultant_id")
    private Long consultantId;

    @Column(name = "date_start", nullable = false)
    private LocalDate dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDate dateEnd;

    @Column(name = "class_id")
    private Long classId;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnoreProperties("sessions")
    @JsonManagedReference
    private final List<Session> sessions = new ArrayList<>();

    // Constructors
    public Assignment() {}

    public Assignment(String courseName, Long consultantId, LocalDate dateStart, LocalDate dateEnd, Long classId) {
        this.courseName = courseName;
        this.consultantId = consultantId;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.classId = classId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
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

    public Long getClassId() {
        return classId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void addSession(Session session) {
        sessions.add(session);
    }
}