package org.example;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return service.getProductById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody Product product) {
        service.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        service.deleteProduct(id);
    }
    @PutMapping("/{id}")
    public void updateProduct(@PathVariable int id,
                              @RequestParam String name,
                              @RequestParam double price,
                              @RequestParam LocalDate expiration_date) {
        service.updateProduct(id, name, price, expiration_date);
    }
}
