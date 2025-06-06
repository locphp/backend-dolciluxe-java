package com.example.backend_dolciluxe_java.product.producttype;

import com.example.backend_dolciluxe_java.product.producttype.dto.ProductTypeRequest;
import com.example.backend_dolciluxe_java.product.producttype.dto.ProductTypeResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    public List<ProductTypeResponse> getAll() {
        return productTypeRepository.findByIsDeletedFalse().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductTypeResponse getById(String id) {
        ProductType productType = productTypeRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product type not found"));
        return toResponse(productType);
    }

    public ProductTypeResponse create(ProductTypeRequest request) {
        if (productTypeRepository.existsByTypeName(request.getTypeName())) {
            throw new RuntimeException("Product type already exists");
        }

        ProductType newType = ProductType.builder()
                .typeName(request.getTypeName())
                .description(request.getDescription())
                .isDeleted(false)
                .deletedAt(null)
                .build();

        return toResponse(productTypeRepository.save(newType));
    }

    public ProductTypeResponse update(String id, ProductTypeRequest request) {
        ObjectId objectId = new ObjectId(id);
        ProductType existing = productTypeRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Product type not found"));

        if (!existing.getTypeName().equals(request.getTypeName()) &&
                productTypeRepository.existsByTypeName(request.getTypeName())) {
            throw new RuntimeException("Product type name already exists");
        }

        existing.setTypeName(request.getTypeName());
        existing.setDescription(request.getDescription());

        return toResponse(productTypeRepository.save(existing));
    }

    public void delete(String id) {
        ObjectId objectId = new ObjectId(id);
        ProductType productType = productTypeRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Product type not found"));

        productType.setIsDeleted(true);
        productType.setDeletedAt(Instant.now());

        productTypeRepository.save(productType);
    }

    public ProductTypeResponse restore(String id) {
        ObjectId objectId = new ObjectId(id);
        ProductType productType = productTypeRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Product type not found"));

        productType.setIsDeleted(false);
        productType.setDeletedAt(null);

        return toResponse(productTypeRepository.save(productType));
    }

    public List<ProductTypeResponse> getDeleted() {
        return productTypeRepository.findByIsDeletedTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProductTypeResponse toResponse(ProductType productType) {
        return ProductTypeResponse.builder()
                .id(productType.get_id().toHexString())
                .typeName(productType.getTypeName())
                .description(productType.getDescription())
                .build();
    }
}
