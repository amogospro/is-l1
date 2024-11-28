package com.lab1.lab1.model.mapper;

import com.lab1.lab1.model.dto.PersonDTO;
import com.lab1.lab1.model.entities.Person;

import java.util.Date;

public class PersonMapper {
    public static PersonDTO toDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setName(person.getName());
        personDTO.setEyeColor(person.getEyeColor());
        personDTO.setHairColor(person.getHairColor());
        personDTO.setLocation(person.getLocation());
        personDTO.setBirthday(person.getBirthday());
        personDTO.setNationality(person.getNationality());
        personDTO.setUserOwner(UserMapper.toDTO(person.getUserOwner()));
        personDTO.setUpdatedAt(person.getUpdatedAt());
        return personDTO;
    }

    public static Person toEntity(PersonDTO personDTO) {
        Person person = new Person();
        person.setId(personDTO.getId());
        person.setName(personDTO.getName());
        person.setEyeColor(personDTO.getEyeColor());
        person.setHairColor(personDTO.getHairColor());
        person.setLocation(personDTO.getLocation());
        person.setBirthday(personDTO.getBirthday());
        person.setNationality(personDTO.getNationality());
        person.setUserOwner(UserMapper.toEntity(personDTO.getUserOwner()));
        person.setUpdatedAt(personDTO.getUpdatedAt());

        if (person.getUpdatedAt() == null) {
            person.setUpdatedAt(new Date());
        }

        return person;
    }
}
