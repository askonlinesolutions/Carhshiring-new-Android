package com.carshiring.webservices;

import java.io.Serializable;

/**
 * Created by rakhi on 09/02/2018.
 */

public class Location implements Serializable{

    /**
     * city_name : Dubai Al Maktoum International Airport (DWC)
     * city_id : 4
     * code : 6493
     * iata : DWC
     * country_code : AE
     * type : a
     */

    public String city_name;
    public String city_id;
    public String code;
    public String iata;
    public String country_code;
    public String type;
    public String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
