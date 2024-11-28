package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.Organization;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.User;
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

    @Transactional
    public void create(Organization organization) { em.persist(organization); }

    @Transactional
    public void delete(Organization organization) { em.remove(em.contains(organization) ? organization : em.merge(organization));}

    @Transactional
    public void update(Organization organization) { em.merge(organization); }

    public List<Organization> findAll(int offset, int limit) {
        List<Organization> organizations = em.createQuery("SELECT o FROM Organization o", Organization.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        organizations.forEach(organization -> {
            User owner = organization.getUserOwner();
            if (owner != null) {
                owner.setPasswordHash(null); // Очищаем значение passwordHash
            }
        });


        return organizations;
    }

    public Organization findByName(String name) {
        try {
            return em.createQuery("SELECT o FROM Organization o WHERE o.name = :name", Organization.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
