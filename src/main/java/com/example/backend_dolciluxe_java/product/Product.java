package com.example.backend_dolciluxe_java.product;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private ObjectId _id;

    private String productName;
    private String description;
    private String imageLink;
    private ObjectId productType;

    private Integer quantity;
    private Integer price;

    @Builder.Default
    private Boolean isDeleted = false;
    private Instant deletedAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public String getId() {
        return _id != null ? _id.toHexString() : null;
    }

    public void setId(String id) {
        this._id = id != null ? new ObjectId(id) : null;
    }
}