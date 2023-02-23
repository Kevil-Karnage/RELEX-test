package ru.relex.rozhnovL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.relex.rozhnovL.entity.Wallet;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT w from Wallet w where w.secretKey = :secretKey")
    List<Wallet> getAllBySecretKey(String secretKey);

    @Query("SELECT w from Wallet w where w.secretKey = :secretKey and w.currencyId=:currencyId")
    Wallet getBySecretKeyAndCurrencyId(String secretKey, long currencyId);


}
