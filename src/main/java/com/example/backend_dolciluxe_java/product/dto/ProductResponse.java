package com.example.backend_dolciluxe_java.product.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProductResponse {
    private String id;
    private String productName;
    private String description;
    private String imageLink;
    private String productType;
    private Integer quantity;
    private Integer price;
    private Boolean isDeleted;
    private Instant deletedAt;
    private Instant createdAt;
    private Instant updatedAt;
}