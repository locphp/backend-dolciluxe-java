package com.example.backend_dolciluxe_java.cart;

import com.example.backend_dolciluxe_java.product.Product;
import com.example.backend_dolciluxe_java.product.ProductRepository;
import com.example.backend_dolciluxe_java.user.User;
import com.example.backend_dolciluxe_java.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null) {
            CartItem item = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .build();

            cart = Cart.builder()
                    .user(user)
                    .items(List.of(item))
                    .build();
        } else {
            // Tìm trong cart items
            Optional<CartItem> existingItemOpt = cart.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                CartItem existingItem = existingItemOpt.get();
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                CartItem newItem = CartItem.builder()
                        .product(product)
                        .quantity(quantity)
                        .build();
                cart.getItems().add(newItem);
            }
        }

        return cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart updateCartItem(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        item.setQuantity(quantity);

        return cartRepository.save(cart);
    }

    public Cart deleteCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        return cartRepository.save(cart);
    }

    public Cart deleteManyCartItem(Long userId, List<Long> productIds) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> productIds.contains(item.getProduct().getId()));

        return cartRepository.save(cart);
    }
}
