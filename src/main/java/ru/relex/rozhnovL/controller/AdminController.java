package ru.relex.rozhnovL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.Wallet;
import ru.relex.rozhnovL.response.CountTransactionsResponse;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    Services services;

    @GetMapping(value = "/wallets/sum", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sumAll(@RequestParam(name = "secret_key") String secretKey,
                                 @RequestParam(name = "currency") String currencyName) {
        // check user access
        if (!services.user.getBySecretKey(secretKey).isAdmin())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Currency currency = services.currency.getByName(currencyName);
        List<Wallet> wallets = services.wallet.getAllByCurrencyId(currency.getId());

        double sum = 0;
        for (Wallet wallet : wallets) {
            sum += wallet.getCount();
        }

        Map<String, String> map = new HashMap<>();
        map.put(currencyName, String.valueOf(sum));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping(value = "/transactions/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCountTransactions(@RequestParam(name = "secret_key") String secretKey,
                                       @RequestParam(name = "date_from") String stringDateFrom,
                                       @RequestParam(name = "date_to") String stringDateTo) {
        // check user access
        if (!services.user.getBySecretKey(secretKey).isAdmin())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

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

        return new ResponseEntity<>(new CountTransactionsResponse("" + count), HttpStatus.OK);
    }
}
