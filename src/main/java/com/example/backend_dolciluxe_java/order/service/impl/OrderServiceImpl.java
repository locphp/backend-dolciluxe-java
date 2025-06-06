package com.example.backend_dolciluxe_java.order.service.impl;

import com.example.backend_dolciluxe_java.address.Address;
import com.example.backend_dolciluxe_java.address.repository.AddressRepository;
import com.example.backend_dolciluxe_java.cart.model.Cart;
import com.example.backend_dolciluxe_java.cart.model.CartItem;
import com.example.backend_dolciluxe_java.cart.repository.CartRepository;
import com.example.backend_dolciluxe_java.common.ApiResponse;
import com.example.backend_dolciluxe_java.order.dto.CreateOrderRequest;
import com.example.backend_dolciluxe_java.order.dto.UpdateOrderStatusRequest;
import com.example.backend_dolciluxe_java.order.model.Order;
import com.example.backend_dolciluxe_java.user.User;
import com.example.backend_dolciluxe_java.order.model.OrderItem;
import com.example.backend_dolciluxe_java.order.repository.OrderRepository;
import com.example.backend_dolciluxe_java.order.service.OrderService;
import com.example.backend_dolciluxe_java.product.Product;
import com.example.backend_dolciluxe_java.product.ProductRepository;
import com.example.backend_dolciluxe_java.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }

    // kiểm tra có phải là admin hay không (isAdmin = true)
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = userRepository.findById(getCurrentUserId()).orElse(null);
            return user != null && user.isAdmin();
        }
        return false;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> createOrder(CreateOrderRequest request) {
        try {
            String userId = getCurrentUserId();

            List<String> cartItemIds = request.getCartItemIds();
            if (cartItemIds == null || cartItemIds.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(400, "No item selected", null));
            }

            Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
            if (cartOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(404, "Cart not found", null));
            }

            Cart cart = cartOptional.get();

            List<CartItem> selectedItems = cart.getItems().stream()
                    .filter(item -> cartItemIds.contains(item.getId()))
                    .collect(Collectors.toList());

            if (selectedItems.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(404, "Selected items not found in cart", null));
            }

            Address addressOptional = addressRepository.findById(request.getAddressId());
            if (addressOptional == null) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(404, "Address not found", null));
            }
            Address address = addressOptional;
            if (address.getUser() == null || !address.getUser().equals(userId)) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(403, "Address does not belong to the user", null));
            }

            List<OrderItem> orderItems = new ArrayList<>();
            int totalPrice = 0;

            for (CartItem cartItem : selectedItems) {
                Optional<Product> productOptional = productRepository.findById(new ObjectId(cartItem.getProductId()));

                if (productOptional.isEmpty()) {
                    continue;
                }

                Product product = productOptional.get();

                OrderItem orderItem = OrderItem.builder()
                        .id(new ObjectId().toString())
                        .product(product)
                        .productId(cartItem.getProductId())
                        .quantity(cartItem.getQuantity())
                        .price(product.getPrice())
                        .build();

                orderItems.add(orderItem);
                totalPrice += product.getPrice() * cartItem.getQuantity();
            }

            Order newOrder = Order.builder()
                    .user(userId)
                    .address(address)
                    .items(orderItems)
                    .totalPrice(totalPrice)
                    .paymentMethod(Order.PaymentMethod.COD)
                    .paymentStatus(Order.PaymentStatus.pending)
                    .orderStatus(Order.OrderStatus.pending)
                    .build();

            Order savedOrder = orderRepository.save(newOrder);

            cart.setItems(cart.getItems().stream()
                    .filter(item -> !cartItemIds.contains(item.getId()))
                    .collect(Collectors.toList()));

            cartRepository.save(cart);

            return ResponseEntity.ok(
                    new ApiResponse<>(201, "Order created successfully", savedOrder));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, "Error creating order: " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getAllOrders() {
        try {
            String userId = getCurrentUserId();
            Optional<List<Order>> orders = orderRepository.findByUser(userId);

            if (orders.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(404, "No orders found for this user", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, "Error retrieving orders: " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getOrderDetail(String orderId) {
        try {
            String userId = getCurrentUserId();
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(404, "Order not found", null));
            }

            Order order = orderOptional.get();

            if (!order.getUser().equals(userId)) {
                return ResponseEntity.ok(
                        new ApiResponse<>(403, "You do not have permission to view this order", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Order retrieved successfully", order));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, "Error retrieving order: " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        try {
            String userId = getCurrentUserId();
            if (!isAdmin()) {
                return ResponseEntity.status(403).body(
                        new ApiResponse<>(403, "You do not have permission to update order status", null));
            }
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(404, "Order not found", null));
            }

            Order order = orderOptional.get();

            if (!order.getUser().equals(userId)) {
                return ResponseEntity.ok(
                        new ApiResponse<>(403, "You do not have permission to update this order", null));
            }

            Order.OrderStatus statusEnum;
            try {
                statusEnum = Order.OrderStatus.valueOf(request.getStatus());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(400, "Invalid order status: " + request.getStatus(), null));
            }
            order.setOrderStatus(statusEnum);
            Order updatedOrder = orderRepository.save(order);

            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Order status updated successfully", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, "Error updating order status: " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteOrder(String orderId) {
        try {
            String userId = getCurrentUserId();
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(404, "Order not found", null));
            }

            Order order = orderOptional.get();

            if (!order.getUser().equals(userId)) {
                return ResponseEntity.ok(
                        new ApiResponse<>(403, "You do not have permission to delete this order", null));
            }

            orderRepository.delete(order);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Order deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(500, "Error deleting order: " + e.getMessage(), null));
        }
    }
}
