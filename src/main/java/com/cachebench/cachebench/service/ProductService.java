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

    // Latence simulee d'un acces BDD distante typique (ms)
    private static final long SIMULATED_DB_LATENCY_MS = 20;

    private final ProductRepository productRepository;

    @Cacheable(value = "products", key = "'all-' + #page + '-' + #size")
    public Page<Product> findAll(int page, int size) {
        log.info("🐢 DB hit - findAll(page={}, size={})", page, size);
        simulateDatabaseLatency();
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        log.info("🐢 DB hit - findById({})", id);
        simulateDatabaseLatency();
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable : " + id));
    }

    @Cacheable(value = "searches", key = "#query + '-' + #page + '-' + #size")
    public Page<Product> search(String query, int page, int size) {
        log.info("🐢 DB hit - search(query={}, page={}, size={})", query, page, size);
        simulateDatabaseLatency();
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    @Cacheable(value = "products", key = "'cat-' + #categoryId + '-' + #page + '-' + #size")
    public Page<Product> findByCategory(Long categoryId, int page, int size) {
        log.info("🐢 DB hit - findByCategory(categoryId={}, page={}, size={})", categoryId, page, size);
        simulateDatabaseLatency();
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    // Simule la latence d'un acces BDD distante (typique 10-50ms en production)
    private void simulateDatabaseLatency() {
        try {
            Thread.sleep(SIMULATED_DB_LATENCY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}