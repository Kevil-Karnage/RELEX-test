package ru.relex.rozhnovL.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "wallets")
//@IdClass(WalletId.class)
@Getter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

//    @Id
    @Column(name = "currency_id", nullable = false, updatable = false)
    private Long currencyId;

//    @Id
    @Column(name = "secret_key", nullable = false, updatable = false)
    private String secretKey;

    @Column(name = "count", nullable = true)
    private Double count;

    public void setCount(Double value) {
        this.count = value;
    }
}
