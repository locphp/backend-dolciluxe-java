package com.example.backend_dolciluxe_java.auth.service;

import com.example.backend_dolciluxe_java.auth.dto.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterRequest request);

    ResponseEntity<?> login(AuthRequest request);

    ResponseEntity<?> refreshToken(RefreshTokenRequest request);

    ResponseEntity<?> logout();

    ResponseEntity<?> forgotPassword(ForgotPasswordRequest request);

    ResponseEntity<?> resetPassword(ResetPasswordRequest request);
}