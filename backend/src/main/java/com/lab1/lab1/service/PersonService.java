package com.lab1.lab1.service;

import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.repository.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PersonService {
    @Inject
    private PersonRepository personRepository;
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }
}
