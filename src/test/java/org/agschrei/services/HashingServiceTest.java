package org.agschrei.services;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class HashingServiceTest {

    static final String expectedSha512 = "0E06000D0A0707090A0A0E03010A09021F111910171E171F18191514141E1D122A212528202" +
            "125282F282C282F262B24373233343C3F393D3E3F37353038383F";
    static final String expectedPbkdf2 = "0404090C080505050A020F0E0004010B1C12101714151010101A18121B1910142F212F2E242" +
            "A21262B26262928292724333A313C3D32383D3130333C39343A3F";
    static final String message = "test";

    @Inject
    HashingService hasher;

    @Test
    public void getSha512() throws NoSuchAlgorithmException {
        assertEquals(expectedSha512, hasher.getSha512NoCache(message));
    }

    @Test
    public void getSha512Cacheable() throws NoSuchAlgorithmException {
        String result = hasher.getSha512(message);
        String resultCached = hasher.getSha512(message);
        assertEquals(expectedSha512, result);
        assertEquals(result, resultCached);
    }

    @Test
    public void getSha512CacheableOfNull() throws NoSuchAlgorithmException {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> hasher.getSha512NoCache(null));
        assertTrue(ex.getMessage().matches(".*null.+message!$"));
    }

    @Test
    public void getSha512OfNull() throws NoSuchAlgorithmException {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> hasher.getSha512(null));
        assertTrue(ex.getMessage().matches(".*null.+message!$"));
    }

    @Test
    public void getPbkdf2() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String result = hasher.getPbkdf2(message, "salty".getBytes(StandardCharsets.UTF_8));
        assertEquals(expectedPbkdf2, result);
    }

}
