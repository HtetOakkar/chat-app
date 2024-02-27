package org.oakkar.chatapp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Value("${firebase.service-account}")
    private String SERVICE_ACCOUNT;

    @Bean
    GoogleCredentials googleCredentials() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT));
    }

    @Bean
    FirebaseApp firebaseApp() throws IOException{
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials())
                .build();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    Firestore firestore() throws IOException {
        return FirestoreClient.getFirestore(firebaseApp());
    }
}

