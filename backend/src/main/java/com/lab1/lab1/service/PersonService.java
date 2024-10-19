package com.lab1.lab1.service;

import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.Product;
import com.lab1.lab1.model.entities.Role;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.repository.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PersonService {
    @Inject
    private PersonRepository personRepository;

    @Transactional
    public void createPerson(Person person, User user) {
        person.setUserOwner(user);
        personRepository.create(person);
    }

    public Person getPersonById(int id) { return personRepository.findById(id); }

    @Transactional
    public void updatePerson(Person person, User user) throws Exception {
        Person currentPerson = getPersonById(person.getId());

        if (currentPerson.getUserOwner().equals(user) || user.getRole() == Role.ADMIN) {
            currentPerson.setName(person.getName());
            currentPerson.setEyeColor(person.getEyeColor());
            currentPerson.setHairColor(person.getHairColor());
            currentPerson.setLocation(person.getLocation());
            currentPerson.setBirthday(person.getBirthday());
            currentPerson.setNationality(person.getNationality());
            personRepository.update(currentPerson);
        } else {
            throw new Exception("Access Denied");
        }

    }

    public void deletePerson(int id, User user) throws Exception {
        Person person = personRepository.findById(id);
        if (person != null) {
            // Check if user is owner or admin
            if (person.getUserOwner().equals(user) || user.getRole() == Role.ADMIN) {
                // Check for related objects
                // If related objects exist, throw exception
                personRepository.delete(person);
            } else {
                throw new Exception("Access Denied");
            }
        }
    }

    public List<Person> getAllPersons(int page, int size) {
        int offset = (page - 1) * size;
        return personRepository.findAll(offset, size);
    }
}
