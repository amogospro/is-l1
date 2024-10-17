package com.lab1.lab1.controller;

import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.service.PersonService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {
    @Inject
    private PersonService personService;

    @GET
    public Response getPersons() {
        List<Person> person = personService.getAllPersons();
        return Response.ok(person).build();
    }
}
