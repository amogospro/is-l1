package com.lab1.lab1.repository;

import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PersonRepository {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public Person findById(int id) { return em.find(Person.class, id); }

    @Transactional
    public void create(Person person) { em.persist(person); }

    @Transactional
    public void delete(Person person) { em.remove(em.contains(person) ? person : em.merge(person));}

    @Transactional
    public void update(Person person) { em.merge(person); }

    public List<Person> findAll(int offset, int limit) {
        List<Person> persons = em.createQuery("SELECT p FROM Person p", Person.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        persons.forEach(person -> {
            User owner = person.getUserOwner();
            if (owner != null) {
                owner.setPasswordHash(null); // Очищаем значение passwordHash
            }
        });

        return persons;
    }

    public Person findByName(String name) {
        try {
            return em.createQuery("SELECT p FROM Person p WHERE p.name = :name", Person.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
