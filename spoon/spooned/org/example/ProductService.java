package org.example;
import java.time.LocalDate;
import java.util.List;
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

    public List<Product> getAllProducts() {
        logger.info("ACTION | userId={} | action=READ | method=getAllProducts");
        try {
            return repo.getAllProducts().get();// block until Firebase returns

        } catch (Exception e) {
            log.error("Error fetching products", e);
            return List.of();
        }
    }

    public Product getProductById(int id) {
        logger.info("ACTION | userId={} | action=READ | method=getProductById");
        repo.getProductById(id);
        return null;
    }

    public void addProduct(Product product) {
        logger.info("ACTION | userId={} | action=WRITE | method=addProduct");
        repo.addProduct(product);
    }

    public void deleteProduct(int id) {
        logger.info("ACTION | userId={} | action=WRITE | method=deleteProduct");
        repo.deleteProduct(id);
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) {
        logger.info("ACTION | userId={} | action=WRITE | method=updateProduct");
        repo.updateProduct(id, name, price, expiration_date);
    }
}