package com.example.backend_dolciluxe_java.cart.dto;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private String productId;
    private int quantity;

}
