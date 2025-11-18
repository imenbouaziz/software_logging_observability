package org.example;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ProductService {
    private ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAllProducts() {
        return repo.getAllProducts();
    }

    public Product getProductById(int id) throws Exception {
        return repo.getProductById(id);
    }

    public void addProduct(Product product) throws Exception {
        repo.addProduct(product);
    }

    public void deleteProduct(int id) throws Exception {
        repo.deleteProduct(id);
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) throws Exception {
        repo.updateProduct(id, name, price, expiration_date);
    }
}
