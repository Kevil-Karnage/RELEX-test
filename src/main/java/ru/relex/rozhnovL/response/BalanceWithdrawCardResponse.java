package ru.relex.rozhnovL.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceWithdrawCardResponse {
    @JsonProperty("RUB_wallet")
    String count;

    public BalanceWithdrawCardResponse(String count) {
        this.count = count;
    }
}
