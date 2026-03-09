package com.zo.webapi.mapper;

import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.ConsultantResponseDTO;
import com.zo.webapi.model.Consultant;

public class ConsultantMapper {

    public static ConsultantResponseDTO toDTO(Consultant consultant) {
        if (consultant == null) return null;

        return new ConsultantResponseDTO(
                consultant.getId(),
                consultant.getUser().getName(),
                consultant.getUser().getCity(),
                consultant.getUser() != null ? consultant.getUser().getId() : null,
                consultant.getManager() != null ? consultant.getManager().getId() : null
        );
    }

    public static Consultant toEntity(ConsultantDTO dto) {
        if (dto == null) return null;

        Consultant consultant = new Consultant();
        consultant.setId(dto.getId());

        return consultant;
    }
}
