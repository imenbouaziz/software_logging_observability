package org.example.logs_tp_backend_spooned;
import DataSnapshot;
import DatabaseError;
import org.slf4j.Logger;
 {
    @Override
    public void onDataChange(DataSnapshot snapshot) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onDataChange");
        if (!snapshot.exists()) {
            System.out.println("Product not found");
        } else {
            org.example.Product existing = snapshot.getValue(org.example.Product.class);
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
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onCancelled");
        System.err.println("Error: " + error.getMessage());
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.4.class);
}