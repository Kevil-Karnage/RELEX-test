package ru.relex.rozhnovL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.relex.rozhnovL.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT U from User U WHERE U.secretKey = :secretKey")
    User getBySecretKey(String secretKey);
}
