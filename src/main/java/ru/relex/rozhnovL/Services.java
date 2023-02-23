package ru.relex.rozhnovL;

import org.springframework.beans.factory.annotation.Autowired;
import ru.relex.rozhnovL.service.CurrencyService;
import ru.relex.rozhnovL.service.CurseService;
import ru.relex.rozhnovL.service.UserService;
import ru.relex.rozhnovL.service.WalletService;

public class Services {
    @Autowired
    public CurrencyService currencyService;
    @Autowired
    public CurseService curseService;
    @Autowired
    public UserService userService;
    @Autowired
    public WalletService walletService;

    public Services(
            CurrencyService currencyService,
            CurseService curseService,
            UserService userService,
            WalletService walletService) {
        this.currencyService = currencyService;
        this.curseService = curseService;
        this.userService = userService;
        this.walletService = walletService;
    }
}

