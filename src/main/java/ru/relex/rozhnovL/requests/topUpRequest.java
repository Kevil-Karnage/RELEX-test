package ru.relex.rozhnovL.requests;

public class topUpRequest {
    public String secret_key;
    public String RUB_wallet;

    public topUpRequest(String secret_key, String RUB_wallet) {
        this.secret_key = secret_key;
        this.RUB_wallet = RUB_wallet;
    }
}
