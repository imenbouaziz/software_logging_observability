package org.example.logs_tp_backend_spooned;
import DataSnapshot;
import DatabaseError;
import FirebaseDatabase;
import ValueEventListener;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.example.DatabaseReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
@Repository
public class ProductRepository {
    private DatabaseReference productRef;

    public ProductRepository() {
    }

    // init after spring got ready not directly
    @PostConstruct
    public void init() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "init");
        this.productRef = FirebaseDatabase.getInstance().getReference("products");
    }

    public void getProductById(int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getProductById");
        this.productRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    org.example.Product product = snapshot.getValue(org.example.Product.class);
                    System.out.println("Found product: " + product.getName());
                } else {
                    System.out.println("Product not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void addProduct(org.example.Product product) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "addProduct");
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("counters/products");
        counterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentId = (snapshot.exists()) ? snapshot.getValue(Long.class) : 0;
                long newId = currentId + 1;
                counterRef.setValueAsync(newId);
                product.setId(((int) (newId)));
                org.example.ProductRepository.this.productRef.child(String.valueOf(newId)).setValueAsync(product);
                System.out.println("Product added with id: " + newId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void deleteProduct(int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "deleteProduct");
        this.productRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    System.out.println("Product not found");
                } else {
                    org.example.ProductRepository.this.productRef.child(String.valueOf(id)).removeValueAsync();
                    System.out.println("Product deleted");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void updateProduct(int id, String name, double price, LocalDate expiration_date) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "updateProduct");
        this.productRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    System.out.println("Product not found");
                } else {
                    org.example.Product existing = snapshot.getValue(org.example.Product.class);
                    if (existing != null) {
                        existing.setName(name);
                        existing.setPrice(price);
                        existing.setExpiration_date(expiration_date);
                        org.example.ProductRepository.this.productRef.child(String.valueOf(id)).setValueAsync(existing);
                        System.out.println("Product updated");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public CompletableFuture<List<org.example.Product>> getAllProducts() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getAllProducts");
        CompletableFuture<List<org.example.Product>> future = new CompletableFuture<>();
        this.productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<org.example.Product> products = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    products.add(child.getValue(org.example.Product.class));
                }
                future.complete(products);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.ProductRepository.class);
}