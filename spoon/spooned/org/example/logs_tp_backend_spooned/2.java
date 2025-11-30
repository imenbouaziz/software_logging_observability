package org.example.logs_tp_backend_spooned;
import DataSnapshot;
import DatabaseError;
import org.slf4j.Logger;
 {
    @Override
    public void onDataChange(DataSnapshot snapshot) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onDataChange");
        if (snapshot.exists()) {
            org.example.User user = snapshot.getValue(org.example.User.class);
            System.out.println("Found user: " + user.getName());
        } else {
            System.out.println("User not found");
        }
    }

    @Override
    public void onCancelled(DatabaseError error) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onCancelled");
        System.err.println("Error: " + error.getMessage());
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.2.class);
}