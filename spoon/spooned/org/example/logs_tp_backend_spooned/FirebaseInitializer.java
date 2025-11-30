package org.example.logs_tp_backend_spooned;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FirebaseInitializer {
    @PostConstruct
    public static void initialize() throws IOException {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "initialize");
        FileInputStream serviceAccount = new FileInputStream("/home/imene/Downloads/logs-tp-db-firebase-adminsdk-fbsvc-6183065085.json");
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl("https://logs-tp-db-default-rtdb.europe-west1.firebasedatabase.app/").build();
        FirebaseApp.initializeApp(options);
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.FirebaseInitializer.class);
}