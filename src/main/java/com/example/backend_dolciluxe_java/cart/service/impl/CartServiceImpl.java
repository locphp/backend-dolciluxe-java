package com.example.backend_dolciluxe_java.cart.service.impl;

import com.example.backend_dolciluxe_java.cart.dto.*;
import com.example.backend_dolciluxe_java.cart.model.Cart;
import com.example.backend_dolciluxe_java.cart.model.CartItem;
import com.example.backend_dolciluxe_java.cart.repository.CartRepository;
import com.example.backend_dolciluxe_java.cart.service.CartService;
import com.example.backend_dolciluxe_java.common.ApiResponse;
import com.example.backend_dolciluxe_java.product.Product;
import com.example.backend_dolciluxe_java.product.ProductRepository;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
        private final CartRepository cartRepository;
        private final ProductRepository productRepository;

        private String getCurrentUserId() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal(); // userId đã được set ở JwtFilter
                }
                throw new RuntimeException("User not authenticated");
        }

        @Override
        public ResponseEntity<ApiResponse<CartDataResponse>> addToCart(AddToCartRequest request) {
                String userId = getCurrentUserId();
                // log ra để kiểm tra userId
                System.out.println("Current User ID: " + userId);
                String productId = request.getProductId();
                int quantity = request.getQuantity();

                // 1. Kiểm tra sản phẩm tồn tại
                Product product = productRepository.findBy_idAndIsDeletedFalse(new ObjectId(productId))
                                .orElseThrow(() -> new RuntimeException("Product not found"));

                // 2. Tìm giỏ hàng hiện tại
                Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
                Cart cart;

                if (cartOptional.isEmpty()) {
                        // 3. Tạo mới giỏ hàng nếu chưa có
                        CartItem newItem = new CartItem();
                        newItem.setProductId(productId);
                        newItem.setQuantity(quantity);
                        newItem.setId(new ObjectId().toString()); // Tạo ID mới

                        cart = new Cart();
                        cart.setUserId(userId);
                        cart.setItems(List.of(newItem));
                } else {
                        // 4. Cập nhật giỏ hàng hiện tại
                        cart = cartOptional.get();
                        boolean itemExists = false;

                        // Tìm sản phẩm trong giỏ hàng
                        for (CartItem item : cart.getItems()) {
                                if (item.getProductId().equals(productId)) {
                                        item.setQuantity(item.getQuantity() + quantity);
                                        itemExists = true;
                                        break;
                                }
                        }

                        // Thêm mới nếu sản phẩm chưa có trong giỏ
                        if (!itemExists) {
                                CartItem newItem = new CartItem();
                                newItem.setProductId(productId);
                                newItem.setQuantity(quantity);
                                newItem.setId(new ObjectId().toString());
                                cart.getItems().add(newItem);
                        }
                }

                // 5. Lưu giỏ hàng
                Cart savedCart = cartRepository.save(cart);

                // 6. Lấy thông tin đầy đủ để trả về
                CartDataResponse response = buildCartResponse(savedCart);

                return ResponseEntity.ok(
                                new ApiResponse<>(200, "Add to cart successfully", response));
        }

        private CartDataResponse buildCartResponse(Cart cart) {
                List<CartItemResponse> itemResponses = new ArrayList<>();

                // Lấy thông tin đầy đủ cho từng sản phẩm
                for (CartItem item : cart.getItems()) {
                        Product product = productRepository.findById(new ObjectId(item.getProductId()))
                                        .orElseThrow(() -> new RuntimeException("Product not found"));

                        itemResponses.add(new CartItemResponse(
                                        product,
                                        item.getQuantity(),
                                        item.getId()));
                }

                return new CartDataResponse(
                                cart.getId(),
                                cart.getUserId(),
                                itemResponses,
                                cart.getCreatedAt(),
                                cart.getUpdatedAt());
        }

        @Override
        public ResponseEntity<ApiResponse<CartDataResponse>> getCart() {
                String userId = getCurrentUserId();
                System.out.println("Current User ID: " + userId);

                Optional<Cart> cartOptional = cartRepository.findByUserId(userId);

                if (cartOptional.isEmpty()) {
                        CartDataResponse emptyCartResponse = new CartDataResponse(
                                        null,
                                        userId,
                                        new ArrayList<>(),
                                        new Date(),
                                        new Date());
                        return ResponseEntity.ok(new ApiResponse<>(
                                        200,
                                        "Cart is empty",
                                        emptyCartResponse));
                }

                Cart cart = cartOptional.get();

                // Chuyển đổi từ CartItem sang CartItemResponse
                List<CartItemResponse> itemResponses = cart.getItems().stream()
                                .map(item -> {
                                        Product product = productRepository.findById(new ObjectId(item.getProductId()))
                                                        .orElseThrow(() -> new RuntimeException("Product not found"));
                                        return new CartItemResponse(
                                                        product,
                                                        item.getQuantity(),
                                                        item.getId() // Giả sử CartItem có field id
                                        );
                                })
                                .collect(Collectors.toList());

                CartDataResponse cartData = new CartDataResponse(
                                cart.getId(),
                                cart.getUserId(),
                                itemResponses,
                                cart.getCreatedAt(),
                                cart.getUpdatedAt());

                return ResponseEntity.ok(new ApiResponse<>(
                                200,
                                "Get cart successfully",
                                cartData));
        }

        @Override
        public ResponseEntity<ApiResponse<CartDataResponse>> updateCartItem(UpdateCartItemRequest request) {
                String userId = getCurrentUserId();
                String productId = request.getProductId();
                int newQuantity = request.getQuantity();

                // 1. Tìm giỏ hàng của người dùng
                Cart cart = cartRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Cart not found"));

                // 2. Tìm sản phẩm trong giỏ hàng
                CartItem itemToUpdate = cart.getItems().stream()
                                .filter(item -> item.getProductId().equals(productId))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Cart item not found"));

                // 3. Cập nhật số lượng
                itemToUpdate.setQuantity(newQuantity);

                // 4. Lưu giỏ hàng
                Cart updatedCart = cartRepository.save(cart);

                // 5. Xây dựng phản hồi
                CartDataResponse response = buildCartResponse(updatedCart);

                return ResponseEntity.ok(new ApiResponse<>(
                                200,
                                "Update cart item successfully",
                                response));
        }

        @Override
        public ResponseEntity<ApiResponse<CartDataResponse>> removeFromCart(String productId) {
                String userId = getCurrentUserId();

                // 1. Tìm giỏ hàng của người dùng
                Cart cart = cartRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Cart not found"));

                // 2. Tìm sản phẩm trong giỏ hàng
                CartItem itemToRemove = cart.getItems().stream()
                                .filter(item -> item.getProductId().equals(productId))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Cart item not found"));

                // 3. Xóa sản phẩm khỏi giỏ hàng
                cart.getItems().remove(itemToRemove);

                // 4. Lưu giỏ hàng
                Cart updatedCart = cartRepository.save(cart);

                // 5. Xây dựng phản hồi
                CartDataResponse response = buildCartResponse(updatedCart);

                return ResponseEntity.ok(new ApiResponse<>(
                                200,
                                "Remove cart item successfully",
                                response));
        }

        @Override
        public ResponseEntity<ApiResponse<CartDataResponse>> removeManyFromCart(RemoveManyFromCartRequest request) {
                String userId = getCurrentUserId();

                // 1. Tìm giỏ hàng của người dùng
                Cart cart = cartRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("Cart not found"));

                // 2. Xóa nhiều sản phẩm khỏi giỏ hàng
                List<String> productIds = request.getProductIds();
                cart.getItems().removeIf(item -> productIds.contains(item.getProductId()));

                // 3. Lưu giỏ hàng
                Cart updatedCart = cartRepository.save(cart);

                // 4. Xây dựng phản hồi
                CartDataResponse response = buildCartResponse(updatedCart);

                return ResponseEntity.ok(new ApiResponse<>(
                                200,
                                "Remove cart items successfully",
                                response));
        }

}
