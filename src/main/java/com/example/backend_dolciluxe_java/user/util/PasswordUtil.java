package com.example.backend_dolciluxe_java.user.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    public static boolean matchPassword(String plain, String hashed) {
        return encoder.matches(plain, hashed);
    }
}
