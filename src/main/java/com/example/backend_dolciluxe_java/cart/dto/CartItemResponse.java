package com.example.backend_dolciluxe_java.cart.dto;

import com.example.backend_dolciluxe_java.product.Product;
import lombok.Data;

@Data
public class CartItemResponse {
    private Product product;
    private int quantity;
    private String _id;

    public CartItemResponse(Product product, int quantity, String _id) {
        this.product = product;
        this.quantity = quantity;
        this._id = _id;
    }

    public String getId() {
        return _id;
    }

}