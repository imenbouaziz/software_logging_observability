package org.example;

import com.google.firebase.FirebaseOptions;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {
    public static void initialize() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("/home/imene/Downloads/logs-tp-db-firebase-adminsdk-fbsvc-6183065085.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://logs-tp-db-default-rtdb.europe-west1.firebasedatabase.app/")
                .build();
        FirebaseApp.initializeApp(options);

    }
}
