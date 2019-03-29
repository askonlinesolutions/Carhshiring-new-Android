package com.carshiring.models;

import java.util.List;

public class TestData {

    /**
     * cat_id : 2
     * cat_code : ["3","34"]
     * cat_name : Economic
     * cat_min_price : 387.75
     */

    private String cat_id;
    private String cat_name;
    private String cat_min_price;
    private List<String> cat_code;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_min_price() {
        return cat_min_price;
    }

    public void setCat_min_price(String cat_min_price) {
        this.cat_min_price = cat_min_price;
    }

    public List<String> getCat_code() {
        return cat_code;
    }

    public void setCat_code(List<String> cat_code) {
        this.cat_code = cat_code;
    }
}
