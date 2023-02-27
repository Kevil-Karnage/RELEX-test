package ru.relex.rozhnovL.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.relex.rozhnovL.entity.Transaction;
import ru.relex.rozhnovL.repository.TransactionRepository;
import ru.relex.rozhnovL.service.TransactionService;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository repository;

    @Override
    public Transaction save(Transaction transaction) {
        return repository.saveAndFlush(transaction);
    }

    @Override
    public int getCountBetweenDates(Date begin, Date end) {
        return repository.getCountBetweenDates(begin, end);
    }
}
