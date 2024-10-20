package com.custis.university.dto;

public class StudentDTO {

    private Integer id;
    private String name;
    private String action;

    public StudentDTO() {
    }

    public StudentDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
