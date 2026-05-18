package com.cachebench.cachebench.repository;

import com.cachebench.cachebench.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Recherche par nom (insensible à la casse)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Lister les produits d'une catégorie
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}