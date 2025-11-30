package org.example;

import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.example.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users/{userId}/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    private void ensureLoggedIn(HttpSession session, int userId) {
        Object sessionUserId = session.getAttribute("userId");
        //just making sure the user id is fetched properly when logged in a session
        System.out.println("Session userId = " + sessionUserId + " | Path userId = " + userId);

        if (sessionUserId == null || !(sessionUserId instanceof Integer) || !sessionUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
    }


    @GetMapping
    public List<Product> fetchAllProducts(@PathVariable int userId, HttpSession session) {
        ensureLoggedIn(session, userId);
        return service.fetchAllProducts(userId);
    }

    @GetMapping("/{id}")
    public Product fetchProductById(@PathVariable int userId,
                                    @PathVariable int id,
                                    HttpSession session) {
        ensureLoggedIn(session, userId);
        return service.fetchProductById(userId, id);
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
}
