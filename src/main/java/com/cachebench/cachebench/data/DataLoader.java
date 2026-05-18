package com.cachebench.cachebench.data;

import com.cachebench.cachebench.entity.Category;
import com.cachebench.cachebench.entity.Product;
import com.cachebench.cachebench.repository.CategoryRepository;
import com.cachebench.cachebench.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            log.info("Données déjà présentes, chargement ignoré.");
            return;
        }

        // === Catégories ===
        Category electronics = categoryRepository.save(
                Category.builder().name("Electronics").description("Appareils électroniques").build()
        );
        Category books = categoryRepository.save(
                Category.builder().name("Books").description("Livres et e-books").build()
        );
        Category clothing = categoryRepository.save(
                Category.builder().name("Clothing").description("Vêtements et accessoires").build()
        );

        // === Produits ===
        productRepository.saveAll(List.of(
                Product.builder().name("MacBook Pro 14\"").description("Ordinateur portable Apple")
                        .price(new BigDecimal("2299.00")).stockQuantity(15)
                        .imageUrl("https://example.com/macbook.jpg").category(electronics).build(),
                Product.builder().name("iPhone 15 Pro").description("Smartphone Apple")
                        .price(new BigDecimal("1229.00")).stockQuantity(50)
                        .imageUrl("https://example.com/iphone.jpg").category(electronics).build(),
                Product.builder().name("Sony WH-1000XM5").description("Casque audio sans fil")
                        .price(new BigDecimal("399.00")).stockQuantity(30)
                        .imageUrl("https://example.com/sony.jpg").category(electronics).build(),
                Product.builder().name("Clean Code").description("Livre de Robert C. Martin")
                        .price(new BigDecimal("35.00")).stockQuantity(100)
                        .imageUrl("https://example.com/cleancode.jpg").category(books).build(),
                Product.builder().name("The Pragmatic Programmer").description("Livre culte du dev")
                        .price(new BigDecimal("42.00")).stockQuantity(80)
                        .imageUrl("https://example.com/pragmatic.jpg").category(books).build(),
                Product.builder().name("Effective Java").description("Livre par Joshua Bloch")
                        .price(new BigDecimal("45.00")).stockQuantity(60)
                        .imageUrl("https://example.com/effective.jpg").category(books).build(),
                Product.builder().name("T-shirt Spring Boot").description("T-shirt 100% coton")
                        .price(new BigDecimal("19.99")).stockQuantity(200)
                        .imageUrl("https://example.com/tshirt.jpg").category(clothing).build(),
                Product.builder().name("Hoodie Java").description("Sweat à capuche pour devs")
                        .price(new BigDecimal("49.99")).stockQuantity(150)
                        .imageUrl("https://example.com/hoodie.jpg").category(clothing).build()
        ));

        log.info("✅ Données initiales chargées : {} catégories, {} produits",
                categoryRepository.count(), productRepository.count());
    }
}