package ru.relex.rozhnovL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.entity.Transaction;
import ru.relex.rozhnovL.entity.Wallet;
import ru.relex.rozhnovL.request.ExchangeCurrencyRequest;
import ru.relex.rozhnovL.request.TopUpRequest;
import ru.relex.rozhnovL.request.WithdrawRequest;
import ru.relex.rozhnovL.response.BalanceExchangeResponse;
import ru.relex.rozhnovL.response.BalanceTopUpResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    Services services;

    /**
     * (USER) Balance Check
     * @param secretKey
     * @return
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBalance(@RequestParam(name = "secret_key") String secretKey) {
        if (services.user.getBySecretKey(secretKey).isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Wallet> wallets = services.wallet.getAllBySecretKey(secretKey);

        return new ResponseEntity<>(walletsListToString(wallets), HttpStatus.OK);
    }

    /**
     * (USER) Balance Top Up
     * @param request
     * @return
     */
    @PostMapping(value = "/top-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> topUp(@RequestBody TopUpRequest request) {
        System.out.println(request);
        Wallet wallet = services.wallet.getMainBySecretKey(request.secret_key);
        double count = Double.parseDouble(request.RUB_wallet);

        if (wallet == null) {
            wallet = new Wallet(request.secret_key, services.currency.getByName("RUB").getId(), count);
        }

        saveTransaction(
                request.secret_key,
                null,
                services.currency.getByName("RUB").getId(),
                count
        );

        services.wallet.save(wallet);

        return new ResponseEntity<>(new BalanceTopUpResponse("" + count), HttpStatus.OK);
    }


    /**
     * (USER) Balance Withdraw Wallet(or Card)
     * @param request
     * @return
     */
    @PostMapping(value = "/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request) {
        if (services.user.getBySecretKey(request.secret_key).isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        System.out.println("???????????? ??????????????");
        Long currencyId = services.currency.getByName(request.currency).getId();

        Wallet wallet = services.wallet.getBySecretKeyAndCurrencyId(request.secret_key, currencyId);
        double count = Double.parseDouble(request.count);

        if (!isCorrectWithdrawRequest(request)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (wallet.getCount() < count)
            return new ResponseEntity<>("Not enough money", HttpStatus.CONFLICT);

        saveTransaction(
                request.secret_key,
                services.currency.getByName(request.currency).getId(),
                null,
                count
        );

        wallet.setCount(wallet.getCount() - count);
        services.wallet.save(wallet);

        Currency currency = services.currency.getById(wallet.getCurrencyId());
        Map<String, String> map = new HashMap<>();
        map.put(currency.getName() + "_wallet", String.format(services.doubleFormat, wallet.getCount()));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * (USER) Balance Exchange Currency
     * @param request
     * @return
     */
    @PostMapping(value = "/exchange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> exchange(@RequestBody ExchangeCurrencyRequest request) {
        if (services.user.getBySecretKey(request.secret_key).isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Long currencyIdFrom = services.currency.getByName(request.currency_from).getId();
        Long currencyIdTo = services.currency.getByName(request.currency_to).getId();
        Double countFrom = Double.parseDouble(request.amount);

        // ???????????????? ???? ?????????????????????? ?????????? ???????????? ?????????? ?? 1-???? ????????????????
        Wallet walletFrom = services.wallet.getBySecretKeyAndCurrencyId(request.secret_key, currencyIdFrom);
        if (walletFrom.getCount() < countFrom)
            return new ResponseEntity<>("Not enough money", HttpStatus.CONFLICT);

        // ?????????????????? 1-???? ???????????? ???? 2-????
        Curse curse = services.curse.getByCurrenciesIds(currencyIdFrom, currencyIdTo);
        double countTo;
        if (curse.getCurrencyIdFrom().equals(currencyIdFrom)) {
            countTo = countFrom / curse.getCount();
        } else {
            countTo = countFrom * curse.getCount();
        }

        // ?????????????????? ????????????????
        saveTransaction(request.secret_key, currencyIdFrom, currencyIdTo, countFrom);

        // ?????????????? ???????????????? ?? ???????????????? 1-???? ???????????? ?? ???????????????? ?????? ???????????? ?????????? ????????????
        walletFrom.setCount(walletFrom.getCount() - countFrom);
        services.wallet.save(walletFrom);

        // ???????????????? ?????????????? 2-???? ???????????? (?? ?????????????? ??????, ???????? ???? ?????????? ???? ????????????????????)
        Wallet walletTo = services.wallet.getBySecretKeyAndCurrencyId(request.secret_key, currencyIdTo);
        if (walletTo == null) {
            walletTo = services.wallet.save(new Wallet(request.secret_key, currencyIdTo, countTo));
        }

        // ?????????????????? ?????????????? 2-???? ???????????? ?? ???????????????? ?????? ???????????? ?????????? ????????????????????
        walletTo.setCount(walletTo.getCount() + countTo);
        services.wallet.save(walletTo);

        return new ResponseEntity<>(
                new BalanceExchangeResponse(
                        request.currency_from,
                        request.currency_to,
                        String.format(services.doubleFormat, walletFrom.getCount()),
                        String.format(services.doubleFormat, walletTo.getCount())
                ),
                HttpStatus.OK
        );
    }



    private boolean isCorrectWithdrawRequest(WithdrawRequest request) {
        if (Pattern.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}", request.to)) {
            if (!request.currency.equals("RUB")) {
                return false;
            }
        } else if (Pattern.matches("[A-Za-z\\d]+", request.to)) {
            if (request.currency.equals("RUB")) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private void saveTransaction(String secretKey, Long currencyFromId, Long currencyToId, Double count) {
        Transaction transaction = new Transaction(new Date(), secretKey, currencyFromId, currencyToId, count);
        services.transaction.save(transaction);
    }

    private Map<String, String> walletsListToString(List<Wallet> wallets) {
        Map<String, String> map = new HashMap<>();
        for (Wallet w: wallets) {
            map.put(services.currency.getById(w.getCurrencyId()).getName(), String.format(services.doubleFormat, w.getCount()));
        }

        return map;
    }
}
