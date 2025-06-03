package com.example.backend_dolciluxe_java.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        Cart result = cartService.addToCart(userDetails.getId(), productId, quantity);
        return ResponseEntity.ok(new ApiResponse(200, "Add to cart successfully", result));
    }

    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Cart cart = cartService.getCart(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse(200, "Get cart successfully", cart));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        Cart result = cartService.updateCartItem(userDetails.getId(), productId, quantity);
        return ResponseEntity.ok(new ApiResponse(200, "Update cart successfully", result));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteCartItem(@AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long productId) {
        Cart result = cartService.deleteCartItem(userDetails.getId(), productId);
        return ResponseEntity.ok(new ApiResponse(200, "Delete item successfully", result));
    }

    @DeleteMapping("/deleteMany")
    public ResponseEntity<?> deleteManyCartItem(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody List<Long> productIds) {
        Cart result = cartService.deleteManyCartItem(userDetails.getId(), productIds);
        return ResponseEntity.ok(new ApiResponse(200, "Delete many items successfully", result));
    }
}
