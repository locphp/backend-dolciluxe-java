package com.example.backend_dolciluxe_java.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private Boolean isAdmin;
    private Boolean isActive;
    private Boolean isDeleted;
}
