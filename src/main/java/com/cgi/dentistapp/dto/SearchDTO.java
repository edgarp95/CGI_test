package com.cgi.dentistapp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;

/**
 * Otsinguobjekt - sellega lihtsam opereerida kui lihtsalt atribuute otsingust saata
 */
public class SearchDTO {
    Long id;

    @Size(max = 50)
    String dentistName;

    String familyDrName;


    public SearchDTO() {
    }


    public SearchDTO(Long id, String dentistName, String familyDrName) {
        this.id = id;
        this.dentistName = dentistName;
        this.familyDrName = familyDrName;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }


    public String getFamilyDrName() {
        return familyDrName;
    }

    public void setFamilyDrName(String familyDrName) {
        this.familyDrName = familyDrName;
    }

}
