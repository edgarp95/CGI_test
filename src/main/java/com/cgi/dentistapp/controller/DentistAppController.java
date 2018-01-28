package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.dao.entity.DentistVisitEntity;
import com.cgi.dentistapp.dto.DentistVisitDTO;
import com.cgi.dentistapp.dto.SearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;
import com.cgi.dentistapp.service.DentistVisitService;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Kontroller, mis põhimõtteliselt töötleb kogu kasutajaloogika
 */
@Controller
@EnableAutoConfiguration
public class DentistAppController extends WebMvcConfigurerAdapter {

    private static final Logger log = Logger.getLogger(DentistAppController.class);

    @Autowired
    private DentistVisitService dentistVisitService;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    /**
     * Meetod, mis avab visiidile registreerimise vaate
     *
     * @param dentistVisitDTO tühi broneeringu objekt, vaja vormi jaoks
     * @param form            mudel, kuhu paneme sisse perearstide nimed vaatesse saatmiseks
     * @return tagastab broneerimise lehe tühja vormiga
     */
    @GetMapping("/")
    public String showRegisterForm(DentistVisitDTO dentistVisitDTO, Model form) {
        log.info("Open register view");
        // Lisame vormile ka perearste, et need poleks lihtsalt htmlis olemas
        form.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());
        return "form";
    }

    /**
     * Meetod, mis töötleb kasutaja sisestatud broneeringut ja salvestab selle andmebaasi
     *
     * @param dentistVisitDTO    täidetud broneeringu objekt
     * @param bindingResult      objekt, mis sisaldab broneeringu vigu, kui need esinevad
     * @param form               mudel kuhu lisame atribuute järgmise vaate jaoks
     * @param redirectAttributes siia paneme õnnestumise sõnumeid
     * @return tagastab broneerimise tühja vormi õnnestumise sõnumiga, kui kõik õnnestus ning vigase vormi, kui ilmnes probleeme
     */
    @PostMapping("/")
    public String postRegisterForm(@Valid DentistVisitDTO dentistVisitDTO, BindingResult bindingResult, Model form, RedirectAttributes redirectAttributes) {
        log.info("Trying to register a visit");
        if (bindingResult.hasErrors()) {
            log.info("Wrong data in registration blanket!");
            // lisame perearstide nimed uuesti
            form.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());
            return "form";
        }
        if (dentistVisitService.timeIssue(dentistVisitDTO, (long) -1)) {
            log.info("Taken visit time, change it!");
            form.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());
            form.addAttribute("time_issue", "time_issue");
            return "form";

        }

        String[] time = dentistVisitDTO.getVisitClock().split(":");
        // Ühendame kuupäeva ja kellaaja
        LocalDateTime registrerTime = LocalDateTime.of(dentistVisitDTO.getVisitTime(), LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1])));

        //  Salvestame kirje andmebaasi
        dentistVisitService.addVisit(dentistVisitDTO.getDentistName(), dentistVisitDTO.getFamilyDrName(), registrerTime);

        redirectAttributes.addFlashAttribute("success_create", "success_create");
        return "redirect:/";
    }


    /**
     * Meetod, mis avab visiitide vaatamise/otsingu lehe
     *
     * @param searchDTO tühi otsingu objekt
     * @param visits    mudel kuhu paneme atribuute järgmise vaate jaoks (näiteks perearstide nimesid)
     * @return tagastab broneeringute vaatamise lehe koos tühja otsinguga ning kõikide olemasolevate broneeringutega
     */
    @GetMapping("/search")
    public String showAllregisters(SearchDTO searchDTO, Model visits) {
        log.info("Open search view");
        visits.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());
        visits.addAttribute("visits", dentistVisitService.listVisits());
        return "allVisits";
    }

    /**
     * Meetod, mis tagastab otsingukriteeriumi järgi sobivad visiidid ja kuvab neid
     *
     * @param searchDTO     täidetud otsinguobjekt
     * @param bindingResult otsinguobjekti vead, juhul kui esinevad
     * @param visits        mudel kuhu paneme atribuudid (näiteks sobivad broneeringud)
     * @return tagastab otsingu/broneeringu vaatamise lehe, uuendatud broneeringute nimekirjaga
     */
    @PostMapping("/search")
    public String search(@Valid SearchDTO searchDTO, BindingResult bindingResult, Model visits) {
        log.info("Trying to process search form");
        visits.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());

        if (bindingResult.hasErrors()) {
            log.info("Error in search form");

            visits.addAttribute("visits", dentistVisitService.listVisits());
            return "allVisits";

        }

        // leiame otsingukriteeriumitele vastavad visiidid
        List<DentistVisitEntity> listVisits = dentistVisitService.search(searchDTO);
        visits.addAttribute("visits", listVisits);
        return "allVisits";
    }

    /**
     * Demoandmete lisamine andmebaasi
     *
     * @return suunab ümber results lehele
     */
    @GetMapping("/addDemoVisits")
    public String addDemoVisits() {
        dentistVisitService.addDemoVisits();
        return "redirect:/results";
    }

    /**
     * Broneeringu kustutamine
     *
     * @param id                 broneeringu id
     * @param redirectAttributes ümbersuunamise atribuudid (siia paneme näiteks sõnumeid järgmise vaate heaks)
     * @return tagastab vastava teatega visiidi otsingu/detailvaate vaate
     */
    @GetMapping("delete/{id}")
    public String deleteVisit(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            log.info("Trying delete visit with id: " + id);
            dentistVisitService.delete(id);
            redirectAttributes.addFlashAttribute("delete_success", "delete_success");
            return "redirect:/search";

        } catch (Exception e) {
            log.info(e);
            redirectAttributes.addFlashAttribute("delete_fail", "delete_fail");
            return "redirect:/search";
        }


    }

    /**
     * Visiidi muutmise vaate avamine
     *
     * @param id                 visiidi id
     * @param dentistVisitDTO    visiidi objekt
     * @param visit              mudel, kuhu paneme atribuute järgmise vaate jaoks (näiteks perearstide nimed)
     * @param redirectAttributes sõnumite edastamiseks järgmisesse vaatesse
     * @return tagastab visiidi muutmise lehe kui õnnestus ning otsingu lehe veaga, kui visiiti pole
     */
    @GetMapping("change/{id}")
    public String changeVisitPage(@PathVariable("id") long id, DentistVisitDTO dentistVisitDTO, Model visit, RedirectAttributes redirectAttributes) {
        try {
            log.info("Opening change visit view");
            // Saadame edasi dentisVisitDTO, vaja, et see oleks õigete argumentidega, muidu see tühi
            DentistVisitEntity visitEntity = dentistVisitService.get(id);
            dentistVisitDTO.setDentistName(visitEntity.getDentistName());
            dentistVisitDTO.setFamilyDrName(visitEntity.getFamilyDr());
            dentistVisitDTO.setVisitTime(visitEntity.getVisitTime().toLocalDate());
            dentistVisitDTO.setVisitClock(visitEntity.getVisitTime().format(DateTimeFormatter.ofPattern("hh:mm")));

            visit.addAttribute("visitid",id);
            visit.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());
            return "editVisit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("not_exist", "not_exist");
            return "redirect:/search";
        }

    }

    /**
     * Visiidi muutmine
     *
     * @param id                 visiidi id
     * @param dentistVisitDTO    visiidi objekt
     * @param bindingResult      visiidi objekti vead
     * @param redirectAttributes ümbersuunamisatribuudid (sõnumite jaoks)
     * @param visit              mudel atribuutide lisamiseks (näiteks perearstid)
     * @return tagastab visiitide otsingu lehe õnnestumise korral ning visiidi muutmise lehe kui midagi läks valesti
     */
    @PostMapping("change/{id}")
    public String changeVisit(@PathVariable("id") long id, @Valid DentistVisitDTO dentistVisitDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model visit) {
        try {
            log.info("Changing visit data");
            if (bindingResult.hasErrors()) {
                log.info("Error in visit form");
                visit.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());

                return "editVisit";

            }
            //ajavea kontroll
            if (dentistVisitService.timeIssue(dentistVisitDTO, id)) {
                log.info("Taken visit time, change it!");
                visit.addAttribute("familyDoctors", dentistVisitService.getFamiliyDrs());
                visit.addAttribute("time_issue", "time_issue");
                return "editVisit";

            }


            // asendame informatsiooni "uuega"
            DentistVisitEntity visitEntity = dentistVisitService.get(id);
            visitEntity.setDentistName(dentistVisitDTO.getDentistName());
            visitEntity.setFamilyDr(dentistVisitDTO.getFamilyDrName());
            String[] time = dentistVisitDTO.getVisitClock().split(":");
            visitEntity.setVisitTime(LocalDateTime.of(dentistVisitDTO.getVisitTime(), LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]))));

            // visiidi muutmine andmebaasis
            dentistVisitService.change(visitEntity);
            redirectAttributes.addFlashAttribute("change_success", "change_success");
            return "redirect:/search";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("not_exist", "not_exist");
            return "redirect:/search";
        }

    }


}
