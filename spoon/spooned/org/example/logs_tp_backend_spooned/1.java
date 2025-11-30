package org.example.logs_tp_backend_spooned;
import DataSnapshot;
import DatabaseError;
import org.slf4j.Logger;
 {
    @Override
    public void onDataChange(DataSnapshot snapshot) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onDataChange");
        long currentId = (snapshot.exists()) ? snapshot.getValue(Long.class) : 0;
        long newId = currentId + 1;
        counterRef.setValueAsync(newId);
        user.setID(((int) (newId)));
        userRef.child(String.valueOf(newId)).setValueAsync(user);
        System.out.println("User registered with id: " + newId);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onCancelled");
        System.err.println("Error: " + error.getMessage());
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.1.class);
}