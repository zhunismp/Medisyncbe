package com.mahidol.drugapi.auth.utils;

import com.mahidol.drugapi.notification.cron.sender.DrugCronSender;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class JWTUtil {

    private final String SECRET_KEY = "b63b08b8e7a911b5d63e8fd15e64c79fa4570389c7d9d9f156120eb3a2182b68";

    public String extractClaim(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName, String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

