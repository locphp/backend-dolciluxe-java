package com.example.backend_dolciluxe_java.cart;

import com.example.backend_dolciluxe_java.cart.service.CartService;
import com.example.backend_dolciluxe_java.cart.dto.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart() {
        return cartService.getCart();
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request);
    }

    @PutMapping
    public ResponseEntity<?> updateCartItem(@RequestBody UpdateCartItemRequest request) {
        return cartService.updateCartItem(request);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String productId) {
        return cartService.removeFromCart(productId);
    }

    @DeleteMapping("/delete-many")
    public ResponseEntity<?> removeManyFromCart(@RequestBody RemoveManyFromCartRequest request) {
        return cartService.removeManyFromCart(request);
    }

}
