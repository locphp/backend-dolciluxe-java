package com.example.backend_dolciluxe_java.address.service.impl;

import com.example.backend_dolciluxe_java.address.service.AddressService;
import com.example.backend_dolciluxe_java.address.Address;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.backend_dolciluxe_java.common.ApiResponse;
import com.example.backend_dolciluxe_java.address.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;
import com.example.backend_dolciluxe_java.address.repository.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal(); // userId đã được set ở JwtFilter
        }
        throw new RuntimeException("User not authenticated");
    }

    @Override
    public ResponseEntity<?> createAddress(CreateAddressRequest request) {
        String userId = getCurrentUserId();

        Address address = new Address();
        address.setUser(userId);
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setDistrict(request.getDistrict());
        address.setWard(request.getWard());
        address.setDetail(request.getDetail());
        if (addressRepository.countByUser(userId) == 0) {
            address.setIsDefault(true);
        } else {
            address.setIsDefault(request.getIsDefault());
        }
        addressRepository.save(address);
        return ResponseEntity.ok(new ApiResponse<>(201, "Address created successfully", address));

    }

    @Override
    public ResponseEntity<?> getAllAddresses() {
        String userId = getCurrentUserId();
        List<Address> addresses = addressRepository.findByUser(userId);
        if (addresses.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(204, "No addresses found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Addresses retrieved successfully", addresses));
    }

    @Override
    public ResponseEntity<?> updateAddress(String addressId, UpdateAddressRequest request) {
        Address address = addressRepository.findById(addressId);
        if (address == null) {
            return ResponseEntity.ok(new ApiResponse<>(404, "Address not found", null));
        }
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setDistrict(request.getDistrict());
        address.setWard(request.getWard());
        address.setDetail(request.getDetail());
        address.setIsDefault(request.getIsDefault());
        addressRepository.save(address);
        return ResponseEntity.ok(new ApiResponse<>(200, "Address updated successfully", address));
    }

    @Override
    public ResponseEntity<?> deleteAddress(String addressId) {
        // Implementation for deleting an address
        Address address = addressRepository.findById(addressId);
        if (address == null) {
            return ResponseEntity.ok(new ApiResponse<>(404, "Address not found", null));
        }
        addressRepository.deleteById(addressId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Address deleted successfully", null));
    }
}
