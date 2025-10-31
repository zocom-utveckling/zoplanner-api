package com.zo.webapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

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

    // Constructors
    public Assignment() {
    }

    public Assignment(String courseName, Long consultantId,
                      LocalDate dateStart, LocalDate dateEnd, Long classId) {
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
    @JsonIgnore
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

}
