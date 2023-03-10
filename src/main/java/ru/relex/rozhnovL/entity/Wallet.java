package ru.relex.rozhnovL.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallets")
//@IdClass(WalletId.class)

@Getter
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    @Id
    @Column(name = "secret_key", nullable = false, updatable = false)
    private String secretKey;
    //    @Id
    @Column(name = "currency_id", nullable = false, updatable = false)
    private Long currencyId;


    @Column(name = "count", nullable = false)
    private Double count;

    public Wallet(String secretKey, Long currencyId, Double count) {
        this.secretKey = secretKey;
        this.currencyId = currencyId;
        this.count = count;
    }

    public void setCount(Double value) {
        this.count = value;
    }
}
