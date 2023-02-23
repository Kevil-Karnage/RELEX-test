package ru.relex.rozhnovL;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.relex.rozhnovL.service.CurrencyService;
import ru.relex.rozhnovL.service.UserService;
import ru.relex.rozhnovL.service.WalletService;

public class Services {
    @Autowired
    public CurrencyService currencyService;
    @Autowired
    public UserService userService;
    @Autowired
    public WalletService walletService;

    public Services(CurrencyService currencyService, UserService userService, WalletService walletService) {
        this.currencyService = currencyService;
        this.userService = userService;
        this.walletService = walletService;
    }
}

