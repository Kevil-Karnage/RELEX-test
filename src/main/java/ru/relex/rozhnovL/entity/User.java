package ru.relex.rozhnovL.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")

@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "username", unique = true, nullable = false)
    String username;
    @Column(name = "email", unique = true, nullable = false)
    String email;
    @Column(name = "key", unique = true, nullable = false)
    String secretKey;

    @Column(name = "is_admin", nullable = false)
    boolean isAdmin;

    public User(String username, String email, String secretKey, boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.secretKey = secretKey;
        this.isAdmin = isAdmin;
    }
}
