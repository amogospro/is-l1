package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.Organization;
import com.lab1.lab1.model.entities.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OrganizationRepository {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public Organization findById(int id) { return em.find(Organization.class, id); }

    public void create(Organization organization) { em.persist(organization); }

    @Transactional
    public void update(Organization organization) { em.merge(organization); }

    public List<Organization> findAll() {
        List<Organization> organization = em.createQuery("SELECT o FROM Organization o", Organization.class)
                .getResultList();


        return organization;
    }
}
