package org.example.logs_tp_backend_spooned;
import java.time.LocalDate;
import java.util.List;
import org.example.DeleteMapping;
import org.example.GetMapping;
import org.example.PathVariable;
import org.example.PostMapping;
import org.example.PutMapping;
import org.example.RequestBody;
import org.example.RequestMapping;
import org.example.RequestParam;
import org.example.RestController;
import org.slf4j.Logger;
@RestController
@RequestMapping("/products")
public class ProductController {
    private final org.example.ProductService service;

    public ProductController(org.example.ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<org.example.Product> getAllProducts() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getAllProducts");
        return this.service.getAllProducts();
    }

    @GetMapping("/{id}")
    public org.example.Product getProductById(@PathVariable
    int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getProductById");
        return this.service.getProductById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody
    org.example.Product product) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "addProduct");
        this.service.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable
    int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "deleteProduct");
        this.service.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable
    int id, @RequestParam
    String name, @RequestParam
    double price, @RequestParam
    LocalDate expiration_date) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "updateProduct");
        this.service.updateProduct(id, name, price, expiration_date);
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.ProductController.class);
}