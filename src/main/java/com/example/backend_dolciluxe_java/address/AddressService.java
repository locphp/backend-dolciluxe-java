package com.example.backend_dolciluxe_java.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(String userId, Address address) {
        ObjectId userObjectId = new ObjectId(userId);
        long count = addressRepository.countByUser(userObjectId);
        if (count == 0) {
            address.setIsDefault(true);
        }
        address.setUser(userObjectId);
        address.set_id(null); // để MongoDB tự sinh _id
        return addressRepository.save(address);
    }

    public List<Address> getAllAddress(String userId) {
        return addressRepository.findByUser(new ObjectId(userId));
    }

    public Address updateAddress(String id, String userId, Address newData) {
        ObjectId objectId = new ObjectId(id);
        ObjectId userObjectId = new ObjectId(userId);
        Optional<Address> addressOptional = addressRepository.findById(objectId);
        if (addressOptional.isEmpty() || !addressOptional.get().getUser().equals(userObjectId)) {
            throw new RuntimeException("Address not found");
        }

        if (Boolean.TRUE.equals(newData.getIsDefault())) {
            List<Address> all = addressRepository.findByUser(userObjectId);
            all.forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        newData.set_id(objectId);
        newData.setUser(userObjectId);
        return addressRepository.save(newData);
    }

    public void deleteAddress(String id, String userId) {
        ObjectId objectId = new ObjectId(id);
        ObjectId userObjectId = new ObjectId(userId);
        Address address = addressRepository.findById(objectId)
                .filter(a -> a.getUser().equals(userObjectId))
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new RuntimeException("Cannot delete default address");
        }

        addressRepository.delete(address);
    }
}