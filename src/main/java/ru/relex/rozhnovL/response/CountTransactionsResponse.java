package ru.relex.rozhnovL.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountTransactionsResponse {
    @JsonProperty("transaction_count")
    String count;

    public CountTransactionsResponse(String count) {
        this.count = count;
    }
}
