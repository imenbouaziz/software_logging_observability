package org.example.logs_tp_backend_spooned;
import DataSnapshot;
import DatabaseError;
import FirebaseDatabase;
import ValueEventListener;
import jakarta.annotation.PostConstruct;
import org.example.DatabaseReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
@Repository
public class UserRepository {
    private DatabaseReference userRef;

    public UserRepository() {
    }

    @PostConstruct
    public void init() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "init");
        this.userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void addUser(org.example.User user) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "addUser");
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("counters/users");
        counterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long currentId = (snapshot.exists()) ? snapshot.getValue(Long.class) : 0;
                long newId = currentId + 1;
                counterRef.setValueAsync(newId);
                user.setID(((int) (newId)));
                org.example.UserRepository.this.userRef.child(String.valueOf(newId)).setValueAsync(user);
                System.out.println("User registered with id: " + newId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public void getUser(int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getUser");
        this.userRef.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    org.example.User user = snapshot.getValue(org.example.User.class);
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

    public void getUserByEmail(String email) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getUserByEmail");
        this.userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        org.example.User user = child.getValue(org.example.User.class);
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
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.UserRepository.class);
}