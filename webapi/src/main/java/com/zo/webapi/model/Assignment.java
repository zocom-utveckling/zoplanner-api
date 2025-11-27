package com.zo.webapi.model;

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

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private Consultant consultant;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;


    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("assignment")
    @JsonManagedReference
    private List<Session> sessions = new ArrayList<>();

    // Constructors
    public Assignment() {}

    public Assignment(Course course, Consultant consultant, LocalDate dateStart, LocalDate dateEnd) {
        this.course = course;
        this.consultant = consultant;
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

    public Consultant getConsultant() {
        return consultant;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session) {
        sessions.add(session);
        session.setAssignment(this);
    }
}
