package ru.relex.rozhnovL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.Wallet;
import ru.relex.rozhnovL.responses.BadResponse;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    Services services;

    @GetMapping("/wallets/sum")
    public String sumAll(@RequestParam(name = "secret_key") String secretKey,
                         @RequestParam(name = "currency") String currencyName) {
        if (!services.user.getBySecretKey(secretKey).isAdmin())
            return new BadResponse("Access denied").toString();

        Currency currency = services.currency.getByName(currencyName);
        List<Wallet> wallets = services.wallet.getAllByCurrencyId(currency.getId());

        double sum = 0;
        for (Wallet wallet : wallets) {
            sum += wallet.getCount();
        }

        return "{\n\"" + currencyName + "\": \"" + sum + "\"\n}";
    }

    @GetMapping("/transactions/count")
    public String getCountTransactions(@RequestParam(name = "secret_key") String secretKey,
                                       @RequestParam(name = "date_from") String stringDateFrom,
                                       @RequestParam(name = "date_to") String stringDateTo) {
        if (!services.user.getBySecretKey(secretKey).isAdmin())
            return new BadResponse("Access denied").toString();

        Date dateFrom;
        Date dateTo;

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            dateFrom = format.parse(stringDateFrom);
            dateTo = format.parse(stringDateTo);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int count = services.transaction.getCountBetweenDates(dateFrom, dateTo);

        return "{\n \"transaction_count\": \"" + count + "\"\n}";
    }
}
