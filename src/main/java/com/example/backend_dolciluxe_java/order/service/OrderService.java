package com.example.backend_dolciluxe_java.order.service;

import com.example.backend_dolciluxe_java.order.dto.*;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<?> createOrder(CreateOrderRequest request);

    ResponseEntity<?> getAllOrders();

    ResponseEntity<?> getOrderDetail(String orderId);

    ResponseEntity<?> updateOrderStatus(String orderId, UpdateOrderStatusRequest request);

    ResponseEntity<?> deleteOrder(String orderId);
}
