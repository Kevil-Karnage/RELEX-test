package ru.relex.rozhnovL.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "transactions")

@NoArgsConstructor
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "time", nullable = false, updatable = false)
    Date dateTime;

    @Column(name = "secret_key", nullable = false, updatable = false)
    String secretKey;

    @Column(name = "currency_from", updatable = false)
    Long fromId;

    @Column(name = "currency_to", updatable = false)
    Long toId;

    @Column(name = "count", updatable = false, nullable = false)
    Double count;

    public Transaction(Date dateTime, String secretKey, Long fromId, Long toId, double count) {
        this.dateTime = dateTime;
        this.secretKey = secretKey;
        this.fromId = fromId;
        this.toId = toId;
        this.count = count;
    }
}
