package ru.relex.rozhnovL.service;

import ru.relex.rozhnovL.entity.Wallet;

import java.util.List;

public interface WalletService {

    Wallet saveWallet(Wallet wallet);

    List<Wallet> getAllBySecretKey(String secretKey);

    Wallet getMainBySecretKey(String secretKey);

    Wallet getBySecretKeyAndCurrencyId(String secretKey, long currencyId);

}
