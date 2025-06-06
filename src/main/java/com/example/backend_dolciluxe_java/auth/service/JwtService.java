package com.example.backend_dolciluxe_java.auth.service;

import com.example.backend_dolciluxe_java.user.User;

public interface JwtService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateToken(String token);

    String extractUserId(String token);
}
