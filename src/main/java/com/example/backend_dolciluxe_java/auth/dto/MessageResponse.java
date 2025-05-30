package com.example.backend_dolciluxe_java.auth.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private int code;
}
