package com.cachebench.cachebench.repository;

import com.cachebench.cachebench.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}