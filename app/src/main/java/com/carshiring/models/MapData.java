package com.carshiring.models;

/**
 * Created by Rakhi on 7/24/2018.
 */
public class MapData {

    /**
     * lat : 25.081666666666667
     * lng : 55.139444444444443
     * supplier_name_ar : Avis
     * supplier_logo : https://static.carhire-solutions.com/images/supplier/logo/logo10.png
     * supp_detail : Jumeira Beach Residence
     * supplier_price : 309.80
     */

    private String lat;
    private String lng;
    private String supplier_name_ar;
    private String supplier_logo;
    private String supp_detail;
    private String supplier_price;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getSupplier_name_ar() {
        return supplier_name_ar;
    }

    public void setSupplier_name_ar(String supplier_name_ar) {
        this.supplier_name_ar = supplier_name_ar;
    }

    public String getSupplier_logo() {
        return supplier_logo;
    }

    public void setSupplier_logo(String supplier_logo) {
        this.supplier_logo = supplier_logo;
    }

    public String getSupp_detail() {
        return supp_detail;
    }

    public void setSupp_detail(String supp_detail) {
        this.supp_detail = supp_detail;
    }

    public String getSupplier_price() {
        return supplier_price;
    }

    public void setSupplier_price(String supplier_price) {
        this.supplier_price = supplier_price;
    }
}
