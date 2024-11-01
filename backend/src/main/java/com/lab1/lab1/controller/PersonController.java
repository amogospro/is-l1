package com.lab1.lab1.controller;

import com.lab1.lab1.model.dto.PersonDTO;
import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.model.mapper.PersonMapper;
import com.lab1.lab1.service.PersonService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.stream.Collectors;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {
    @Inject
    private PersonService personService;

    @Context
    private SecurityContext securityContext;

    @GET
    public Response getPersons(@QueryParam("page") @DefaultValue("1") int page,
                               @QueryParam("size") @DefaultValue("10") int size) {
        List<Person> persons = personService.getAllPersons(page, size);
        List<PersonDTO>personsDTO = persons.stream()
                .map(PersonMapper::toDTO)
                .collect(Collectors.toList());
        return Response.ok(personsDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response getPersonsById(@PathParam("id") int id) {
        Person person = personService.getPersonById(id);
        if (person != null) {
            return Response.ok(PersonMapper.toDTO(person)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createPerson(Person person) {
        User user = (User) securityContext.getUserPrincipal();
        try {
            personService.createPerson(person, user);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") int id, Person updatedPerson) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            updatedPerson.setId(id);
            personService.updatePerson(updatedPerson, user);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            personService.deletePerson(id, user);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }
}
