package com.lab1.lab1.controller;

import com.lab1.lab1.model.entities.Organization;
import com.lab1.lab1.service.OrganizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationController {
    @Inject
    private OrganizationService organizationService;

    @GET
    public Response getOrganizations() {
        List<Organization> organization = organizationService.getAllOrganizations();
        return Response.ok(organization).build();
    }
}
