package com.example.backend_dolciluxe_java.user.controller;

import com.example.backend_dolciluxe_java.common.ApiResponse;
import com.example.backend_dolciluxe_java.user.dto.*;
import com.example.backend_dolciluxe_java.user.service.UserService;
import com.example.backend_dolciluxe_java.user.User;

// import com.example.backend_dolciluxe_java.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfully fetched user list!", users));
    }

    @GetMapping("/current-user")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(200, "Successfully fetched current user", user));
    }

    @PutMapping("/current-user/update-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(@RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Password updated successfully", null));
    }

}
