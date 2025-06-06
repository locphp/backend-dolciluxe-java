package com.example.backend_dolciluxe_java.order.repository;

import com.example.backend_dolciluxe_java.order.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    // findby user
    Optional<List<Order>> findByUser(String userId);

    // void deleteByUserId(String userId);
}