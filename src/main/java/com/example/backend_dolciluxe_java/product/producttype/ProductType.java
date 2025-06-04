package com.example.backend_dolciluxe_java.product.producttype;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Document(collection = "productType")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductType {
    @Id
    private ObjectId _id;

    private String typeName;
    private String description;

    @Builder.Default
    private Boolean isDeleted = false;
    private Instant deletedAt;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
