package com.example.backend_dolciluxe_java.auth.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class MessageResponse {
    private int code;
    private String message;
    private String error;

    public MessageResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.error = null;
    }
}
