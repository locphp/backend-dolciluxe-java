package com.example.backend_dolciluxe_java.user.repository;

// import com.example.backend_dolciluxe_java.user.User;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend_dolciluxe_java.user.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> findByIsDeletedFalse();
}
