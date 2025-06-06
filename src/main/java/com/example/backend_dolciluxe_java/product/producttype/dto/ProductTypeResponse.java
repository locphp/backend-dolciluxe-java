package com.example.backend_dolciluxe_java.product.producttype.dto;

import lombok.*;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTypeResponse {
    private String id;
    private String typeName;
    private String description;
}
