package ru.relex.rozhnovL.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.repository.CurrencyRepository;
import ru.relex.rozhnovL.service.CurrencyService;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository repository;

    @Override
    public Currency getById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Currency getByName(String currencyName) {
        return repository.getByName(currencyName);
    }
}
