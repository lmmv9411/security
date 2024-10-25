package com.securitytest.security.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.securitytest.security.models.Product;
import com.securitytest.security.services.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<Product> findProductsPage(Pageable pageable) {
        return productService.findProductsPage(pageable);
    }

    @PostMapping
    public Product save(@Valid @RequestBody Product product) {
        return productService.save(product);
    }

    @PatchMapping("/{id}/increase-stock")
    public ResponseEntity<?> increaseStock(@PathVariable Long id, @RequestParam @Min(1) int q) {
        productService.increaseStock(id, q);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/decrease-stock")
    public ResponseEntity<?> decreaseStock(@PathVariable Long id, @RequestParam @Min(1) int q) {
        productService.decreaseStock(id, q);
        return ResponseEntity.ok().build();
    }

}
