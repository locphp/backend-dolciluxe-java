package com.example.backend_dolciluxe_java.product.producttype;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductTypeRepository extends MongoRepository<ProductType, ObjectId> {
    Optional<ProductType> findByTypeName(String typeName);
    boolean existsByTypeName(String typeName);

    List<ProductType> findByIsDeletedFalse();
    List<ProductType> findByIsDeletedTrue();
}
