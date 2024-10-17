package com.lab1.lab1.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}