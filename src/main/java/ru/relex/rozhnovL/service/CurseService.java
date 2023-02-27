package ru.relex.rozhnovL.service;

import ru.relex.rozhnovL.entity.Curse;

import java.util.List;

public interface CurseService {

    Curse save(Curse curse);
    List<Curse> getByCurrencyId(long currencyId);
    Curse getByCurrenciesIds(long currencyIdFrom, long currencyIdTo);
}
