package com.securitytest.security.controllers;

import com.securitytest.security.models.Product;
import com.securitytest.security.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
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

}
