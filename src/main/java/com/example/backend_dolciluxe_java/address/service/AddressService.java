package com.example.backend_dolciluxe_java.address.service;

import com.example.backend_dolciluxe_java.address.dto.*;

import org.springframework.http.ResponseEntity;

public interface AddressService {
    ResponseEntity<?> createAddress(CreateAddressRequest request);

    ResponseEntity<?> getAllAddresses();

    ResponseEntity<?> updateAddress(String addressId, UpdateAddressRequest request);

    ResponseEntity<?> deleteAddress(String addressId);
}