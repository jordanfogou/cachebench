import http from 'k6/http';
import { check, sleep } from 'k6';

// ============================================================
// Scenario : Charge progressive realiste sur l'API e-commerce
// ============================================================
// Simule des utilisateurs qui :
//  - listent les produits (pagination)
//  - consultent un produit
//  - font une recherche
//  - filtrent par categorie
// ============================================================

export const options = {
    stages: [
        { duration: '30s', target: 50 },  // Montee progressive : 0 -> 50 VUs
        { duration: '60s', target: 50 },  // Charge stable a 50 VUs
        { duration: '15s', target: 0 },   // Descente
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // alerte si p95 > 500ms
        http_req_failed: ['rate<0.01'],   // alerte si > 1% erreurs
    },
};

const BASE_URL = 'http://localhost:8080/api';

export default function () {
    // 1. Liste des produits (page 0)
    const products = http.get(`${BASE_URL}/products?page=0&size=20`);
    check(products, { 'GET /products 200': (r) => r.status === 200 });
    sleep(0.5);

    // 2. Detail d'un produit aleatoire (entre 1 et 8)
    const id = Math.floor(Math.random() * 8) + 1;
    const product = http.get(`${BASE_URL}/products/${id}`);
    check(product, { 'GET /products/{id} 200': (r) => r.status === 200 });
    sleep(0.5);

    // 3. Recherche
    const search = http.get(`${BASE_URL}/products/search?q=java`);
    check(search, { 'GET /search 200': (r) => r.status === 200 });
    sleep(0.5);

    // 4. Produits d'une categorie
    const cat = Math.floor(Math.random() * 3) + 1;
    const byCategory = http.get(`${BASE_URL}/products/by-category/${cat}`);
    check(byCategory, { 'GET /by-category 200': (r) => r.status === 200 });
    sleep(0.5);
}