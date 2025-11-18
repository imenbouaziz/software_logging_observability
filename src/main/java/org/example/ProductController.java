package org.example;
import java.time.LocalDate;
import java.util.List;

public class ProductController {
    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    public Product getProductById(int id) throws Exception {
        return service.getProductById(id);
    }

    public void addProduct(Product product) throws Exception {
        service.addProduct(product);
    }

    public void deleteProduct(int id) throws Exception {
        service.deleteProduct(id);
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) throws Exception {
        service.updateProduct(id, name, price, expiration_date);
    }
}
