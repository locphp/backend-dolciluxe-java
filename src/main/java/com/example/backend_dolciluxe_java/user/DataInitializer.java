package com.example.backend_dolciluxe_java.user;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor

public class DataInitializer {
    private final UserRepository userRepo;

    @PostConstruct // Tự động chạy khi ứng dụng khởi động
    public void init() {
        if (userRepo.count() == 0) {
            User testUser = User.builder()
                    .name("Admin")
                    .email("admin@example.com")
                    .build();
            userRepo.save(testUser);
            System.out.println("Đã tạo dữ liệu user mẫu!");
        }
    }
}
