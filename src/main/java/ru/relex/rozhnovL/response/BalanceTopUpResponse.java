package ru.relex.rozhnovL.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceTopUpResponse {
    @JsonProperty("RUB_wallet")
    String count;

    public BalanceTopUpResponse(String count) {
        this.count = count;
    }
}
