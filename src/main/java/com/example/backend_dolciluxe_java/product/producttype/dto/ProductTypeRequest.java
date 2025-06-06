package com.example.backend_dolciluxe_java.product.producttype.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeRequest {
    private String typeName;
    private String description;
}
