package ru.relex.rozhnovL.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpResponse {
    @JsonProperty("secret_key")
    public String secretKey;

    public SignUpResponse(String secretKey) {
        this.secretKey = secretKey;
    }
}
