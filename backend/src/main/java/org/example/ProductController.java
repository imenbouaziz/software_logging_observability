package org.example;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> fetchAllProducts(@PathVariable int userId) {
        return service.fetchAllProducts(userId);
    }

    @GetMapping("/{id}")
    public Product fetchProductById(@PathVariable int userId, @PathVariable int id) {
        return service.fetchProductById(userId, id);
    }

    @PostMapping
    public void addProduct(@PathVariable int userId, @RequestBody Product product) {
        service.addProduct(userId, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int userId, @PathVariable int id) {
        service.deleteProduct(userId, id);
    }

    // Update product for a user
    @PutMapping("/{id}")
    public void updateProduct(@PathVariable int userId,
                              @PathVariable int id,
                              @RequestParam String name,
                              @RequestParam double price,
                              @RequestParam LocalDate expiration_date) {
        service.updateProduct(userId, id, name, price, expiration_date);
    }
}
