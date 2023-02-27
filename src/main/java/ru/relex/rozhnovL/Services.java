package ru.relex.rozhnovL;

import org.springframework.beans.factory.annotation.Autowired;
import ru.relex.rozhnovL.service.*;

public class Services {
    @Autowired
    public CurrencyService currency;
    @Autowired
    public CurseService curse;
    @Autowired
    public TransactionService transaction;
    @Autowired
    public UserService user;
    @Autowired
    public WalletService wallet;

    public Services(
            CurrencyService currency,
            CurseService curse,
            TransactionService transaction,
            UserService user,
            WalletService wallet) {
        this.currency = currency;
        this.curse = curse;
        this.transaction = transaction;
        this.user = user;
        this.wallet = wallet;
    }
}

