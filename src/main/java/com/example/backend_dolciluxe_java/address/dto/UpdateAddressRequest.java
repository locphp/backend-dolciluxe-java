package com.example.backend_dolciluxe_java.address.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UpdateAddressRequest {
    private String fullName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String detail;

    @Builder.Default
    private Boolean isDefault = false;
}
