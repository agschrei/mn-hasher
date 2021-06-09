package org.agschrei.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.agschrei.data.HashAlgorithm;
import org.agschrei.data.HashResponse;
import org.agschrei.services.HashingService;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller("/")
@Tag(name = "Hasher")
@ExecuteOn(TaskExecutors.IO)
public class HashController {

    @Inject
    HashingService hashingService;

    @Operation(summary = "Returns pbkdf2 hash for input text",
            description = "Computes 1000 iterations of PBKDF2-SHA256 with a random 128-bit salt")
    @Get(uri = "/pbkdf2/{input}", produces = MediaType.APPLICATION_JSON)
    public Single<HttpResponse<HashResponse>> getPbkdf2Hash(String input) {
        try {
            return Single.just(HttpResponse.ok().body(
                    new HashResponse(HashAlgorithm.PBKDF2, hashingService.getPbkdf2(input)))
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return Single.just(HttpResponse.serverError());
        }
    }

    @Get(uri = "cache/sha512/{input}", produces = MediaType.APPLICATION_JSON)
    public Single<HttpResponse<HashResponse>> getSha512Hash(String input) {
        try {
            return Single.just(HttpResponse.ok().body(
                    new HashResponse(HashAlgorithm.SHA512, hashingService.getSha512(input)))
            );
        } catch (NoSuchAlgorithmException ex) {
            return Single.just(HttpResponse.serverError());
        }
    }

    @Get(uri = "/sha512/{input}", produces = MediaType.APPLICATION_JSON)
    public Single<HttpResponse<HashResponse>> getSha512HashNoCache(String input) {
        try {
            return Single.just(HttpResponse.ok().body(
                    new HashResponse(HashAlgorithm.SHA512, hashingService.getSha512NoCache(input)))
            );
        } catch (NoSuchAlgorithmException ex) {
            return Single.just(HttpResponse.serverError());
        }
    }
}
