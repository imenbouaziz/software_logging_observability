package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public Product getProductById(int id) throws Exception {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new Exception("Product not found"));
    }

    public void addProduct(Product product) throws Exception {
        if (products.stream().anyMatch(p -> p.getId() == product.getId())) {
            throw new Exception("Product with this ID already exists");
        }
        products.add(product);
    }

    public void deleteProduct(int id) throws Exception {
        Product p = getProductById(id);
        products.remove(p);
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) throws Exception {
        Product p = getProductById(id);
        p.setName(name);
        p.setPrice(price);
        p.setExpiration_date(expiration_date);
    }

    public List<Product> getAllProducts(){
        return products;
    }

}
