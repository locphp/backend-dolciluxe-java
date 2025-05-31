package com.example.backend_dolciluxe_java.auth.service.impl;

import com.example.backend_dolciluxe_java.user.User;
import com.example.backend_dolciluxe_java.user.UserRepository;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        new ArrayList<>() // Quyền hạn (authorities) - tạm thời để trống
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}