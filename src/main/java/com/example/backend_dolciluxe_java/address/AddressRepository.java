package com.example.backend_dolciluxe_java.address;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;
import java.util.List;

public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    List<Address> findByUser(ObjectId user);
    long countByUser(ObjectId user);
}