package ru.relex.rozhnovL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.entity.Wallet;
import ru.relex.rozhnovL.requests.ExchangeCurrencyRequest;
import ru.relex.rozhnovL.requests.TopUpRequest;
import ru.relex.rozhnovL.requests.WithdrawCardRequest;
import ru.relex.rozhnovL.requests.WithdrawWalletRequest;
import ru.relex.rozhnovL.responses.BadResponse;

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

        return walletsToString(wallets);
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

        return changeWalletBalance(wallet, Double.parseDouble(request.RUB_wallet));
    }

    /**
     * (USER) Balance Withdraw Card
     * @param request
     * @return
     */
    @PostMapping("/withdraw/card")
    public String withdraw(@RequestBody WithdrawCardRequest request) {
        System.out.println("Снятие средств на карту");

        Wallet wallet = services.wallet.getMainBySecretKey(request.secret_key);
        double count = Double.parseDouble(request.count);

        if (wallet.getCount() < count) {
            return "{ \"response\": \"not enough money\"}";
        }

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
            return new BadResponse("not enough money on wallet").toString();
        }

        // переводим 1-ую валюту во 2-ую
        Curse curse = services.curse.getByCurrenciesIds(currencyIdFrom, currencyIdTo);
        double countTo;
        if (curse.getCurrencyIdFrom().equals(currencyIdFrom)) {
            countTo = countFrom / curse.getCount();
        } else {
            countTo = countFrom * curse.getCount();
        }

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



    private String walletsToString(List<Wallet> wallets) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append('\n');
        for (int i = 0; i < wallets.size(); i++) {
            Currency currency = services.currency.getById(wallets.get(i).getCurrencyId());
            sb.append('"');
            sb.append(currency.getName());
            sb.append("_wallet\": ");
            sb.append('"');
            sb.append(wallets.get(i).getCount());
            sb.append('"');
            sb.append('\n');
        }
        sb.append('}');

        return sb.toString();
    }

    private String changeWalletBalance(Wallet wallet, double count) {
        wallet.setCount(wallet.getCount() + count);
        services.wallet.save(wallet);

        Currency currency = services.currency.getById(wallet.getCurrencyId());
        return "{ \"" + currency.getName() + "_wallet\": \"" + wallet.getCount() + "\"}";
    }
}
