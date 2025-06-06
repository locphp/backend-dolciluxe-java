package com.example.backend_dolciluxe_java.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String newPassword;
    private String otp;
}
