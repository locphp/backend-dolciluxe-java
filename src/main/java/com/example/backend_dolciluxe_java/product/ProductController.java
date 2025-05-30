package com.example.backend_dolciluxe_java.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable String id) {
        return productService.getById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        productService.delete(id);
        return "Product deleted successfully";
    }

    @PatchMapping("/restore/{id}")
    public Product restore(@PathVariable String id) {
        return productService.restore(id);
    }

    @GetMapping("/by-type/{typeId}")
    public List<Product> getByType(@PathVariable String typeId) {
        return productService.getByType(typeId);
    }

    @GetMapping("/trash")
    public List<Product> getDeleted() {
        return productService.getDeleted();
    }
}