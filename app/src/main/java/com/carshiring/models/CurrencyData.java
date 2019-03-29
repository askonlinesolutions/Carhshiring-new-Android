package com.carshiring.models;

/**
 * Created by Rakhi on 9/5/2018.
 */
public class CurrencyData {
    private String name;
    private String currency;

    public CurrencyData(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
