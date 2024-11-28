package com.lab1.lab1.controller;

import com.lab1.lab1.model.dto.PersonDTO;
import com.lab1.lab1.model.entities.ImportHistory;
import com.lab1.lab1.model.entities.Person;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.model.mapper.PersonMapper;
import com.lab1.lab1.service.ImportService;
import com.lab1.lab1.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RESTful API for managing movies
 */
@Path("/import")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImportController {

    @Inject
    private ImportService importService;

    @Context
    private SecurityContext securityContext;

    @POST
    public Response importObjects(InputStream fileInputStream) {
        User user = (User) securityContext.getUserPrincipal();
        try {
            importService.importObjects(fileInputStream, user);  // Передаем поток данных
            return Response.status(Response.Status.CREATED).entity("Objects imported successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            importService.logImportHistory(user.getUsername(), "FAIL", 0);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getImportHistory() {
        User user = (User) securityContext.getUserPrincipal();
        try {
            List<ImportHistory> history = importService.getImportHistory(user);
            return Response.ok(history).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
