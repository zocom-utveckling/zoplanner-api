package com.zo.webapi.mapper;

import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.model.Consultant;

public class ConsultantMapper {

    public static ConsultantDTO toDTO(Consultant consultant) {
        if (consultant == null) return null;

        return new ConsultantDTO(
                consultant.getId(),
                consultant.getCity(),
                consultant.getUser() != null ? consultant.getUser().getId() : null,
                consultant.getManager() != null ? consultant.getManager().getId() : null
        );
    }

    public static Consultant toEntity(ConsultantDTO dto) {
        if (dto == null) return null;

        Consultant consultant = new Consultant();
        consultant.setId(dto.getId());
        consultant.setCity(dto.getCity());


        return consultant;
    }
}
