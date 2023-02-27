package ru.relex.rozhnovL.service;

import ru.relex.rozhnovL.entity.Transaction;

import java.util.Date;

public interface TransactionService {
    Transaction save(Transaction transaction);
    int getCountBetweenDates(Date from, Date begin);
}
