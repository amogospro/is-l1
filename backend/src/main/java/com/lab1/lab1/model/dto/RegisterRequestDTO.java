package com.lab1.lab1.model.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private boolean adminRequest; // True, если пользователь хочет зарегистрироваться как администратор
}
