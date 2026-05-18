# Journal de bord — ADS CacheBench

**Étudiant :** Jordan [TON-NOM]  
**Promotion :** A4 FISE INFO  
**Sujet :** Étude comparative des stratégies de mise en cache pour API REST e-commerce  
**Démarrage :** Mai 2026

---

## Session 1 — 18/05/2026 — Mise en place du projet et API REST

**Durée :** environ 4 heures  
**Objectif :** Avoir une API REST fonctionnelle qui servira de banc de test.

###  Ce qui a été fait
- Génération du projet via Spring Initializr (Spring Boot 3.5.14, Java 17, Maven)
- Création de 2 entités JPA : `Product` (avec prix, stock, image, catégorie) et `Category`
- Création de 2 Repositories Spring Data JPA
- Création de 2 Services pour isoler la logique métier
- Création de 2 Controllers REST exposant 6 endpoints (`/api/products`, `/api/categories`, etc.)
- Mise en place d'un `DataLoader` qui injecte 3 catégories et 8 produits au démarrage
- Configuration de H2 en mémoire pour la phase de prototypage
- Initialisation du dépôt Git + GitHub

###  Concepts théoriques abordés
- **Architecture en couches** Spring Boot (Controller → Service → Repository) : séparation des responsabilités, clé pour l'introduction transparente d'un cache.
- **Lazy loading** Hibernate : Hibernate remplace les relations `@ManyToOne(fetch = LAZY)` par des proxies ByteBuddy. Cela impacte directement la sérialisation JSON et donc le choix du cache (un objet à mettre en cache doit pouvoir être sérialisé).
- **Spring Data JPA** : génération automatique des requêtes SQL à partir du nom des méthodes (`findByNameContainingIgnoreCase`).

###  Difficultés rencontrées
- Erreur 500 "ByteBuddyInterceptor" sur `/api/products` à cause du proxy Hibernate sur la catégorie.
- **Solution retenue :** annotation `@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})` sur `Category`.
- **Alternatives envisagées :** DTO dédiés (plus propre mais plus de code, à envisager pour la version finale).

###  Pour le rapport
- **Section 7.1 (concepts théoriques)** : développer le lazy loading et l'impact sur la sérialisation, le pattern proxy en ORM.
- **Section 8.1 (cas d'usage)** : modèle de données simplifié (2 entités, relation N..1), tableau des endpoints REST.
- **Section 8.2 (banc de test)** : justifier le choix Spring Boot + JPA + H2 pour la phase de mise au point.
- **Annexe** : code source des entités, repositories, services, controllers.

###  Données / captures à archiver
- [ ] Capture du JSON `/api/products` (preuve API fonctionnelle)
- [ ] Capture console H2 avec tables remplies
- [ ] Capture logs de démarrage avec "Données initiales chargées"
- [ ] Fichier `application.properties` complet

---

## Session 2 — ...