package com.mahidol.drugapi.external.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mahidol.drugapi.common.exceptions.InternalServerError;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FCMInitializer {
    @Value("")
    private String firebaseConfigPath;

    @PostConstruct
    public void initializer() {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())).build();

            if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new InternalServerError("Can not resolve firebase configuration path");
        }
    }
}
