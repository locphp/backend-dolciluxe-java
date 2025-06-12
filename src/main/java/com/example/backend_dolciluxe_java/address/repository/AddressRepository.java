package com.example.backend_dolciluxe_java.address.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;

import com.example.backend_dolciluxe_java.address.Address;

import java.util.List;

public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    List<Address> findByUser(String userId);

    Address findById(String addressId);

    void deleteById(String addressId);

    long countByUser(String userId);

}