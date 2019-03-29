package com.carshiring.models;

import java.io.Serializable;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class FilterDefaultMultipleListModel  implements Serializable{
    public String Supplier,Features,Packages,Insurances;
    String name;
    boolean checked=false;
    public String getSupplier() {
        return Supplier;
    }

    public void setSupplier(String supplier) {
        Supplier = supplier;
    }

    public String getFeatures() {
        return Features;
    }

    public void setFeatures(String features) {
        Features = features;
    }

    public String getPackages() {
        return Packages;
    }

    public void setPackages(String packages) {
        Packages = packages;
    }

    public String getInsurances() {
        return Insurances;
    }

    public void setInsurances(String insurances) {
        Insurances = insurances;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
