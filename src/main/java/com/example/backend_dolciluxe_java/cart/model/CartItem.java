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
    private String id;

    @DBRef
    private Product product;

    @Builder.Default
    private int quantity = 1;

    public String getId() {
        return id;
    }

    // setId
    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return product != null ? product.getId() : null;
    }

    public void setProductId(String productId) {
        if (product == null) {
            product = new Product();
        }
        product.setId(productId);
    }
}
