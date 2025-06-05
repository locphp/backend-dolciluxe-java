package com.example.backend_dolciluxe_java.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Dùng userId tạm thời truyền trong query/body (vì chưa có auth)
    @PostMapping
    public Address create(@RequestParam String userId, @RequestBody Address address) {
        return addressService.createAddress(userId, address);
    }

    @GetMapping
    public List<Address> getAll(@RequestParam String userId) {
        return addressService.getAllAddress(userId);
    }

    @PutMapping("/{id}")
    public Address update(@PathVariable String id,
                          @RequestParam String userId,
                          @RequestBody Address address) {
        return addressService.updateAddress(id, userId, address);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id,
                         @RequestParam String userId) {
        addressService.deleteAddress(id, userId);
        return "Delete success";
    }
}
