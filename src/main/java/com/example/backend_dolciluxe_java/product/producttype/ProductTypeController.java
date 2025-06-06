package com.example.backend_dolciluxe_java.product.producttype;

import com.example.backend_dolciluxe_java.product.producttype.dto.ProductTypeRequest;
import com.example.backend_dolciluxe_java.product.producttype.dto.ProductTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-type")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @GetMapping
    public List<ProductTypeResponse> getAll() {
        return productTypeService.getAll();
    }

    @GetMapping("/{id}")
    public ProductTypeResponse getById(@PathVariable String id) {
        return productTypeService.getById(id);
    }

    @PostMapping
    public ProductTypeResponse create(@RequestBody ProductTypeRequest request) {
        return productTypeService.create(request);
    }

    @PutMapping("/{id}")
    public ProductTypeResponse update(@PathVariable String id,
                                      @RequestBody ProductTypeRequest request) {
        return productTypeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        productTypeService.delete(id);
        return "Product type deleted successfully";
    }

    @PatchMapping("/restore/{id}")
    public ProductTypeResponse restore(@PathVariable String id) {
        return productTypeService.restore(id);
    }

    @GetMapping("/trash")
    public List<ProductTypeResponse> getDeleted() {
        return productTypeService.getDeleted();
    }
}
