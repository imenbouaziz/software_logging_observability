package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import com.google.firebase.database.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
@Repository
public class ProductRepository {
    private DatabaseReference productRef;

    public ProductRepository() {
    }

    // init after spring got ready not directly
    @PostConstruct
    public void init() {
        this.productRef = FirebaseDatabase.getInstance().getReference("products");
    }

    public void fetchProductById(int id, int userId) {
        CompletableFuture<Product> future = new CompletableFuture<>();
        productRef.child(String.valueOf(userId)).child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
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

    public void addProduct(int userId, Product product) {
        DatabaseReference counterRef = FirebaseDatabase.getInstance()
                .getReference("counters/products").child(String.valueOf(userId));

        counterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentId = (snapshot.exists()) ? snapshot.getValue(Long.class) : 0;
                long newId = currentId + 1;
                counterRef.setValueAsync(newId);

                product.setId((int) newId);
                product.setUserId(userId); // keep userId inside product object

                // Save product under products/{userId}/{productId}
                productRef.child(String.valueOf(userId))
                        .child(String.valueOf(newId))
                        .setValueAsync(product);

                System.out.println("Product registered with id: " + newId + " for user " + userId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void deleteProduct(int userId, int id) {
        productRef.child(String.valueOf(userId)).child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    System.out.println("Product not found");
                } else {
                    productRef.child(String.valueOf(userId)).child(String.valueOf(id)).removeValueAsync();
                    System.out.println("Product deleted");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void updateProduct(int userId, int id, String name, double price, String expiration_date) {
        productRef.child(String.valueOf(userId)).child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    System.out.println("Product not found");
                } else {
                    Product existing = snapshot.getValue(Product.class);
                    if (existing != null) {
                        existing.setName(name);
                        existing.setPrice(price);
                        existing.setExpirationDate(expiration_date);
                        productRef.child(String.valueOf(userId)).child(String.valueOf(id)).setValueAsync(existing);
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

    public CompletableFuture<List<Product>> fetchAllProducts(int userId) {
        CompletableFuture<List<Product>> future = new CompletableFuture<>();

        productRef.child(String.valueOf(userId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<Product> products = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Product product = child.getValue(Product.class);
                            if (product != null) {
                                products.add(product);
                            }
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

    public CompletableFuture<List<Product>> fetchAllProductsGlobal() {
        CompletableFuture<List<Product>> future = new CompletableFuture<>();
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot userNode : snapshot.getChildren()) {
                    for (DataSnapshot child : userNode.getChildren()) {
                        Product product = child.getValue(Product.class);
                        if (product != null) {
                            products.add(product);
                        }
                    }
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

    public CompletableFuture<Product> fetchProductByNameGlobal(String name) {
        CompletableFuture<Product> future = new CompletableFuture<>();

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userNode : snapshot.getChildren()) {
                    for (DataSnapshot child : userNode.getChildren()) {
                        Product product = child.getValue(Product.class);
                        if (product != null && product.getName().equalsIgnoreCase(name)) {
                            future.complete(product);
                            return;
                        }
                    }
                }
                future.complete(null);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }
    //pour l'instant 5 top
    public CompletableFuture<List<Product>> findTopProducts() {
        CompletableFuture<List<Product>> future = new CompletableFuture<>();
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Product> allProducts = new ArrayList<>();
                for (DataSnapshot userNode : snapshot.getChildren()) {
                    for (DataSnapshot child : userNode.getChildren()) {
                        Product product = child.getValue(Product.class);
                        if (product != null) {
                            allProducts.add(product);
                        }
                    }
                }
                allProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                List<Product> top10 = allProducts.stream()
                        .limit(5)
                        .collect(Collectors.toList());

                future.complete(top10);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

        return future;
    }

}
