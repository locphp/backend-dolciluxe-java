package com.example.backend_dolciluxe_java.user.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String name;
    private String phone;
    private String avatar;
}
