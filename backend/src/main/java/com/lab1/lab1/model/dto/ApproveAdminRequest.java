package com.lab1.lab1.model.dto;

import lombok.Data;

@Data
public class ApproveAdminRequest {
    private Long userId; // Идентификатор пользователя, которого нужно одобрить как администратора
}