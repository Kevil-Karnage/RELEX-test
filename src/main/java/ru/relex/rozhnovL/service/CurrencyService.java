package ru.relex.rozhnovL.service;

import ru.relex.rozhnovL.entity.Currency;

public interface CurrencyService {
    Currency getById(Long id);

    Currency getByName(String name);
}
