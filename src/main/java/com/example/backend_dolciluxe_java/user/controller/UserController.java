package com.example.backend_dolciluxe_java.user.controller;

import com.example.backend_dolciluxe_java.user.dto.*;
import com.example.backend_dolciluxe_java.user.service.UserService;

// import com.example.backend_dolciluxe_java.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping("/current-user/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request) {
        return userService.updatePassword(request);
    }

    @PutMapping("/current-user/update-info")
    public ResponseEntity<?> updateCurrentUser(@RequestBody UserUpdateRequest request) {
        return userService.updateCurrentUser(request);
    }
}
