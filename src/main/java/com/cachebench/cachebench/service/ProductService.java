package com.cachebench.cachebench.service;

import com.cachebench.cachebench.entity.Product;
import com.cachebench.cachebench.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(value = "products", key = "'all-' + #page + '-' + #size")
    public Page<Product> findAll(int page, int size) {
        log.info(" DB hit - findAll(page={}, size={})", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        log.info(" DB hit - findById({})", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable : " + id));
    }

    @Cacheable(value = "searches", key = "#query + '-' + #page + '-' + #size")
    public Page<Product> search(String query, int page, int size) {
        log.info(" DB hit - search(query={}, page={}, size={})", query, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    @Cacheable(value = "products", key = "'cat-' + #categoryId + '-' + #page + '-' + #size")
    public Page<Product> findByCategory(Long categoryId, int page, int size) {
        log.info(" DB hit - findByCategory(categoryId={}, page={}, size={})", categoryId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryId(categoryId, pageable);
    }
}