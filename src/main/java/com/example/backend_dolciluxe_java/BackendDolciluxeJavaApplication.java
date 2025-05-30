package com.example.backend_dolciluxe_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BackendDolciluxeJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendDolciluxeJavaApplication.class, args);
	}

}
