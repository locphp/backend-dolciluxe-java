package com.example.backend_dolciluxe_java.user.dto;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String currentPassword;
    private String newPassword;
}