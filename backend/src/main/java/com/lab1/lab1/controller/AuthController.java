package com.lab1.lab1.controller;

import com.lab1.lab1.model.dto.AuthResponseDTO;
import com.lab1.lab1.model.dto.ErrorResponseDTO;
import com.lab1.lab1.model.dto.LoginRequestDTO;
import com.lab1.lab1.model.dto.RegisterRequestDTO;
import com.lab1.lab1.model.entities.Role;
import com.lab1.lab1.model.entities.User;
import com.lab1.lab1.service.AuthService;
import jakarta.ws.rs.Path;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private AuthService authService;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/pending")
    public Response getPendingAdmins() {
        List<User> user = authService.getAllPendingAdmins();
        return Response.ok(user).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO request) throws NoSuchAlgorithmException {
        User user = authService.authenticate(request.getUsername(), request.getPassword());
        if (user != null) {
            String token = authService.generateToken(user);
            return Response.ok(new AuthResponseDTO(token)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequestDTO request) {
        try {
            authService.register(request.getUsername(), request.getPassword(), request.isAdminRequest());
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Endpoint for admins to approve pending admin requests
    @POST
    @Path("/approve-admin/{userId}")
    public Response approveAdmin(@PathParam("userId") Long userId) {
        try {
            // Получаем текущего пользователя из SecurityContext
            Principal principal = securityContext.getUserPrincipal();
            if (principal == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Необходима авторизация").build();
            }

            User admin = authService.getUserByUsername(principal.getName());

            // Проверяем, что текущий пользователь является администратором
            if (admin.getRole() != Role.ADMIN) {
                return Response.status(Response.Status.FORBIDDEN).entity("Доступ запрещён").build();
            }

            authService.approveAdminRequest(userId, admin);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDTO(e.getMessage()))
                    .build();
        }
    }
}

