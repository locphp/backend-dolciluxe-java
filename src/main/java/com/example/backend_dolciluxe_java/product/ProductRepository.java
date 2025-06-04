package com.example.backend_dolciluxe_java.product;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    List<Product> findByIsDeletedFalse();
    List<Product> findByIsDeletedTrue();
    List<Product> findByProductTypeAndIsDeletedFalse(ObjectId productType);
    boolean existsByProductType(ObjectId productType);
}