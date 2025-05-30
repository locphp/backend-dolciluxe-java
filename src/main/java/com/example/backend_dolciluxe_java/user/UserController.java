package com.example.backend_dolciluxe_java.user;

import com.example.backend_dolciluxe_java.user.dto.*;
// import com.example.backend_dolciluxe_java.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<UserResponse> getCurrentUser(@PathVariable String id) {
        return userService.getCurrentUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<String> updatePassword(
            @PathVariable String id,
            @RequestBody PasswordUpdateRequest request) {
        String msg = userService.updatePassword(id, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<UserResponse> toggleActive(
            @PathVariable String id,
            @RequestParam boolean isActive) {
        return ResponseEntity.ok(userService.toggleActive(id, isActive));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateRole(
            @PathVariable String id,
            @RequestBody RoleUpdateRequest request) {
        return ResponseEntity.ok(
                userService.updateUserRole(
                        request.getAdminId(),
                        request.getAdminPassword(),
                        id,
                        request.getIsAdmin()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> softDelete(@PathVariable String id) {
        return ResponseEntity.ok(userService.softDeleteUser(id));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.restoreUser(id));
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<Void> deleteUserPermanently(@PathVariable String id) {
        userService.deleteUserPermanently(id);
        return ResponseEntity.noContent().build();
    }
}
