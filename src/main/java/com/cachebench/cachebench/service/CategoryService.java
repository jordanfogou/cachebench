package com.cachebench.cachebench.service;

import com.cachebench.cachebench.entity.Category;
import com.cachebench.cachebench.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categories", key = "'all'")
    public List<Category> findAll() {
        log.info(" DB hit - findAll categories");
        return categoryRepository.findAll();
    }

    @Cacheable(value = "categories", key = "#id")
    public Category findById(Long id) {
        log.info(" DB hit - findById category {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable : " + id));
    }
}