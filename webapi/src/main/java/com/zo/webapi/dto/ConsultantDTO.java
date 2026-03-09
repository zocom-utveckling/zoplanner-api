package com.zo.webapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConsultantDTO {

    private Long id;
    private Long userId;
    private Long managerId;


    public ConsultantDTO(Long id, Long userId, Long managerId) {
        this.id = id;
        this.userId = userId;
        this.managerId = managerId;
    }
}

