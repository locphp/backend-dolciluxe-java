package com.example.backend_dolciluxe_java.address;

import com.example.backend_dolciluxe_java.address.service.AddressService;

import lombok.RequiredArgsConstructor;

import com.example.backend_dolciluxe_java.address.dto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor

public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody CreateAddressRequest request) {
        return addressService.createAddress(request);

    }

    @GetMapping
    public ResponseEntity<?> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable String addressId, @RequestBody UpdateAddressRequest request) {
        return addressService.updateAddress(addressId, request);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable String addressId) {
        return addressService.deleteAddress(addressId);
    }
}
