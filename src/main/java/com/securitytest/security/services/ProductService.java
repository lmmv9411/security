package com.securitytest.security.services;

import com.securitytest.security.models.Product;
import com.securitytest.security.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product save(@Valid Product product) {
        var now = LocalDateTime.now();
        product.setIncome(now);
        product.setLastMove(now);
        return productRepository.save(product);
    }

    public Page<Product> findProductsPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
