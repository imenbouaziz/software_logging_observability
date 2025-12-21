package org.example.logs_tp_backend_spooned.service;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.logs_tp_backend_spooned.model.Product;
import org.example.logs_tp_backend_spooned.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class ProductService {
    private ProductRepository repo;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> fetchAllProducts(int userId) {
        try {
            return repo.fetchAllProducts(userId).get();
        } catch (Exception e) {
            log.error("Error fetching products", e);
            return List.of();
        }
    }

    public Product fetchProductById(int userId, int id) {
        repo.fetchProductById(userId, id);
        return null;
    }

    public void addProduct(int userId, Product product) {
        repo.addProduct(userId, product);
    }

    public void deleteProduct(int userId, int id) {
        repo.deleteProduct(userId, id);
    }

    public void updateProduct(int userId, int id, String name, double price, String expiration_date) {
        repo.updateProduct(userId, id, name, price, expiration_date);
    }

    public Product fetchProductByNameGlobal(int userId, String name) {
        try {
            return repo.fetchProductByNameGlobal(name).get();
        } catch (Exception e) {
            log.error("Error fetching product by name globally for user {}", userId, e);
            return null;
        }
    }

    public List<Product> fetchAllProductsGlobal(int userId) {
        try {
            return repo.fetchAllProductsGlobal().get();
        } catch (Exception e) {
            log.error("Error fetching global products for user {}", userId, e);
            return List.of();
        }
    }

    public Set<Integer> topExpensiveProductIds() {
        try {
            return repo.findTopProducts().get().stream().map(Product::getId).collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Error fetching top expensive products", e);
            return Set.of();
        }
    }
}