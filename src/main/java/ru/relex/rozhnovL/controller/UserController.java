package ru.relex.rozhnovL.controller;

import generator.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.requests.RegistrationRequest;
import ru.relex.rozhnovL.requests.topUpRequest;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Currency;
import ru.relex.rozhnovL.entity.User;
import ru.relex.rozhnovL.entity.Wallet;
import ru.relex.rozhnovL.requests.withdrawCardRequest;
import ru.relex.rozhnovL.requests.withdrawWalletRequest;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    Services services;


    /**
     * (USER) Sign Up
     * @param request
     * @return
     */
    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public String signUp(@RequestBody RegistrationRequest request) {
        System.out.println("username: " + request.username + " email: " + request.email);
        String secretKey = Generator.generateSecretKey(request.username, request.email);

        User newUser = new User(request.username, request.email, secretKey, false);
        services.userService.saveUser(newUser);
        return "{ \"secret_key\": \"" + secretKey + "\"}";
    }

    /**
     * (USER) Balance Check
     * @param secretKey
     * @return
     */
    @GetMapping("/balance")
    public String getBalance(@RequestParam(name = "secretKey") String secretKey) {
        List<Wallet> wallets = services.walletService.getAllBySecretKey(secretKey);

        return walletsToString(wallets);
    }

    /**
     * (USER) Balance Top Up
     * @param request
     * @return
     */
    @PostMapping("/balance/top-up")
    public String topUp(@RequestBody topUpRequest request) {
        System.out.println(request);
        Wallet wallet = services.walletService.getMainBySecretKey(request.secret_key);

        return changeWalletBalance(wallet, Double.parseDouble(request.RUB_wallet));
    }

    /**
     * (USER) Balance Withdraw Card
     * @param request
     * @return
     */
    @PostMapping("/balance/withdraw/card")
    public String withdraw(@RequestBody withdrawCardRequest request) {
        System.out.println("Снятие средств на карту");

        Wallet wallet = services.walletService.getMainBySecretKey(request.secret_key);
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
    @PostMapping("/balance/withdraw/wallet")
    public String withdraw(@RequestBody withdrawWalletRequest request) {
        System.out.println("Снятие средств на кошелёк");
        Long currencyId = services.currencyService.getByName(request.currency).getId();

        Wallet wallet =
                services.walletService.getBySecretKeyAndCurrencyId(request.secret_key, currencyId);
        double count = Double.parseDouble(request.count);

        if (wallet.getCount() < count) {
            return "{ \"response\": \"not enough money\"}";
        }

        return changeWalletBalance(wallet, -count);
    }


    @GetMapping("/curse")
    public String checkCurse(@RequestParam(name = "secret_key") String secretKey,
                             @RequestParam(name = "currency") String curCurrency) {
        Long currencyId = services.currencyService.getByName(curCurrency).getId();
        List<Curse> curses = services.curseService.getByCurrencyId(currencyId);

        return cursesToString(curses, currencyId);
    }



    private String cursesToString(List<Curse> curses, long currencyId) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        for (Curse curse : curses) {
            String currencyName = services.currencyService.getById(curse.getCurrencyIdTo()).getName();
            sb.append('"');
            sb.append(currencyName);

            sb.append("\": \"");
            if (curse.getCurrencyIdFrom().equals(currencyId)) {
                sb.append(curse.getCount());
            } else {
                double count = 1.0 / curse.getCount();
                sb.append((float)((int)(count * 10000) / 10000));
            }
            sb.append("\",\n");
        }
        sb.append('}');
        return sb.toString();
    }
    private String walletsToString(List<Wallet> wallets) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append('\n');
        for (int i = 0; i < wallets.size(); i++) {
            Currency currency = services.currencyService.getById(wallets.get(i).getCurrencyId());
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
        services.walletService.saveWallet(wallet);

        Currency currency = services.currencyService.getById(wallet.getCurrencyId());
        return "{ \"" + currency.getName() + "_wallet\": \"" + wallet.getCount() + "\"}";
    }
}
