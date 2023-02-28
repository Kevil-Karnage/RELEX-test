package ru.relex.rozhnovL.controller;

import generator.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.entity.Transaction;
import ru.relex.rozhnovL.entity.Wallet;
import ru.relex.rozhnovL.request.ExchangeCurrencyRequest;
import ru.relex.rozhnovL.request.TopUpRequest;
import ru.relex.rozhnovL.request.WithdrawCardRequest;
import ru.relex.rozhnovL.request.WithdrawWalletRequest;

import java.util.Date;
import java.util.List;

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
    @GetMapping("")
    public String getBalance(@RequestParam(name = "secretKey") String secretKey) {
        List<Wallet> wallets = services.wallet.getAllBySecretKey(secretKey);

        return walletsListToString(wallets);
    }

    /**
     * (USER) Balance Top Up
     * @param request
     * @return
     */
    @PostMapping("/top-up")
    public String topUp(@RequestBody TopUpRequest request) {
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

        return changeWalletBalance(wallet, count);
    }

    /**
     * (USER) Balance Withdraw Credit Card
     * @param request
     * @return
     */
    @PostMapping("/withdraw/card")
    public String withdraw(@RequestBody WithdrawCardRequest request) {
        System.out.println("Снятие средств на карту");

        Wallet wallet = services.wallet.getMainBySecretKey(request.secret_key);
        double count = Double.parseDouble(request.count);

        if (wallet.getCount() < count) {
            return JsonGenerator.generateBadResponse("Not enough money");
        }

        saveTransaction(
                request.secret_key,
                services.currency.getByName("RUB").getId(),
                null,
                count
        );

        return changeWalletBalance(wallet, -count);
    }

    /**
     * (USER) Balance Withdraw Wallet
     * @param request
     * @return
     */
    @PostMapping("/withdraw/wallet")
    public String withdraw(@RequestBody WithdrawWalletRequest request) {
        System.out.println("Снятие средств на кошелёк");
        Long currencyId = services.currency.getByName(request.currency).getId();

        Wallet wallet =
                services.wallet.getBySecretKeyAndCurrencyId(request.secret_key, currencyId);
        double count = Double.parseDouble(request.count);

        if (wallet.getCount() < count) {
            return "{ \"response\": \"not enough money\"}";
        }

        saveTransaction(
                request.secret_key,
                services.currency.getByName(request.currency).getId(),
                null,
                count
        );

        return changeWalletBalance(wallet, -count);
    }

    /**
     * (USER) Balance Exchange Currency
     * @param request
     * @return
     */
    @PostMapping("/exchange")
    public String exchange(@RequestBody ExchangeCurrencyRequest request) {
        Long currencyIdFrom = services.currency.getByName(request.currency_from).getId();
        Long currencyIdTo = services.currency.getByName(request.currency_to).getId();
        Double countFrom = Double.parseDouble(request.amount);

        // проверка на возможность снять нужную сумму с 1-го кошелька
        Wallet walletFrom = services.wallet.getBySecretKeyAndCurrencyId(request.secret_key, currencyIdFrom);
        if (walletFrom.getCount() < countFrom) {
            return JsonGenerator.generateBadResponse("not enough money on wallet");
        }

        // переводим 1-ую валюту во 2-ую
        Curse curse = services.curse.getByCurrenciesIds(currencyIdFrom, currencyIdTo);
        double countTo;
        if (curse.getCurrencyIdFrom().equals(currencyIdFrom)) {
            countTo = countFrom / curse.getCount();
        } else {
            countTo = countFrom * curse.getCount();
        }

        // сохраняем операцию
        saveTransaction(request.secret_key, currencyIdFrom, currencyIdTo, countFrom);

        // снимаем сумму с кошелька 1-ой валюты
        changeWalletBalance(walletFrom, -countFrom);

        // кладём сумму на кошелёк 2-ой валюты (и создаем его, если до этого не создавался)
        Wallet walletTo = services.wallet.getBySecretKeyAndCurrencyId(request.secret_key, currencyIdTo);
        if (walletTo == null) {
            services.wallet.save(new Wallet(request.secret_key, currencyIdTo, countTo));
            return "{ \"" + request.currency_to + "_wallet\": \"" + countTo + "\"}";
        } else {
            return changeWalletBalance(walletTo, countTo);
        }
    }



    private void saveTransaction(String secretKey, Long currencyFromId, Long currencyToId, Double count) {
        Transaction transaction = new Transaction(new Date(), secretKey, currencyFromId, currencyToId, count);
        services.transaction.save(transaction);
    }

    private String walletsListToString(List<Wallet> wallets) {
        StringBuilder sb = new StringBuilder();

        for (Wallet w: wallets) {
            sb.append(String.format(
                    JsonGenerator.jsonParamFormat,
                    services.currency.getById(w.getCurrencyId()).getName(),
                    w.getCount()
            ));
        }

        sb.deleteCharAt(sb.length() - 1);

        return String.format(JsonGenerator.jsonShellFormat, sb);
    }

    private String changeWalletBalance(Wallet wallet, double count) {
        wallet.setCount(wallet.getCount() + count);
        services.wallet.save(wallet);

        Currency currency = services.currency.getById(wallet.getCurrencyId());
        return JsonGenerator.generateJsonResponse(currency.getName() + "_wallet", wallet.getCount().toString());
    }
}
