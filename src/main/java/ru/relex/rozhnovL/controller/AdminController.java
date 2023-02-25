package ru.relex.rozhnovL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.User;
import ru.relex.rozhnovL.entity.Wallet;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    Services services;

    @GetMapping("/wallets/sum")
    public String sumAll(@RequestParam(name = "secret_key") String secretKey,
                         @RequestParam(name = "currency") String currencyName) {
        if (isAdmin(secretKey)) {
            Currency currency = services.currency.getByName(currencyName);
            List<Wallet> wallets = services.wallet.getAllByCurrencyId(currency.getId());

            double sum = 0;
            for (Wallet wallet : wallets) {
                sum += wallet.getCount();
            }

            return "{ \"" + currencyName + "\": \"" + sum + "\"}";
        } else {
            return notAvailable();
        }
    }


    private boolean isAdmin(String secretKey) {
        User user = services.user.getBySecretKey(secretKey);
        return user.isAdmin();
    }


    private String notAvailable() {
        return "{ \"response\": \"It is not available to you\"}";
    }
}
