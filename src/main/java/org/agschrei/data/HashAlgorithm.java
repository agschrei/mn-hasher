package org.agschrei.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Supported Hashing algorithms")
public enum HashAlgorithm {
    PBKDF2,
    SHA512
}
