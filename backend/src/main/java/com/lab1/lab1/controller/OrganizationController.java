package com.lab1.lab1.controller;

import com.lab1.lab1.model.entities.Organization;
import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.service.OrganizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationController {
    @Inject
    private OrganizationService organizationService;

    @Context
    private SecurityContext securityContext;

    @GET
    public Response getOrganizations(@QueryParam("page") @DefaultValue("1") int page,
                                     @QueryParam("size") @DefaultValue("10") int size) {
        List<Organization> organization = organizationService.getAllOrganizations(page, size);
        return Response.ok(organization).build();
    }

    @GET
    @Path("/{id}")
    public Response getOrganizationsById(@PathParam("id") int id) {
        Organization organization = organizationService.getOrganizationById(id);
        if (organization != null) {
            return Response.ok(organization).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createPerson(Organization organization) {
        User user = (User) securityContext.getUserPrincipal();
        try {
            organizationService.createOrganization(organization, user);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED).entity(organization).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrganization(@PathParam("id") int id, Organization updatedOrganization) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            updatedOrganization.setId(id);
            organizationService.updateOrganization(updatedOrganization, user);
            return Response.ok(updatedOrganization).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrganization(@PathParam("id") int id) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            organizationService.deleteOrganization(id, user);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }
}
