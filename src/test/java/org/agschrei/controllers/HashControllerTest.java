package org.agschrei.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.agschrei.data.HashAlgorithm;
import org.agschrei.data.HashResponse;
import org.agschrei.services.HashingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@MicronautTest
public class HashControllerTest {
    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    HashingService svc;

    @Test
    void getSha512() throws NoSuchAlgorithmException {
        when(svc.getSha512NoCache(eq("test"))).thenReturn("1234");
        var resp = client.toBlocking().retrieve(HttpRequest.GET("sha512/test"),
                HashResponse.class);
        assert(resp != null);
        assertEquals(HashAlgorithm.SHA512, resp.getAlgorithm());
        assertEquals("1234", resp.getHash());
    }

    @Test
    void getSha512Returns500() throws NoSuchAlgorithmException {
        when(svc.getSha512NoCache(any())).thenThrow(new NoSuchAlgorithmException());
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange("sha512/test"));
        assertEquals(500, ex.getResponse().code());
    }

    @Test
    void getCachedSha512Returns500() throws NoSuchAlgorithmException {
        when(svc.getSha512(any())).thenThrow(new NoSuchAlgorithmException());
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange("cache/sha512/test"));
        assertEquals(500, ex.getResponse().code());
    }

    @Test
    void getSha512Cached() throws NoSuchAlgorithmException {
        when(svc.getSha512(eq("test"))).thenReturn("1234");
        var resp = client.toBlocking().retrieve(HttpRequest.GET("cache/sha512/test"),
                HashResponse.class);
        assert(resp != null);
        assertEquals(HashAlgorithm.SHA512, resp.getAlgorithm());
        assertEquals("1234", resp.getHash());
    }

    @Test
    void getPbkdf2() throws InvalidKeySpecException, NoSuchAlgorithmException {
        when(svc.getPbkdf2(eq("test"), eq("salt".getBytes(StandardCharsets.UTF_8)))).thenReturn("1234");
        var resp = client.toBlocking().retrieve(HttpRequest.GET("pbkdf2/test/salt"),
                HashResponse.class);
        assert(resp != null);
        assertEquals(HashAlgorithm.PBKDF2, resp.getAlgorithm());
        assertEquals("1234", resp.getHash());
    }

    @MockBean(HashingService.class)
    HashingService getServiceMock(){
        return Mockito.mock(HashingService.class);
    }
}
