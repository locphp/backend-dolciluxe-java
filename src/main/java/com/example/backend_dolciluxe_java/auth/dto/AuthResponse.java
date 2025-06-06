package com.example.backend_dolciluxe_java.auth.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AuthResponse {
    private int code;
    private String message;
    private String accessToken;
    private String refreshToken;
    private Object data;
}