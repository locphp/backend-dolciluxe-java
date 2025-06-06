package com.example.backend_dolciluxe_java.order.controller;

import com.example.backend_dolciluxe_java.order.service.OrderService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.backend_dolciluxe_java.order.dto.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor

public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable String orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(orderId, request);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderId) {
        return orderService.deleteOrder(orderId);
    }

}
