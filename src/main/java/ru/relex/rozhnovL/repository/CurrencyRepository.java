package ru.relex.rozhnovL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.relex.rozhnovL.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query("SELECT C from Currency C where C.name = :name")
    Currency getByName(String name);
}
