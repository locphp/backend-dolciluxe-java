package com.example.backend_dolciluxe_java.auth.service.impl;

import com.example.backend_dolciluxe_java.user.User;
import com.example.backend_dolciluxe_java.user.repository.UserRepository;
import com.example.backend_dolciluxe_java.auth.dto.*;
import com.example.backend_dolciluxe_java.auth.service.AuthService;
import com.example.backend_dolciluxe_java.user.util.PasswordUtil;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.JwtException;
// import io.jsonwebtoken.Jwts;

import com.example.backend_dolciluxe_java.auth.service.JwtService;
import com.example.backend_dolciluxe_java.cart.model.Cart;
import java.util.Map;
import java.util.Optional;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    // private final CartRepository cartRepository;

    // private final PasswordUtil passwordEncoder;
    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isPresent()) {
                return ResponseEntity.status(400).body("Email already exists!");
            }

            // String avatar = generateAvatar(request.getName());

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(PasswordUtil.hashPassword(request.getPassword()));

            userRepository.save(user);

            Cart cart = new Cart();
            cart.setUser(user);
            // cartRepository.save(cart);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 201);
            response.put("message", "User registered successfully!");
            // response.put("user", Map.of("avatar", user.getAvatar()));

            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "code", 500,
                    "message", "Server error",
                    "error", e.getMessage()));
        }

        // return ResponseEntity.status(201).body("User registered successfully!");
    }

    @Override
    public ResponseEntity<?> login(AuthRequest request) {

        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElse(null);
            if (user == null) {
                return ResponseEntity.status(400).body(Map.of(
                        "code", 400,
                        "message", "Email does not exist!"));
            }

            if (!user.isActive()) {
                return ResponseEntity.status(403).body(Map.of(
                        "code", 403,
                        "message", "Tài khoản của bạn đã bị khóa"));
            }

            if (user.isDeleted()) {
                return ResponseEntity.status(400).body(Map.of(
                        "code", 400,
                        "message", "Account has been deleted!"));
            }

            boolean isMatch = PasswordUtil.matchPassword(request.getPassword(), user.getPassword());
            if (!isMatch) {
                return ResponseEntity.status(400).body(Map.of(
                        "code", 400,
                        "message", "Invalid password!"));
            }

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            Map<String, Object> data = new HashMap<>();
            data.put("_id", user.getId());
            data.put("name", user.getName());
            data.put("email", user.getEmail());
            Optional.ofNullable(user.getAvatar()).ifPresent(avatar -> data.put("avatar", avatar));
            Optional.ofNullable(user.getPhone()).ifPresent(phone -> data.put("phone", phone));
            data.put("isAdmin", user.isAdmin());
            data.put("isActive", user.isActive());

            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "Login successfully!",
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "data", data));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "code", 500,
                    "message", "Internal server error!",
                    "error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Refresh Token is required!"));
        }

        boolean isValid = jwtService.validateToken(refreshToken);
        if (!isValid) {
            return ResponseEntity.status(403).body(Map.of("message", "Invalid Refresh Token!"));
        }

        try {
            // Giải mã token để lấy subject (username)
            String userId = jwtService.extractUserId(refreshToken);
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid token!"));
            }
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Tạo access token mới
            String newAccessToken = jwtService.generateAccessToken(user);

            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "New access token generated successfully!",
                    "accessToken", newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Server error"));
        }
    }

    @Override
    public ResponseEntity<?> logout() {
        try {
            // You can access the authenticated user using Spring Security
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(new MessageResponse(401, "User not authenticated"));
            }

            // Clear the security context
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(new MessageResponse(200, "Logout successful!"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new MessageResponse(500, "Logout failed", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest request) {
        // TODO: implement forgot password logic
        return ResponseEntity.ok("Password reset email sent");
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {
        // TODO: implement reset password logic
        return ResponseEntity.ok("Password reset successfully");
    }
}
