package com.zo.webapi.dto;

public class ConsultantDTO {

    private Long id;
    private String name;
    private String city;


    public ConsultantDTO(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;

    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }

}
