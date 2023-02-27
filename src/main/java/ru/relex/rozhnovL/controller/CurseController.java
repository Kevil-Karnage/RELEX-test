package ru.relex.rozhnovL.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.requests.ChangeCurseRequest;
import ru.relex.rozhnovL.responses.BadResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CurseController {

    @Autowired
    Services services;

    /**
     * (ALL) Check Curses
     * @param secretKey
     * @param currency
     * @return
     */
    @GetMapping("/curse")
    public String checkCurse(@RequestParam(name = "secret_key") String secretKey,
                             @RequestParam(name = "currency") String currency) {
        Long currencyId = services.currency.getByName(currency).getId();
        List<Curse> curses = services.curse.getByCurrencyId(currencyId);

        return cursesToString(curses, currencyId);
    }

    @PostMapping("/curse/change")
    public String changeCurses(@RequestBody String json) {
        ChangeCurseRequest request = parseJsonToChangeCurseRequest(json);
        if (request == null) return "{\"response\": \"bad request\"}";

        if (!services.user.getBySecretKey(request.secret_key).isAdmin()) {
            return new BadResponse("Access denied").toString();
        }

        List<Curse> curses = new ArrayList<>();
        Long baseCurrencyId = services.currency.getByName(request.base_currency).getId();
        for (String key: request.other_currencies.keySet()) {
            Long currencyId = services.currency.getByName(key).getId();
            Curse curse = services.curse.getByCurrenciesIds(baseCurrencyId, currencyId);

            double count = checkCorrectCurseCount(curse, baseCurrencyId);
            curse.setCount(count);

            services.curse.save(curse);
            curses.add(curse);
        }

        return cursesToString(curses, baseCurrencyId);
    }


    private ChangeCurseRequest parseJsonToChangeCurseRequest(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map;
        try {
            map = objectMapper.readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            return null;
        }


        ChangeCurseRequest request = new ChangeCurseRequest();
        request.secret_key = map.remove("secret_key");
        request.base_currency = map.remove("base_currency");

        request.other_currencies = new HashMap<>();
        for (String key : map.keySet()) {
            request.other_currencies.put(key, Double.parseDouble(map.get(key)));
        }

        return request;
    }

    private String cursesToString(List<Curse> curses, long currencyId) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        for (Curse curse : curses) {
            long otherCurrencyId;
            if (curse.getCurrencyIdTo().equals(currencyId))
                otherCurrencyId = curse.getCurrencyIdFrom();
            else
                otherCurrencyId = curse.getCurrencyIdTo();


            sb.append('"');
            sb.append(services.currency.getById(otherCurrencyId).getName());
            sb.append("\": \"");
            sb.append(checkCorrectCurseCount(curse, currencyId));
            sb.append("\",\n");
        }
        sb.append('}');
        return sb.toString();
    }

    private Double checkCorrectCurseCount(Curse curse, long currencyId) {
        if (curse.getCurrencyIdFrom().equals(currencyId))
            return curse.getCount();

        return 1.0 / curse.getCount();
    }
}
