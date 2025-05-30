package com.example.backend_dolciluxe_java.product;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findByIsDeletedFalse();
    }

    public Product getById(String id) {
        return productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product create(Product product) {
        product.setIsDeleted(false);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        return productRepository.save(product);
    }

    public Product update(String id, Product productData) {
        Product product = productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(productData.getProductName());
        product.setDescription(productData.getDescription());
        product.setImageLink(productData.getImageLink());
        product.setProductType(productData.getProductType());
        product.setQuantity(productData.getQuantity());
        product.setPrice(productData.getPrice());
        product.setUpdatedAt(Instant.now());

        return productRepository.save(product);
    }

    public void delete(String id) {
        Product product = productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsDeleted(true);
        product.setDeletedAt(Instant.now());
        productRepository.save(product);
    }

    public Product restore(String id) {
        Product product = productRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsDeleted(false);
        product.setDeletedAt(null);
        return productRepository.save(product);
    }

    public List<Product> getByType(String typeId) {
        return productRepository.findByProductTypeAndIsDeletedFalse(new ObjectId(typeId));
    }

    public List<Product> getDeleted() {
        return productRepository.findByIsDeletedTrue();
    }
}