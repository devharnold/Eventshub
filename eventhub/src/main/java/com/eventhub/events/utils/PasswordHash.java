package com.eventhub.events.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHash {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    /**
     * Hash a plain-text password using PBKDF2 with a generated salt
     *
     * @param password the plain-text password
     * @return a hash in the format: salt:hash (both should come out as base64 encoded)
     */
    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            // generate hash
            byte[] hash = computePBKDF2Hash(password.toCharArray(), salt);
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while trying to hash password", e);
        }
    }

    private static byte[] computePBKDF2Hash(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }

    /**
     * verify a plain-text password against a hashedPassword
     *
     * @param password the plain-text password
     * @param storedHashSalt the stored hash in salt:hash format (base64.encoded)
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHashSalt) {
        try {
            String[] parts = storedHashSalt.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHash = Base64.getDecoder().decode(parts[1]);

            byte[] computedHash = computePBKDF2Hash(password.toCharArray(), salt);

            return constantTimeArrayEquals(storedHash, computedHash);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean constantTimeArrayEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i]; // perform bitwise XOR, if equal, result is 0 : non-zero then `result |= accumulates any difference found`
        }
        return result == 0;
    }
}
