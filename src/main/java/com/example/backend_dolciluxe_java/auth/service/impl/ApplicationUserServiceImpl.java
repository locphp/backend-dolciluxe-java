package com.example.backend_dolciluxe_java.auth.service.impl;

import com.example.backend_dolciluxe_java.user.User;
import com.example.backend_dolciluxe_java.user.repository.UserRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ApplicationUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrId) throws UsernameNotFoundException {
        // Thử tìm user bằng ID trước (nếu usernameOrId là ObjectId hợp lệ)
        if (isValidObjectId(usernameOrId)) {
            return userRepository.findById(usernameOrId)
                    .map(user -> buildUserDetails(user))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + usernameOrId));
        }

        // Nếu không phải ID, tìm bằng email
        return userRepository.findByEmail(usernameOrId)
                .map(user -> buildUserDetails(user))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + usernameOrId));
    }

    private boolean isValidObjectId(String id) {
        // Kiểm tra nếu id là ObjectId hợp lệ (dùng cho MongoDB)
        return ObjectId.isValid(id); // Import từ org.bson.types.ObjectId
        // Hoặc kiểm tra UUID nếu dùng PostgreSQL, etc.
    }

    private UserDetails buildUserDetails(User user) {
        // Chuyển đổi User thành UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.isActive())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .authorities(new ArrayList<>()) // Thêm quyền nếu cần
                .build();
    }
}