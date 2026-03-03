package com.zo.webapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConsultantDTO {

    private Long id;
    private String name;
    private String city;
    private Long userId;
    private Long managerId;

    public ConsultantDTO(Long id, String name, String city, Long userId, Long managerId) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.userId = userId;
        this.managerId = managerId;
    }

    public ConsultantDTO(Long id, String city, Long userId, Long managerId) {
        this.id = id;
        this.city = city;
        this.userId = userId;
        this.managerId = managerId;
    }
}

