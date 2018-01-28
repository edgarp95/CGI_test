package com.cgi.dentistapp.dao.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dentist_visit")
public class DentistVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "dentist")
    private String dentistName;

    @Column(name = "familyDr")
    private String familyDr;


    @Column(name = "visit_time")
    private LocalDateTime visitTime; // nüüd siin ka kellaaeg sees, seega date ei sobi

    public DentistVisitEntity() {
    }


    public DentistVisitEntity(String dentistName, String familyDr, LocalDateTime visitTime) {
        this.dentistName = dentistName;
        this.familyDr = familyDr;
        this.setVisitTime(visitTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalDateTime visitTime) {
        this.visitTime = visitTime;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public String getFamilyDr() {
        return familyDr;
    }

    public void setFamilyDr(String familyDr) {
        this.familyDr = familyDr;
    }


    @Override
    public String toString() {
        return "Visiidi id: "+id+ ", hambaarsti nimi: " + dentistName + "; perearsti nimi: " + familyDr + "; broneeringu kuupäev: " + visitTime.toString();
    }
}
