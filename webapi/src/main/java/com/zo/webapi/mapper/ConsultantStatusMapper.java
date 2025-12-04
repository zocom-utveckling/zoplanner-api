package com.zo.webapi.mapper;

import com.zo.webapi.dto.ConsultantStatusDTO;
import com.zo.webapi.model.ConsultantStatus;

public class ConsultantStatusMapper {

    public static ConsultantStatusDTO toDTO(ConsultantStatus status) {
        ConsultantStatusDTO dto = new ConsultantStatusDTO();
        dto.setConsultantId(status.getConsultant().getId());
        dto.setStatus(status.getStatus());
        dto.setDateStart(status.getDateStart());
        dto.setDateEnd(status.getDateEnd());
        dto.setComment(status.getComment());
        return dto;
    }

    public static ConsultantStatus toEntity(ConsultantStatusDTO dto) {
        ConsultantStatus status = new ConsultantStatus();
        // Consultant måste sättas separat i service
        status.setStatus(dto.getStatus());
        status.setDateStart(dto.getDateStart());
        status.setDateEnd(dto.getDateEnd());
        status.setComment(dto.getComment());
        return status;
    }
}
