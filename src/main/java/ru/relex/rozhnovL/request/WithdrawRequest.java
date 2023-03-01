package ru.relex.rozhnovL.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawRequest {
    public String secret_key;
    public String currency;
    public String count;
    @JsonProperty("credit_card")
    @JsonAlias("wallet")
    public String to;
}
