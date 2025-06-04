package com.example.backend_dolciluxe_java.user.service.impl;

import com.example.backend_dolciluxe_java.user.dto.PasswordUpdateRequest;
import com.example.backend_dolciluxe_java.user.User;
import com.example.backend_dolciluxe_java.user.repository.UserRepository;
import com.example.backend_dolciluxe_java.user.service.UserService;
import com.example.backend_dolciluxe_java.user.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    @Override
    public void updatePassword(PasswordUpdateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!PasswordUtil.matchPassword(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(PasswordUtil.hashPassword(request.getNewPassword()));
        userRepository.save(user);
    }

    private User mapToDto(User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                // .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }
}
