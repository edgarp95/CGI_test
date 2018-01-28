package com.cgi.dentistapp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.cgi.dentistapp.dao.entity.DentistVisitEntity;
import java.util.List;

@Repository
public class DentistVisitDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(DentistVisitEntity visit) {
        entityManager.persist(visit);
    }

    public List<DentistVisitEntity> getAllVisits() {
        return entityManager.createQuery("SELECT e FROM DentistVisitEntity e").getResultList();
    }

    /**
     * Visiidi kätte saamine id abil
     * @param id visiidi id
     * @return tagastab leitud visiidi
     */
    public DentistVisitEntity getByid(Long id) {
        // klassi kirjet vaja, et ta tuvastaks andmebaasiobjekti õigesti
        return entityManager.createQuery("SELECT e FROM DentistVisitEntity e WHERE e.id=" + id, DentistVisitEntity.class).getSingleResult();
    }

    /**
     * Visiidi kustutamine
     * @param id kustutatava visiidi id
     */
    public void delete(Long id) {
        entityManager.remove(getByid(id));
    }

    /**
     * Visiidi muutmine
     * @param visit muudetav visiit
     */
    public void change(DentistVisitEntity visit) {
        entityManager.merge(visit);
    }

}
