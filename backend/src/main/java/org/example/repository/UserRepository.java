package org.example.repository;

import com.google.firebase.database.*;
import jakarta.annotation.PostConstruct;
import org.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public class UserRepository {
    private DatabaseReference userRef;

    public UserRepository() {

    }

    @PostConstruct
    public void init() {
        this.userRef = FirebaseDatabase.getInstance().getReference("users");
    }


    public void registerUser(User user) {
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("counters/users");

        counterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentId = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                long newId = currentId + 1;
                counterRef.setValueAsync(newId);
                user.setId((int)newId);
                userRef.child(String.valueOf(newId)).setValueAsync(user);

                System.out.println("User registered with id: " + newId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void getUser(int id) {
        userRef.child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            System.out.println("Found user: " + user.getName());
                        } else {
                            System.out.println("User not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Error: " + error.getMessage());
                    }
                });
    }

    public User getUserByEmail(String email) {
        CompletableFuture<User> future = new CompletableFuture<>();
        userRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                User user = child.getValue(User.class);
                                //setting the id properly
                                user.setId(Integer.parseInt(child.getKey()));
                                future.complete(user);
                                return;
                            }
                        }
                        future.complete(null);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        try {
            return future.get(); //blocking firebase responses
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
