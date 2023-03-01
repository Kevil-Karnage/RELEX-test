package ru.relex.rozhnovL.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.Curse;
import ru.relex.rozhnovL.request.ChangeCurseRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/curse")
public class CurseController {

    @Autowired
    Services services;

    /**
     * (ALL) Curses Check
     * @param secretKey
     * @param currency
     * @return
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkCurse(@RequestParam(name = "secret_key") String secretKey,
                                        @RequestParam(name = "currency") String currency) {
        Long currencyId = services.currency.getByName(currency).getId();
        List<Curse> curses = services.curse.getByCurrencyId(currencyId);

        return new ResponseEntity<>(cursesToString(curses, currencyId), HttpStatus.OK);
    }

    /**
     * (ADMIN) Curses Change
     * @param json
     * @return
     */
    @PostMapping(value = "/change", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeCurses(@RequestBody String json) {
        ChangeCurseRequest request = parseJsonToChangeCurseRequest(json);
        if (request == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!services.user.getBySecretKey(request.secret_key).isAdmin())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        List<Curse> curses = new ArrayList<>();
        Long baseCurrencyId = services.currency.getByName(request.base_currency).getId();

        for (String key: request.other_currencies.keySet()) {
            Long currencyId = services.currency.getByName(key).getId();
            Curse curse = services.curse.getByCurrenciesIds(baseCurrencyId, currencyId);

            double count = checkCorrectCurseCount(curse, baseCurrencyId, request.other_currencies.get(key));
            curse.setCount(count);

            services.curse.save(curse);
            curses.add(curse);
        }

        return new ResponseEntity<>(cursesToString(curses, baseCurrencyId), HttpStatus.OK);
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

    private Map<String, String> cursesToString(List<Curse> curses, long currencyId) {
        Map<String, String> map = new HashMap<>();

        for (Curse curse : curses) {
            String currencyName;
            Double count;

            if (curse.getCurrencyIdFrom().equals(currencyId)) {
                currencyName = services.currency.getById(curse.getCurrencyIdTo()).getName();
                count = curse.getCount();
            } else {
                currencyName = services.currency.getById(curse.getCurrencyIdFrom()).getName();
                count = 1 / curse.getCount();
            }

            map.put(currencyName, "" + String.format(services.doubleFormat, count));
        }

        return map;
    }


    private Double checkCorrectCurseCount(Curse curse, long currencyId, double count) {
        if (curse.getCurrencyIdFrom().equals(currencyId))
            return count;

        return 1.0 / count;
    }
}
