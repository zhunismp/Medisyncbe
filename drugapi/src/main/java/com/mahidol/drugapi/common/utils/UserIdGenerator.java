package com.mahidol.drugapi.common.utils;

import com.mahidol.drugapi.common.exceptions.InternalServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UserIdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UserIdGenerator.class);

    public static UUID generate(String key, String secret) {
        try {
            String input = key + secret;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            long mostSigBits = 0;
            long leastSigBits = 0;
            for (int i = 0; i < 8; i++) {
                mostSigBits = (mostSigBits << 8) | (hashBytes[i] & 0xff);
                leastSigBits = (leastSigBits << 8) | (hashBytes[i + 8] & 0xff);
            }
            logger.info("MostSigBits: " + mostSigBits);
            logger.info("LeastSigBits: " + leastSigBits);
            return new UUID(mostSigBits, leastSigBits);

        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerError("SHA-256 algorithm not available");
        }
    }
}
