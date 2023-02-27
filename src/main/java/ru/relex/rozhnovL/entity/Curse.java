package ru.relex.rozhnovL.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "curses")

@Getter
public class Curse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "currency_id_from", nullable = false)
    Long currencyIdFrom;

    @Column(name = "currency_id_to", nullable = false)
    Long currencyIdTo;

    @Column(name = "count", nullable = false)
    Double count;

    public void setCount(Double count) {
        this.count = count;
    }
}
