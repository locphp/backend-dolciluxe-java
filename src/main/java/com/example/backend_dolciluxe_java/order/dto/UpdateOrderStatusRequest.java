package com.example.backend_dolciluxe_java.order.dto;

import com.example.backend_dolciluxe_java.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    private String status;

    public Order.OrderStatus getOrderStatusEnum() {
        if (status == null) {
            return Order.OrderStatus.pending; // Default value
        }

        switch (status.toLowerCase()) {
            case "processing":
                return Order.OrderStatus.processing;
            case "shipping":
                return Order.OrderStatus.shipping;
            case "completed":
                return Order.OrderStatus.completed;
            case "cancelled":
                return Order.OrderStatus.cancelled;
            case "pending":
            default:
                return Order.OrderStatus.pending;
        }
    }

}
