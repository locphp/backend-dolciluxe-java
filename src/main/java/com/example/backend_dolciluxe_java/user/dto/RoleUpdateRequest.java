package com.example.backend_dolciluxe_java.user.dto;

import lombok.Data;

@Data
public class RoleUpdateRequest {
    private String adminId;
    private String adminPassword;
    private Boolean isAdmin;
}
