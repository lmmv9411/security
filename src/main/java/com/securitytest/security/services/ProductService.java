package com.securitytest.security.services;

import com.securitytest.security.dto.Product.ProductPatchDTO;
import com.securitytest.security.exceptions.customs.BadRequestException;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    public Product findById(Long id) {
        var optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            throw new NotFoundException("Product not found: " + id);
        }
        return optionalProduct.get();
    }

    @Transactional
    public Product update(Long id, ProductPatchDTO productPatch) {

        var product = findById(id);

        var count = new AtomicInteger();

        Optional.ofNullable(productPatch.getDescription())
                .filter(description -> !description.isBlank())
                .ifPresentOrElse(product::setDescription, count::incrementAndGet);

        Optional.ofNullable(productPatch.getPrice())
                .filter(price -> price > 0)
                .ifPresentOrElse(product::setPrice, count::incrementAndGet);

        if (count.get() == productPatch.getClass().getDeclaredFields().length) {
            throw new BadRequestException("No hay campos validos a actualizar!.");
        }

        return productRepository.save(product);

    }
}
