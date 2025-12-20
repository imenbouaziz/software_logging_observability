package org.example.controller;

import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpSession;
import org.example.service.ProductService;
import org.example.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/users/{userId}/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    private void ensureLoggedIn(HttpSession session, int userId) {
        Object sessionUserId = session.getAttribute("userId");
        System.out.println("Session userId = " + sessionUserId + " | Path userId = " + userId);

        if (sessionUserId == null || !(sessionUserId instanceof Integer) || !sessionUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
    }

    @GetMapping
    public List<Product> fetchAllProducts(@PathVariable int userId, HttpSession session) {
        ensureLoggedIn(session, userId);

        List<Product> products = service.fetchAllProducts(userId);

        Set<Integer> top = service.topExpensiveProductIds();
        int expensiveCount = (int) products.stream()
                .filter(p -> top.contains(p.getId()))
                .count();

       // System.out.println("User " + userId + " searched " + expensiveCount + " expensive products");

        return products;
    }

    @GetMapping("/{id}")
    public Product fetchProductById(@PathVariable int userId,
                                    @PathVariable int id,
                                    HttpSession session) {
        ensureLoggedIn(session, userId);

        Product product = service.fetchProductById(userId, id);
        Set<Integer> top = service.topExpensiveProductIds();
        boolean isExpensive = (product != null && top.contains(product.getId()));
        int expensiveCount = 0;
        if (isExpensive) {
            expensiveCount += 1;
          //  System.out.println("User " + userId + " searched for an expensive product with id " + product.getId());
        }

        return product;
    }

    @PostMapping
    public void addProduct(@PathVariable int userId,
                           @RequestBody Product product,
                           HttpSession session) {
        ensureLoggedIn(session, userId);
        service.addProduct(userId, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int userId,
                              @PathVariable int id,
                              HttpSession session) {
        ensureLoggedIn(session, userId);
        service.deleteProduct(userId, id);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable int userId,
                              @PathVariable int id,
                              @RequestParam String name,
                              @RequestParam double price,
                              @RequestParam String expiration_date,
                              HttpSession session) {
        ensureLoggedIn(session, userId);
        service.updateProduct(userId, id, name, price, expiration_date);
    }

    @GetMapping("/global")
    public List<Product> fetchAllProductsGlobal(@PathVariable int userId, HttpSession session) {
        ensureLoggedIn(session, userId);

        List<Product> products = service.fetchAllProductsGlobal(userId);
        Set<Integer> top = service.topExpensiveProductIds();
        int expensiveCount = (int) products.stream()
                .filter(p -> top.contains(p.getId()))
                .count();

        //System.out.println("User " + userId + " searched " + expensiveCount + " expensive products globally");

        return products;
    }

    @GetMapping("/global/name/{name}")
    public Product fetchProductByNameGlobal(@PathVariable int userId,
                                            @PathVariable String name,
                                            HttpSession session) {
        ensureLoggedIn(session, userId);

        Product product = service.fetchProductByNameGlobal(userId, name);
        Set<Integer> top = service.topExpensiveProductIds();
        boolean isExpensive = (product != null && top.contains(product.getId()));
        int expensiveCount = 0;
        if (isExpensive) {
            expensiveCount += 1;
        }
        return product;
    }
}

