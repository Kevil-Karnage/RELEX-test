package ru.relex.rozhnovL.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Curse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;


}
