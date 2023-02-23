package ru.relex.rozhnovL.entity;

import java.io.Serializable;

public class WalletId implements Serializable {
    private Long currencyId;
    private String secretKey;

    public WalletId(Long currencyId, String secretKey) {
        this.currencyId = currencyId;
        this.secretKey = secretKey;
    }
}
