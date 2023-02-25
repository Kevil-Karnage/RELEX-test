package ru.relex.rozhnovL.controller;

import generator.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.requests.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.User;

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
        services.user.saveUser(newUser);
        return "{ \"secret_key\": \"" + secretKey + "\"}";
    }


    @GetMapping("/curse")
    public String checkCurse(@RequestParam(name = "secret_key") String secretKey,
                             @RequestParam(name = "currency") String curCurrency) {
        Long currencyId = services.currency.getByName(curCurrency).getId();
        List<Curse> curses = services.curse.getByCurrencyId(currencyId);

        return cursesToString(curses, currencyId);
    }



    private String cursesToString(List<Curse> curses, long currencyId) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        for (Curse curse : curses) {
            String currencyName = services.currency.getById(curse.getCurrencyIdTo()).getName();
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
}
