package org.example.logs_tp_backend_spooned;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class ProductService {
    private org.example.ProductRepository repo;

    private static final Logger log = LoggerFactory.getLogger(org.example.ProductService.class);

    public ProductService(org.example.ProductRepository repo) {
        this.repo = repo;
    }

    public List<org.example.Product> getAllProducts() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getAllProducts");
        try {
            return this.repo.getAllProducts().get();// block until Firebase returns

        } catch (Exception e) {
            org.example.ProductService.log.error("Error fetching products", e);
            return List.of();
        }
    }

    public org.example.Product getProductById(int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getProductById");
        this.repo.getProductById(id);
        return null;
    }

    public void addProduct(org.example.Product product) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "addProduct");
        this.repo.addProduct(product);
    }

    public void deleteProduct(int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "deleteProduct");
        this.repo.deleteProduct(id);
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "updateProduct");
        this.repo.updateProduct(id, name, price, expiration_date);
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.ProductService.class);
}