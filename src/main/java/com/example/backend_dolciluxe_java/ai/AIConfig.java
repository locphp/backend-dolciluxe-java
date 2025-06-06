package com.example.backend_dolciluxe_java.ai;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;

@Configuration
public class AIConfig {
    // @Bean
    // public RestTemplate restTemplate() {
    //     return new RestTemplate();
    // }
    @Value("${cloudinary.upload.cloud_name}")
    private String name;
    @Value("${cloudinary.upload.api_key}")
    private String key;
    @Value("${cloudinary.upload.api_secret}")
    private String secret;
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = Map.of(
            "cloud_name", name,
            "api_key", key,
            "api_secret", secret
        );
        return new Cloudinary(config);
    }
}

