package org.example;
import java.util.List;
import java.util.Set;
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
        Object sessionUserId = session.getAttribute("userId");
        System.out.println((("Session userId = " + sessionUserId) + " | Path userId = ") + userId);
        if (((sessionUserId == null) || (!(sessionUserId instanceof Integer))) || (!sessionUserId.equals(userId))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
        logger.info("ACTION UNKNOWN");
    }

    @GetMapping
    public List<Product> fetchAllProducts(@PathVariable
    int userId, HttpSession session) {
        ensureLoggedIn(session, userId);
        List<Product> products = service.fetchAllProducts(userId);
        Set<Integer> top = service.topExpensiveProductIds();
        int expensiveCount = ((int) (products.stream().filter(p -> top.contains(p.getId())).count()));
        logger.info("ACTION | userId={} | action={} | method={} | expensiveCount={} | totalExpensiveProducts={}", userId, "READ", "fetchAllProducts", expensiveCount, 10);
        // System.out.println("User " + userId + " searched " + expensiveCount + " expensive products");
        return products;
    }

    @GetMapping("/{id}")
    public Product fetchProductById(@PathVariable
    int userId, @PathVariable
    int id, HttpSession session) {
        ensureLoggedIn(session, userId);
        Product product = service.fetchProductById(userId, id);
        Set<Integer> top = service.topExpensiveProductIds();
        boolean isExpensive = (product != null) && top.contains(product.getId());
        int expensiveCount = 0;
        if (isExpensive) {
            expensiveCount += 1;
            // System.out.println("User " + userId + " searched for an expensive product with id " + product.getId());
        }
        logger.info("ACTION | userId={} | action={} | method={} | expensiveCount={} | totalExpensiveProducts={}", userId, "READ", "fetchProductById", expensiveCount, 10);
        return product;
    }

    @PostMapping
    public void addProduct(@PathVariable
    int userId, @RequestBody
    Product product, HttpSession session) {
        ensureLoggedIn(session, userId);
        service.addProduct(userId, product);
        logger.info("ACTION | userId={} | action={} | method={} ", userId, "WRITE", "addProduct");
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable
    int userId, @PathVariable
    int id, HttpSession session) {
        ensureLoggedIn(session, userId);
        service.deleteProduct(userId, id);
        logger.info("ACTION | userId={} | action={} | method={} ", userId, "WRITE", "deleteProduct");
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable
    int userId, @PathVariable
    int id, @RequestParam
    String name, @RequestParam
    double price, @RequestParam
    String expiration_date, HttpSession session) {
        ensureLoggedIn(session, userId);
        service.updateProduct(userId, id, name, price, expiration_date);
        logger.info("ACTION | userId={} | action={} | method={} ", userId, "WRITE", "updateProduct");
    }

    @GetMapping("/global")
    public List<Product> fetchAllProductsGlobal(@PathVariable
    int userId, HttpSession session) {
        ensureLoggedIn(session, userId);
        List<Product> products = service.fetchAllProductsGlobal(userId);
        Set<Integer> top = service.topExpensiveProductIds();
        int expensiveCount = ((int) (products.stream().filter(p -> top.contains(p.getId())).count()));
        logger.info("ACTION | userId={} | action={} | method={} | expensiveCount={} | totalExpensiveProducts={}", userId, "READ", "fetchAllProductsGlobal", expensiveCount, 10);
        // System.out.println("User " + userId + " searched " + expensiveCount + " expensive products globally");
        return products;
    }

    @GetMapping("/global/name/{name}")
    public Product fetchProductByNameGlobal(@PathVariable
    int userId, @PathVariable
    String name, HttpSession session) {
        ensureLoggedIn(session, userId);
        Product product = service.fetchProductByNameGlobal(userId, name);
        Set<Integer> top = service.topExpensiveProductIds();
        boolean isExpensive = (product != null) && top.contains(product.getId());
        int expensiveCount = 0;
        if (isExpensive) {
            expensiveCount += 1;
        }
        logger.info("ACTION | userId={} | action={} | method={} | expensiveCount={} | totalExpensiveProducts={}", userId, "READ", "fetchProductByNameGlobal", expensiveCount, 10);
        return product;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.ProductController.class);
}