package ru.relex.rozhnovL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.relex.rozhnovL.entity.Curse;

import java.util.List;

@Repository
public interface CurseRepository extends JpaRepository<Curse, Long> {

    @Query("SELECT C FROM Curse C where C.currencyIdFrom=:currencyId OR C.currencyIdTo=:currencyId")
    List<Curse> getByCurrencyId(long currencyId);
}
