package org.example.logs_tp_backend_spooned;
import DataSnapshot;
import DatabaseError;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
 {
    @Override
    public void onDataChange(DataSnapshot snapshot) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onDataChange");
        List<org.example.Product> products = new ArrayList<>();
        for (DataSnapshot child : snapshot.getChildren()) {
            products.add(child.getValue(org.example.Product.class));
        }
        future.complete(products);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "onCancelled");
        future.completeExceptionally(new RuntimeException(error.getMessage()));
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.5.class);
}