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

    public List<Product> getAllProducts() {
        try {
            return repo.getAllProducts().get(); // block until Firebase returns
        } catch (Exception e) {
            log.error("Error fetching products", e);
            return List.of();
        }
    }


    public Product getProductById(int id) {
        repo.getProductById(id);
        return null;
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
