package ru.relex.rozhnovL.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "currency")

@Getter
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "name", updatable = false, nullable = false)
    String name;
}
