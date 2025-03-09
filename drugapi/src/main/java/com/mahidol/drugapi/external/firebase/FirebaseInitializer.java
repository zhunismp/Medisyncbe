package com.mahidol.drugapi.external.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mahidol.drugapi.common.exceptions.InternalServerError;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseInitializer {
    @Value("${firebase.config.base64}")
    private String firebaseConfigBase64;

    @Bean
    GoogleCredentials googleCredentials() {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(firebaseConfigBase64);
            return GoogleCredentials.fromStream(new ByteArrayInputStream(decodedBytes));
        } catch (IOException e) {
            throw new InternalServerError("Can not resolve firebase configuration path");
        }
    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
