package com.carshiring.models;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class ExtraBean {

    /**
     * type : 413
     * name : Full Protection
     * price : 36.50
     * currency : USD
     */

    public String type;
    public String name;
    public String price;
    public String currency;
    public boolean isChecked;
    private String charg_type;
    public String qty;

    public String getCharg_type() {
        return charg_type;
    }

    public void setCharg_type(String charg_type) {
        this.charg_type = charg_type;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
