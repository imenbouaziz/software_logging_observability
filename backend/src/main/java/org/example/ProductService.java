package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository repo;
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAllProducts(int userId) {
        try {
            return repo.getAllProducts(userId).get();
        } catch (Exception e) {
            log.error("Error fetching products", e);
            return List.of();
        }
    }


    public Product getProductById(int userId, int id) {
        repo.getProductById(userId, id);
        return null;
    }

    public void addProduct(int userId, Product product) {
        repo.addProduct( userId,product);
    }

    public void deleteProduct(int userId, int id) {
        repo.deleteProduct(userId, id);
    }

    public void updateProduct(int userId, int id, String name, double price, LocalDate expiration_date) {
        repo.updateProduct(userId, id, name, price, expiration_date);
    }
}
