package org.example;

import java.time.LocalDate;

public class ProductController {
    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    public void getAllProducts() {
        service.getAllProducts();
    }

    public void getProductById(int id) {
        service.getProductById(id);
    }

    public void addProduct(Product product) {
        service.addProduct(product);
    }

    public void deleteProduct(int id) {
        service.deleteProduct(id);
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) {
        service.updateProduct(id, name, price, expiration_date);
    }
}
