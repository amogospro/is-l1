package com.lab1.lab1.model.dto;

import lombok.Data;

@Data
public class ApproveAdminRequestDTO {
    private Long userId; // Идентификатор пользователя, которого нужно одобрить как администратора
}