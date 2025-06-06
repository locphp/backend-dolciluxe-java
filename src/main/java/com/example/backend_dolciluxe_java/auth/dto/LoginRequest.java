package com.example.backend_dolciluxe_java.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
