package ru.relex.rozhnovL.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.repository.CurseRepository;
import ru.relex.rozhnovL.service.CurseService;

import java.util.List;

@Service
public class CurseServiceImpl implements CurseService {

    @Autowired
    CurseRepository repository;

    @Override
    public List<Curse> getByCurrencyId(long currencyId) {
        return repository.getByCurrencyId(currencyId);
    }
}
