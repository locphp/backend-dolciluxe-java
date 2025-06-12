package com.example.backend_dolciluxe_java.cart.service;

import com.example.backend_dolciluxe_java.cart.dto.*;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> getCart();

    ResponseEntity<?> addToCart(AddToCartRequest request);

    ResponseEntity<?> updateCartItem(UpdateCartItemRequest request);

    ResponseEntity<?> removeFromCart(String productId);

    ResponseEntity<?> removeManyFromCart(RemoveManyFromCartRequest request);
}
