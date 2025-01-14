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

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseInitializer {
    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Bean
    GoogleCredentials googleCredentials() {
        try {
            return GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());
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
