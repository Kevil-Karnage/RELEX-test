package ru.relex.rozhnovL.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceExchangeResponse {
    @JsonProperty("currency_from")
    String currencyFrom;
    @JsonProperty("currency_to")
    String currencyTo;
    @JsonProperty("amount_from")
    String amountFrom;
    @JsonProperty("amount_to")
    String amountTo;

    public BalanceExchangeResponse(String currencyFrom, String currencyTo, String amountFrom, String amountTo) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }
}
