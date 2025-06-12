package com.example.backend_dolciluxe_java.order.model;

import com.example.backend_dolciluxe_java.product.Product;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private String id;
    @DBRef
    private Product product;
    private String productId;
    @Builder.Default
    private int quantity = 1;
    private Number price;

}
