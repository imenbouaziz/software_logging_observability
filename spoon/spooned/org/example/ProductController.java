package org.example;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("ACTION | userId={} | action=READ | method=getAllProducts");
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable
    int id) {
        logger.info("ACTION | userId={} | action=READ | method=getProductById");
        return service.getProductById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody
    Product product) {
        logger.info("ACTION | userId={} | action=WRITE | method=addProduct");
        service.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable
    int id) {
        logger.info("ACTION | userId={} | action=WRITE | method=deleteProduct");
        service.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable
    int id, @RequestParam
    String name, @RequestParam
    double price, @RequestParam
    LocalDate expiration_date) {
        logger.info("ACTION | userId={} | action=WRITE | method=updateProduct");
        service.updateProduct(id, name, price, expiration_date);
    }
}