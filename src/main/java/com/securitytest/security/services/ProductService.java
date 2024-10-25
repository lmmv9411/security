package com.securitytest.security.services;

import com.securitytest.security.exceptions.customs.NotFoundException;
import com.securitytest.security.models.Product;
import com.securitytest.security.models.TypeMove;
import com.securitytest.security.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Transactional
    public void increaseStock(Long id, int q) {

        var optionalProduct = productRepository.findById(id);

        optionalProduct.ifPresentOrElse((p) -> {
            productRepository.increaseStock(p.getId(), q, TypeMove.ENTRY);
        }, () -> {
            throw new NotFoundException("Product not found.");
        });

    }

    @Transactional
    public void decreaseStock(Long id, int q) {
        var optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresentOrElse((p) -> {
            productRepository.decreaseStock(p.getId(), q, TypeMove.EXIT);
        }, () -> {
            throw new NotFoundException("Product not found.");
        });
    }
}
