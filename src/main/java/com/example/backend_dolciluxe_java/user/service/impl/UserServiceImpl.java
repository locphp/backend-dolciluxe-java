package com.example.backend_dolciluxe_java.user.service.impl;

import com.example.backend_dolciluxe_java.user.dto.PasswordUpdateRequest;
import com.example.backend_dolciluxe_java.user.dto.UserUpdateRequest;
import com.example.backend_dolciluxe_java.common.ApiResponse;
import com.example.backend_dolciluxe_java.user.User;
import com.example.backend_dolciluxe_java.user.repository.UserRepository;
import com.example.backend_dolciluxe_java.user.service.UserService;
import com.example.backend_dolciluxe_java.user.util.PasswordUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }

    // kiểm tra có phải là admin hay không (isAdmin = true)
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = userRepository.findById(getCurrentUserId()).orElse(null);
            return user != null && user.isAdmin();
        }
        return false;
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body(
                    new ApiResponse<>(403, "You do not have permission to update order status", null));
        }
        List<User> users = userRepository.findAll();
        List<User> userDtos = users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @Override
    public ResponseEntity<?> getCurrentUser() {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(404, "User not found", null));
        }
        return ResponseEntity.ok(
                new ApiResponse<>(200, "User retrieved successfully", mapToDto(user)));
    }

    @Override
    public ResponseEntity<?> updatePassword(PasswordUpdateRequest request) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(404, "User not found", null));
        }

        if (!PasswordUtil.matchPassword(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(400).body(
                    new ApiResponse<>(400, "Old password is incorrect", null));
        }

        user.setPassword(PasswordUtil.hashPassword(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Password updated successfully", null));
    }

    private User mapToDto(User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
                .isDeleted(user.isDeleted())
                .isActive(user.isActive())
                .build();
    }

    @Override
    public ResponseEntity<?> updateCurrentUser(UserUpdateRequest request) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(
                    new ApiResponse<>(404, "User not found", null));
        }

        // Cập nhật thông tin người dùng
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        // if (request.getAvatar() != null) {
        // user.setAvatar(request.getAvatar());
        // }

        userRepository.save(user);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "User updated successfully", mapToDto(user)));
    }
}
