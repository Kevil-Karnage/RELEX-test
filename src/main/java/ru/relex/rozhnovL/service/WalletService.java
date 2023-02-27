package ru.relex.rozhnovL.service;

import ru.relex.rozhnovL.entity.Wallet;

import java.util.List;

public interface WalletService {

    Wallet save(Wallet wallet);

    List<Wallet> getAllBySecretKey(String secretKey);

    Wallet getMainBySecretKey(String secretKey);

    Wallet getBySecretKeyAndCurrencyId(String secretKey, long currencyId);

    List<Wallet> getAllByCurrencyId(Long currencyId);

}
