package org.agschrei.services;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@Singleton
@CacheConfig("hashes")
public class HashingService {

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public String getPbkdf2(String message) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = getRandomBytesOfLength(16);
        return getPbkdf2(message, salt);
    }

    @Cacheable(parameters = {"message", "salt"})
    public String getPbkdf2(String message, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10000;
        char[] chars = message.toCharArray();
        //128bit salt recommended here
        PBEKeySpec pbeSpec = new PBEKeySpec(chars, salt, iterations, 512);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(pbeSpec).getEncoded();
        return getHexFromBytes(hash);
    }

    @Cacheable
    public String getSha512(String message) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bytes = md.digest(message.getBytes(StandardCharsets.UTF_8));
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getHexFromBytes(bytes);
    }

    public String getSha512NoCache(String message) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bytes = md.digest(message.getBytes(StandardCharsets.UTF_8));
        return getHexFromBytes(bytes);
    }

    private byte[] getRandomBytesOfLength(int length) throws NoSuchAlgorithmException {
        SecureRandom secRng = SecureRandom.getInstance("SHA1PRNG");
        byte[] randomBytes = new byte[length];
        secRng.nextBytes(randomBytes);
        return randomBytes;
    }

    private String getHexFromBytes(byte[] bytes) {
        //we know each byte needs two characters
        byte[] hexChars = new byte[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int maskedByte = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[i >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[maskedByte & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
