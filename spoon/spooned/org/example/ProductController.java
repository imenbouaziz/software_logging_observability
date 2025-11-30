package org.example;
import java.util.List;
import org.slf4j.Logger;
import jakarta.servlet.http.HttpSession;
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
        logger.info("ACTION UNKNOWN");
        Object sessionUserId = session.getAttribute("userId");
        // just making sure the user id is fetched properly when logged in a session
        System.out.println((("Session userId = " + sessionUserId) + " | Path userId = ") + userId);
        if (((sessionUserId == null) || (!(sessionUserId instanceof Integer))) || (!sessionUserId.equals(userId))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
    }

    @GetMapping
    public List<Product> fetchAllProducts(@PathVariable
    int userId, HttpSession session) {
        logger.info("ACTION | userId={} | action={} | method={}", userId, "READ", "fetchAllProducts");
        ensureLoggedIn(session, userId);
        return service.fetchAllProducts(userId);
    }

    @GetMapping("/{id}")
    public Product fetchProductById(@PathVariable
    int userId, @PathVariable
    int id, HttpSession session) {
        logger.info("ACTION | userId={} | action={} | method={}", userId, "READ", "fetchProductById");
        ensureLoggedIn(session, userId);
        return service.fetchProductById(userId, id);
    }

    @PostMapping
    public void addProduct(@PathVariable
    int userId, @RequestBody
    Product product, HttpSession session) {
        logger.info("ACTION | userId={} | action={} | method={}", userId, "WRITE", "addProduct");
        ensureLoggedIn(session, userId);
        service.addProduct(userId, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable
    int userId, @PathVariable
    int id, HttpSession session) {
        logger.info("ACTION | userId={} | action={} | method={}", userId, "WRITE", "deleteProduct");
        ensureLoggedIn(session, userId);
        service.deleteProduct(userId, id);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable
    int userId, @PathVariable
    int id, @RequestParam
    String name, @RequestParam
    double price, @RequestParam
    String expiration_date, HttpSession session) {
        logger.info("ACTION | userId={} | action={} | method={}", userId, "WRITE", "updateProduct");
        ensureLoggedIn(session, userId);
        service.updateProduct(userId, id, name, price, expiration_date);
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.ProductController.class);
}