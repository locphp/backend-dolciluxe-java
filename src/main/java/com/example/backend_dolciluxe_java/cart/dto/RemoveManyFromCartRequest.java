package com.example.backend_dolciluxe_java.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveManyFromCartRequest {
    private List<String> productIds;

}
