package com.cgi.dentistapp.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import javax.transaction.Transactional;

import com.cgi.dentistapp.dto.DentistVisitDTO;
import com.cgi.dentistapp.dto.SearchDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgi.dentistapp.dao.DentistVisitDao;
import com.cgi.dentistapp.dao.entity.DentistVisitEntity;

/**
 * Klass, mis suhtleb andmebaasiga vastavalt kasutaja vajadusele
 */
@Service
@Transactional
public class DentistVisitService {

    private static final Logger log = Logger.getLogger(DentistVisitService.class);

    @Autowired
    private DentistVisitDao dentistVisitDao;

    /**
     * Visiidi lisamine
     * @param dentistName hambaarsti nimi
     * @param familyDr perearsti nimi
     * @param visitTime visiidi aeg
     */
    public void addVisit(String dentistName, String familyDr, LocalDateTime visitTime) {
        DentistVisitEntity visit = new DentistVisitEntity(dentistName, familyDr, visitTime);
        dentistVisitDao.create(visit);
    }

    /**
     * Demovisiitide lisamine andmebaasi
     */
    public void addDemoVisits() {
        dentistVisitDao.create(new DentistVisitEntity("Edgar", "Liisa Temperatuur", LocalDateTime.now().plusDays(1)));
        dentistVisitDao.create(new DentistVisitEntity("Edgar", "Mari-Liis Köharohi", LocalDateTime.now().plusHours(1)));
        dentistVisitDao.create(new DentistVisitEntity("Edgar", "Mari-Liis Köharohi", LocalDateTime.now().plusMonths(5)));
        dentistVisitDao.create(new DentistVisitEntity("Madis", "Willem Nahatõbi", LocalDateTime.now().plusMinutes(23)));
        dentistVisitDao.create(new DentistVisitEntity("Mihkel", "Willem Nahatõbi", LocalDateTime.now().plusMonths(1)));
        dentistVisitDao.create(new DentistVisitEntity("Jaana", "Madis Kurguarst", LocalDateTime.now().plusYears(2)));
        dentistVisitDao.create(new DentistVisitEntity("Mihkel", "Mihkel Palavik", LocalDateTime.now().plusMinutes(1)));

    }

    /**
     * Perearstide kätte saamine ripp menüü jaoks
     *
     * @return stringide list perearstide nimedest
     */
    public List<String> getFamiliyDrs() {
        return new ArrayList<String>(Arrays.asList("Liisa Temperatuur", "Mihkel Palavik", "Mari-Liis Köharohi", "Madis Kurguarst", "Willem Nahatõbi"));
    }

    public List<DentistVisitEntity> listVisits() {
        return dentistVisitDao.getAllVisits();
    }

    public List<DentistVisitEntity> search(SearchDTO searchDTO) {
        List<DentistVisitEntity> visits = listVisits();

        // ühisosa leidmine
        if (searchDTO.getId() != null) {
            log.info("Visit id in search query!");
            log.info("Visit id: " + searchDTO.getId());
            visits.removeIf(visit -> !(Objects.equals(visit.getId(), searchDTO.getId())));

        }
        if (!searchDTO.getDentistName().isEmpty()) {
            log.info("Dentist name in search query!");
            log.info("Dentist name: " + searchDTO.getDentistName());
            visits.removeIf(visit -> !(visit.getDentistName().equals(searchDTO.getDentistName())));
        }

        if (!searchDTO.getFamilyDrName().isEmpty()) {
            log.info("FamilyDr name in search query!");
            log.info("FamilyDr name: " + searchDTO.getFamilyDrName());
            visits.removeIf(visit -> !(visit.getFamilyDr().equals(searchDTO.getFamilyDrName())));
        }

        return visits;
    }

    /**
     * Visiitide ühele ajale sattumise kontroll
     * @param visit kontrollitav visiit
     * @param id selle id, kui visiit uus, siis väärtuseks -1
     * @return tagastab kas true või false, vastavalt sellele, kas broneeritav visiit satub mõne teisega kokku
     */
    public Boolean timeIssue(DentistVisitDTO visit, Long id) {
        String[] time = visit.getVisitClock().split(":");
        LocalDateTime visitTime = LocalDateTime.of(visit.getVisitTime(), LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1])));
        // arvestame, et üks visiit on 45 minutit
        List<DentistVisitEntity> visits = listVisits();

        for (DentistVisitEntity v: visits) {
            System.out.println(v);
        }

        for (DentistVisitEntity v : visits) {

            // vaja uurida just sama hambaarsti aegu, kelle juurde klient tulla tahab, ühe kliendi vastuvõtt on 45 minutit
            if (v.getDentistName().equals(visit.getDentistName()) && (!Objects.equals(id, v.getId()))) {
                if (visitTime.isEqual(v.getVisitTime()) ) {
                    return true;
                }
                else if (!visitTime.isBefore(v.getVisitTime().minusMinutes(45)) && visitTime.isBefore(v.getVisitTime())) return true;
                else if (!visitTime.isAfter(v.getVisitTime().plusMinutes(45)) && visitTime.isAfter(v.getVisitTime())) return true;

            }
        }
        return false;
    }

    /**
     * Visiidi kustutamine
     * @param id kustutatava visiidi id
     */
    public void delete(Long id) {
        dentistVisitDao.delete(id);
    }

    /**
     * Visiidi kätte saamine
     * @param id otsitava visiidi id
     * @return tagastab visiidi kirje
     */
    public DentistVisitEntity get(Long id) {
        return dentistVisitDao.getByid(id);
    }

    /**
     * Visiidi muutmine
     * @param visit muudetav visiit
     */
    public void change(DentistVisitEntity visit) {
        dentistVisitDao.change(visit);
    }

}
