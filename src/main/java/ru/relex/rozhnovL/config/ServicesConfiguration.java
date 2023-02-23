package ru.relex.rozhnovL.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.service.impl.CurrencyServiceImpl;
import ru.relex.rozhnovL.service.impl.UserServiceImpl;
import ru.relex.rozhnovL.service.impl.WalletServiceImpl;

@Configuration
public class ServicesConfiguration {
    @Bean
    Services init() {
        return new Services(
                new CurrencyServiceImpl(),
                new UserServiceImpl(),
                new WalletServiceImpl());
    }
}
