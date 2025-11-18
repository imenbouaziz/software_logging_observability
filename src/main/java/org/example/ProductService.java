package org.example;

import java.time.LocalDate;

public class ProductService {
    private ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public void getAllProducts() {
        repo.getAllProducts();
    }

    public void getProductById(int id) {
        repo.getProductById(id);
    }

    public void addProduct(Product product) {
        repo.addProduct(product);
    }

    public void deleteProduct(int id) {
        repo.deleteProduct(id);
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) {
        repo.updateProduct(id, name, price, expiration_date);
    }
}
