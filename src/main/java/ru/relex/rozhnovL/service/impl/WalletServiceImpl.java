package ru.relex.rozhnovL.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.relex.rozhnovL.entity.Wallet;
import ru.relex.rozhnovL.repository.WalletRepository;
import ru.relex.rozhnovL.service.WalletService;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository repository;

    @Override
    public Wallet saveWallet(Wallet wallet) {
        return repository.saveAndFlush(wallet);
    }

    @Override
    public List<Wallet> getAllBySecretKey(String secretKey) {
        return repository.getAllBySecretKey(secretKey);
    }

    @Override
    public Wallet getMainBySecretKey(String secretKey) {
        return repository.getBySecretKeyAndCurrencyId(secretKey, 1L);
    }

    @Override
    public Wallet getBySecretKeyAndCurrencyId(String secretKey, long currencyId) {
        return repository.getBySecretKeyAndCurrencyId(secretKey, currencyId);
    }

    @Override
    public List<Wallet> getAllByCurrencyId(Long currencyId) {
        return repository.getAllByCurrencyId(currencyId);
    }
}
