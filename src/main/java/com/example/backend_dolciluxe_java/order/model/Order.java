package com.example.backend_dolciluxe_java.order.model;

import com.example.backend_dolciluxe_java.address.Address;
import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    private String id;
    private String user;
    @DBRef
    private Address address;

    List<OrderItem> items;

    private Number totalPrice;
    @Builder.Default
    private PaymentMethod paymentMethod = PaymentMethod.COD;
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.pending;
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.pending;

    public enum PaymentMethod {
        COD,
        VNPAY
    }

    public enum PaymentStatus {
        pending,
        paid
    }

    public enum OrderStatus {
        pending,
        processing,
        shipping,
        completed,
        cancelled
    }

}
