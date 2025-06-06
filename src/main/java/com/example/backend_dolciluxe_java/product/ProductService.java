package com.example.backend_dolciluxe_java.product;

import com.example.backend_dolciluxe_java.product.dto.ProductResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> getAll() {
        return productRepository.findByIsDeletedFalse()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(String id) {
        Product product = productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse create(Product product) {
        product.setIsDeleted(false);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(String id, Product productData) {
        Product product = productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(productData.getProductName());
        product.setDescription(productData.getDescription());
        product.setImageLink(productData.getImageLink());
        product.setProductType(productData.getProductType());
        product.setQuantity(productData.getQuantity());
        product.setPrice(productData.getPrice());
        product.setUpdatedAt(Instant.now());

        return toResponse(productRepository.save(product));
    }

    public void delete(String id) {
        Product product = productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsDeleted(true);
        product.setDeletedAt(Instant.now());
        productRepository.save(product);
    }

    public ProductResponse restore(String id) {
        Product product = productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsDeleted(false);
        product.setDeletedAt(null);
        return toResponse(productRepository.save(product));
    }

    public List<ProductResponse> getByType(String typeId) {
        return productRepository.findByProductTypeAndIsDeletedFalse(new ObjectId(typeId))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getDeleted() {
        return productRepository.findByIsDeletedTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.get_id().toHexString())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageLink(product.getImageLink())
                .productType(product.getProductType() != null ? product.getProductType().toHexString() : null)
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .isDeleted(product.getIsDeleted())
                .deletedAt(product.getDeletedAt())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}