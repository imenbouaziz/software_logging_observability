package org.example;

import com.google.firebase.database.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ProductRepository {
    private DatabaseReference productRef;

    public ProductRepository() {
    }
    //init after spring got ready not directly
    @PostConstruct
    public void init() {
        this.productRef = FirebaseDatabase.getInstance().getReference("products");
    }

    public void fetchProductById(int id, int userId) {
        CompletableFuture<Product> future = new CompletableFuture<>();
        productRef.child(String.valueOf(userId)).child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("counters/products" + userId);

        counterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentId = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                long newId = currentId + 1;
                counterRef.setValueAsync(newId);
                product.setId((int)newId);
                productRef.child(String.valueOf(userId)).child(String.valueOf(newId)).setValueAsync(product);

                System.out.println("Product added with id: " + newId + " =for user" + userId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }


    public void deleteProduct(int  userId, int id) {
        productRef.child(String.valueOf(userId)).child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void updateProduct(int userId,  int id, String name, double price, LocalDate expiration_date) {
        productRef.child(String.valueOf(userId)).child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            System.out.println("Product not found");
                        } else {
                            Product existing = snapshot.getValue(Product.class);
                            if (existing != null) {
                                existing.setName(name);
                                existing.setPrice(price);
                                existing.setExpiration_date(expiration_date);
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
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    products.add(child.getValue(Product.class));
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

}
