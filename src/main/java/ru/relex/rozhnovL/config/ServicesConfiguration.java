package ru.relex.rozhnovL.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.service.impl.*;

@Configuration
public class ServicesConfiguration {
    @Bean
    Services init() {
        return new Services(
                new CurrencyServiceImpl(),
                new CurseServiceImpl(),
                new TransactionServiceImpl(),
                new UserServiceImpl(),
                new WalletServiceImpl()
        );
    }
}
