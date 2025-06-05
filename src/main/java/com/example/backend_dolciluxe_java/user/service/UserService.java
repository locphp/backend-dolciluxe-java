package com.example.backend_dolciluxe_java.user.service;

import com.example.backend_dolciluxe_java.user.dto.PasswordUpdateRequest;
import com.example.backend_dolciluxe_java.user.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getCurrentUser();

    void updatePassword(PasswordUpdateRequest request);
}