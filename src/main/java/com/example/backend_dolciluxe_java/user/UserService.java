package com.example.backend_dolciluxe_java.user;

import com.example.backend_dolciluxe_java.user.dto.UserRequest;
import com.example.backend_dolciluxe_java.user.dto.UserResponse;
import com.example.backend_dolciluxe_java.user.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findByIsDeletedFalse()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> getUserById(String id) {
        return userRepository.findById(id)
                .filter(user -> !user.getIsDeleted())
                .map(this::convertToResponse);
    }

    public Optional<UserResponse> getCurrentUser(String userId) {
        return getUserById(userId);
    }

    public UserResponse updateUser(String id, UserRequest updateData) {
        return userRepository.findById(id).map(user -> {
            user.setName(updateData.getName());
            user.setPhone(updateData.getPhone());
            user.setEmail(updateData.getEmail());
            user.setUpdatedAt(new Date());
            return convertToResponse(userRepository.save(user));
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String updatePassword(String userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!PasswordUtil.matchPassword(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(PasswordUtil.hashPassword(newPassword));
        user.setUpdatedAt(new Date());
        userRepository.save(user);
        return "Password updated successfully";
    }

    public UserResponse softDeleteUser(String userId) {
        return userRepository.findById(userId).map(user -> {
            user.setIsDeleted(true);
            return convertToResponse(userRepository.save(user));
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse restoreUser(String userId) {
        return userRepository.findById(userId).map(user -> {
            user.setIsDeleted(false);
            return convertToResponse(userRepository.save(user));
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUserPermanently(String userId) {
        userRepository.deleteById(userId);
    }

    public UserResponse toggleActive(String userId, boolean isActive) {
        return userRepository.findById(userId).map(user -> {
            user.setIsActive(isActive);
            return convertToResponse(userRepository.save(user));
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse updateUserRole(String adminId, String adminPassword, String targetUserId, boolean isAdmin) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!PasswordUtil.matchPassword(adminPassword, admin.getPassword()))
            throw new RuntimeException("Incorrect admin password");

        if (adminId.equals(targetUserId))
            throw new RuntimeException("Cannot change your own role");

        return userRepository.findById(targetUserId).map(user -> {
            user.setIsAdmin(isAdmin);
            user.setUpdatedAt(new Date());
            return convertToResponse(userRepository.save(user));
        }).orElseThrow(() -> new RuntimeException("Target user not found"));
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .isAdmin(user.getIsAdmin())
                .isActive(user.getIsActive())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    private User convertFromRequest(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .avatar(request.getAvatar())
                .build();
    }
}
