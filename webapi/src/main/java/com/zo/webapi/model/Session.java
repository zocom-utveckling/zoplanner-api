package com.zo.webapi.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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


    @Schema(example = "2025-11-03 12:00", description = "När passet börjar (format: yyyy-MM-dd HH:mm)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Schema(example = "2025-11-03 16:00", description = "När passet slutar (format: yyyy-MM-dd HH:mm)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "time_end")
    private LocalDateTime timeEnd;

    //@JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "location" /*, columnDefinition = "session_location"*/)
    private SessionLocation location;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    @JsonIgnoreProperties("assignment")
    @Schema(hidden = true)
    @JsonBackReference
    private Assignment assignment;

    /**
     * Represents the time frame where a consultant is lecturing.
     *
     * @param timeStart What time the session starts
     * @param timeEnd   What time the session end
     */
    public Session(LocalDateTime timeStart,  LocalDateTime timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public Session() {}

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime endTime) {
        this.timeEnd = endTime;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime startTime) {
        this.timeStart = startTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public SessionLocation getLocation() {
        return location;
    }

    public void setLocation(SessionLocation location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
