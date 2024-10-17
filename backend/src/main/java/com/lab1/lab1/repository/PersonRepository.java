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

    public void create(Person person) { em.persist(person); }

    @Transactional
    public void update(Person person) { em.merge(person); }

    public List<Person> findAll() {
        List<Person> person = em.createQuery("SELECT p FROM Person p", Person.class)
                .getResultList();


        return person;
    }

}
