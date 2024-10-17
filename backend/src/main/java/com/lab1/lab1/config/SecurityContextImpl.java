package com.lab1.lab1.config;

import com.lab1.lab1.model.entities.User;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class SecurityContextImpl implements SecurityContext {

    private final User user;
    private final boolean isSecure;

    public SecurityContextImpl(User user, boolean isSecure) {
        this.user = user;
        this.isSecure = isSecure;
    }


    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user != null && user.getRole().name().equalsIgnoreCase(role);
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
