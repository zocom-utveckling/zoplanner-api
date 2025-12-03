package com.zo.webapi.dto;

import com.zo.webapi.enums.ConsultantStatusType;
import java.time.LocalDate;

public class ConsultantStatusDTO {

    private Long id;
    private Long consultantId;
    private ConsultantStatusType status;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String comment;

    // Tom konstruktor
    public ConsultantStatusDTO() {}

    // Konstruktor med alla fält
    public ConsultantStatusDTO(Long id, Long consultantId, ConsultantStatusType status,
                               LocalDate dateStart, LocalDate dateEnd, String comment) {
        this.id = id;
        this.consultantId = consultantId;
        this.status = status;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.comment = comment;
    }

    // Getters
    public Long getId() { return id; }
    public Long getConsultantId() { return consultantId; }
    public ConsultantStatusType getStatus() { return status; }
    public LocalDate getDateStart() { return dateStart; }
    public LocalDate getDateEnd() { return dateEnd; }
    public String getComment() { return comment; }
}
