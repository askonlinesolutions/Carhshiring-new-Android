package com.carshiring.models;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class CoveragesBean {

    /**
     * name : Location service charge
     * currency : USD
     * amount : 0.00
     * desc : An additional service charge applies at some airports, railway stations and ports- In this offer it is included.
     */

    public String name;
    public String currency;
    public String amount;
    public String desc;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
