package com.cgi.dentistapp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Created by serkp on 2.03.2017.
 */
public class DentistVisitDTO {


    @Size(min = 1, max = 50)
    String dentistName;


    @NotNull
    String familyDrName;

    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    LocalDate visitTime;

    @NotNull
    // Regex leitud siit: http://www.mkyong.com/regular-expressions/how-to-validate-time-in-24-hours-format-with-regular-expression/
    @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]")
    String visitClock;



    public DentistVisitDTO() {
    }



    public DentistVisitDTO(String dentistName, String familyDrName, LocalDate visitTime, String visitClock) {
        this.dentistName = dentistName;
        this.familyDrName = familyDrName;
        this.visitTime = visitTime;
        this.visitClock = visitClock;

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

    public LocalDate getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalDate visitTime) {
        this.visitTime = visitTime;
    }


    public String getVisitClock() {
        return visitClock;
    }

    public void setVisitClock(String visitClock) {
        this.visitClock = visitClock;
    }
}
