package com.lab1.lab1.model.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private boolean adminRequest; // True, если пользователь хочет зарегистрироваться как администратор
}
