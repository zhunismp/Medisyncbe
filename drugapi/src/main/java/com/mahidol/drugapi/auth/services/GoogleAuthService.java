package com.mahidol.drugapi.auth.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.mahidol.drugapi.common.exceptions.InternalServerError;
import com.mahidol.drugapi.common.utils.UserIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@Service
public class GoogleAuthService {

    @Value("${google.client-id}")
    private String CLIENT_ID;
    @Value("${salt.uuid}")
    private String SECRET;

    public UUID verify(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(CLIENT_ID)).build();
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) throw new IllegalArgumentException("Invalid token");
            else return UserIdGenerator.generate(idToken.getPayload().getSubject(), SECRET);

        } catch (GeneralSecurityException ex) {
            throw new IllegalArgumentException("Invalid credentials.");
        } catch (IOException ex) {
            throw new InternalServerError("IO thing go wrong");
        }
    }
}
