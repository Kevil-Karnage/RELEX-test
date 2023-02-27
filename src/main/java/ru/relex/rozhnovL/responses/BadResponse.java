package ru.relex.rozhnovL.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BadResponse {

    @JsonProperty("response")
    String response;

    public BadResponse(String response) {
        this.response = response;
    }
}
