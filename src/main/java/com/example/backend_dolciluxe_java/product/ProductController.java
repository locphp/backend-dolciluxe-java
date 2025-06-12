package com.example.backend_dolciluxe_java.product;

import com.example.backend_dolciluxe_java.product.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return productService.getById(id);
    }

    @PostMapping
    public ProductResponse create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        productService.delete(id);
        return "Product deleted successfully";
    }

    @PatchMapping("/restore/{id}")
    public ProductResponse restore(@PathVariable String id) {
        return productService.restore(id);
    }

    @GetMapping("/by-type/{typeId}")
    public List<ProductResponse> getByType(@PathVariable String typeId) {
        return productService.getByType(typeId);
    }

    @GetMapping("/trash")
    public List<ProductResponse> getDeleted() {
        return productService.getDeleted();
    }
}