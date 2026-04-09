package external.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(name = "consultant_email")
    private String consultantEmail;

    @Column(name = "message")
    private String message;

    @Column(name = "send_at")
    private LocalDateTime sendAt;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "sent")
    private boolean sent;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createdAt;

    public Reminder() {}
}



//CREATE TABLE reminders (
//        id UUID PRIMARY KEY,
//        teacher_email TEXT NOT NULL,
//        message TEXT NOT NULL,
//        send_at TIMESTAMP NOT NULL,
//        event_time TIMESTAMP,
//        sent BOOLEAN DEFAULT FALSE,
//        created_at TIMESTAMP DEFAULT NOW()
//);