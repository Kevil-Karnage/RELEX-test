package ru.relex.rozhnovL.requests;

public class TopUpRequest {
    public String secret_key;
    public String RUB_wallet;

    public TopUpRequest(String secret_key, String RUB_wallet) {
        this.secret_key = secret_key;
        this.RUB_wallet = RUB_wallet;
    }
}
