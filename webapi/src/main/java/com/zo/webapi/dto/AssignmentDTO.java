package com.zo.webapi.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AssignmentDTO {

    private Long id; // for update, kan vara null for create
    private Long consultantId;
    private Long courseId;
    private LocalDate dateStart;
    private LocalDate dateEnd;
}
