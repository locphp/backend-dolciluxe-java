package com.example.backend_dolciluxe_java.cart.model;

import com.example.backend_dolciluxe_java.product.Product;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @DBRef
    private Product product;

    @Builder.Default
    private int quantity = 1;
}
