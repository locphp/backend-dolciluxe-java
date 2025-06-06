package com.example.backend_dolciluxe_java.order.dto;

import com.example.backend_dolciluxe_java.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private List<String> cartItemIds;
    private String addressId;
    private String paymentMethod;

    public Order.PaymentMethod getPaymentMethodEnum() {
        if (paymentMethod != null && paymentMethod.equalsIgnoreCase("VNPAY")) {
            return Order.PaymentMethod.VNPAY;
        }
        return Order.PaymentMethod.COD;
    }

}
