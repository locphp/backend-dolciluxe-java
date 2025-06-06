package com.example.backend_dolciluxe_java.user.service;

import com.example.backend_dolciluxe_java.user.dto.PasswordUpdateRequest;
import com.example.backend_dolciluxe_java.user.dto.UserUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getCurrentUser();

    ResponseEntity<?> updatePassword(PasswordUpdateRequest request);

    ResponseEntity<?> updateCurrentUser(UserUpdateRequest request);
}