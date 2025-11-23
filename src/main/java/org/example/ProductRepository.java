package org.example;

import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private final DatabaseReference productRef;

    public ProductRepository() {
        this.productRef = FirebaseDatabase.getInstance().getReference("products");
    }

    public void getProductById(int id) {
        productRef.child(String.valueOf(id))
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

    public void addProduct(Product product) {
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("counters/products");

        counterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentId = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                long newId = currentId + 1;
                counterRef.setValueAsync(newId);
                product.setId((int)newId);
                productRef.child(String.valueOf(newId)).setValueAsync(product);

                System.out.println("Product added with id: " + newId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }


    public void deleteProduct(int id) {
        productRef.child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            System.out.println("Product not found");
                        } else {
                            productRef.child(String.valueOf(id)).removeValueAsync();
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
        productRef.child(String.valueOf(id))
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
                                productRef.child(String.valueOf(id)).setValueAsync(existing);
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

    public void getAllProducts() {
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    products.add(child.getValue(Product.class));
                }
                System.out.println("Products: " + products);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }
}
