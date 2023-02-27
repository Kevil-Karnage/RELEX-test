package ru.relex.rozhnovL.controller;

import generator.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.Wallet;

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
        // check user access
        if (!services.user.getBySecretKey(secretKey).isAdmin())
            return JsonGenerator.generateBadResponse("Access denied");

        Currency currency = services.currency.getByName(currencyName);
        List<Wallet> wallets = services.wallet.getAllByCurrencyId(currency.getId());

        double sum = 0;
        for (Wallet wallet : wallets) {
            sum += wallet.getCount();
        }

        return JsonGenerator.generateJsonResponse(currencyName, "" + sum);
    }

    @GetMapping("/transactions/count")
    public String getCountTransactions(@RequestParam(name = "secret_key") String secretKey,
                                       @RequestParam(name = "date_from") String stringDateFrom,
                                       @RequestParam(name = "date_to") String stringDateTo) {
        // check user access
        if (!services.user.getBySecretKey(secretKey).isAdmin())
            return JsonGenerator.generateBadResponse("Access denied");

        Date dateFrom;
        Date dateTo;

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            dateFrom = format.parse(stringDateFrom);
            dateTo = format.parse(stringDateTo);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int count = services.transaction.getCountBetweenDates(dateFrom, dateTo);

        return JsonGenerator.generateJsonResponse("transaction_count", "" + count);
    }
}
