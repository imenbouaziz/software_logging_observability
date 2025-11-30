package org.example;

import com.google.firebase.database.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private DatabaseReference userRef;

    public UserRepository() {

    }

    @PostConstruct
    public void init() {
        this.userRef = FirebaseDatabase.getInstance().getReference("users");
    }


    public void addUser(User user) {
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("counters/users");

        counterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentId = snapshot.exists() ? snapshot.getValue(Long.class) : 0;
                long newId = currentId + 1;
                counterRef.setValueAsync(newId);
                user.setID((int)newId);
                userRef.child(String.valueOf(newId)).setValueAsync(user);

                System.out.println("User registered with id: " + newId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void fetchUser(int id) {
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

    public User fetchUserByEmail(String email) {
        userRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                User user = child.getValue(User.class);
                                System.out.println("Found user: " + user.getName());
                                return;
                            }
                        } else {
                            System.out.println("No user found with email: " + email);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Error: " + error.getMessage());
                    }
                });
        return null;
    }
}
