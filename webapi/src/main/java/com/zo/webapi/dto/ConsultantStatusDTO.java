package com.zo.webapi.dto;

import com.zo.webapi.enums.ConsultantStatusType; // ⚡ korrekt package
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ConsultantStatusDTO {

    @NotNull
    private Long consultantId;

    @NotNull
    private ConsultantStatusType status;

    @NotNull
    private LocalDate dateStart;

    @NotNull
    private LocalDate dateEnd;

    private String comment;

    // Getters & Setters
    public Long getConsultantId() { return consultantId; }
    public void setConsultantId(Long consultantId) { this.consultantId = consultantId; }

    public ConsultantStatusType getStatus() { return status; }
    public void setStatus(ConsultantStatusType status) { this.status = status; }

    public LocalDate getDateStart() { return dateStart; }
    public void setDateStart(LocalDate dateStart) { this.dateStart = dateStart; }

    public LocalDate getDateEnd() { return dateEnd; }
    public void setDateEnd(LocalDate dateEnd) { this.dateEnd = dateEnd; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
