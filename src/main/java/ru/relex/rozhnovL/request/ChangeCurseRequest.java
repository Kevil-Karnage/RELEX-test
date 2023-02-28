package ru.relex.rozhnovL.request;

import java.util.Map;

public class ChangeCurseRequest {
    public String secret_key;
    public String base_currency;

    public Map<String, Double> other_currencies;

}
