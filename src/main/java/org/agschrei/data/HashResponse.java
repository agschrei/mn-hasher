package org.agschrei.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "HashResponse", description = "Response type that returns hash digest and algorithm info")
public class HashResponse {
    private String hash;

    private HashAlgorithm algorithm;

    @JsonCreator
    public HashResponse(@JsonProperty("algorithm") HashAlgorithm algorithm, @JsonProperty("hash") String hash) {
        this.algorithm = algorithm;
        this.hash = hash;
    }

    @JsonGetter("hash")
    public String getHash() {
        return hash;
    }

    @JsonSetter("hash")
    public void setHash(String hash) {
        this.hash = hash;
    }

    @JsonGetter("algorithm")
    public HashAlgorithm getAlgorithm() {
        return algorithm;
    }

    @JsonSetter("algorithm")
    public void setAlgorithm(HashAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
}
