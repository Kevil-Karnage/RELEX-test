package ru.relex.rozhnovL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.relex.rozhnovL.entity.Transaction;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT COUNT (T) FROM Transaction T WHERE T.dateTime > :begin AND T.dateTime < :end")
    int getCountBetweenDates(Date begin, Date end);
}
