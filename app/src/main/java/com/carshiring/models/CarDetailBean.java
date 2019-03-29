package com.carshiring.models;

import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class CarDetailBean {

    /**
     * feature : {"aircondition":"false","transmission":"Manual","fueltype":"Petrol","bag":"2","passenger":"4","door":"2"}
     * category : ab
     * model : VW Up
     * model_code : MBMN
     * image : https://static.carhire-solutions.com/images/car/GlobalRentaCar/large/MBMN.jpg
     * price : 531.17
     * currency : SAR
     * time_unit : Day
     * time : 5
     * driver_min_age : 25
     * driver_max_age : 80
     * opening_hours_start : 07:00
     * opening_hours_end : 23:00
     * collision_damage_waiver : ["Maximum excess of 800 EUR to be paid in the case of damage. Please note that comprehensive insurance does not cover damage to wheels, tires, mirrors and underbody"]
     * theft_protection : ["In case of theft, there is an excess of 800 EUR.  Please note: this does not cover loss of personal items."]
     * deposit_currency : EUR
     * deposit_price : 950
     * deposit_desc : Upon collection of the car a security deposit will be blocked on the driver’s credit card. This deposit is determined by supplier considering your selected car category. The value of one tank of fuel and possible traffic fines can be additionally blocked on a valid credit card (prepaid debit cards, prepaid credit cards and cash cannot be accepted).
     For luxury cars two credit cards in the same name are required for all rentals. The credit card must not be from the same issuer. Please note some suppliers will not accept American Express, Visa Premier or Diners Club credit cards, we strongly recommend to use a Visa or Mastercard. In the event that you fail to produce a valid credit card or have insufficient funds available the car rental agent may refuse to release the vehicle. In this instance no funds will be reimbursed.Estimated deposit amount: EUR 950
     * deposit_name : Estimated deposit amount
     * fullprotection_currency : EUR
     * fullprotection_amount : 37.50
     * supplier : Global Rent a Car
     * supplier_city : Car rental Center / Mietwagen center
     * supplier_logo : https://static.carhire-solutions.com/images/supplier/logo/logo51.png
     * drop_city : null
     * tc : https://createpdf.carhire-solutions.com/termsandconditions.aspx?reference=62210311392230109339558058&languageId=2
     * extra : [{"type":"222","name":"Additional Driver","price":"37.50","currency":"EUR"},{"type":"13","name":"GPS (Global Positioning System)","price":"30.00","currency":"EUR"},{"type":"8","name":"Child Seat (ab-3 years)","price":"35.00","currency":"EUR"},{"type":"7","name":"Infant Seat (0-ab year)","price":"35.00","currency":"EUR"},{"type":"10","name":"Snow Chains","price":"100.00","currency":"EUR"},{"type":"413","name":"Full Protection","price":"36.50","currency":"USD"}]
     * coverages : [{"name":"Winter Tyres","currency":"EUR","amount":"0.00","desc":"*WRJ Winterreif./Winter Tyr.pp"},{"name":"Collision damage waiver","currency":"USD","amount":"0.00","desc":"Maximum excess of 800 EUR to be paid in the case of damage. Please note that comprehensive insurance does not cover damage to wheels, tires, mirrors and underbody"},{"name":"Supplementary Liability Insurance","currency":"USD","amount":"0.00","desc":"Legally required, insurance for damages on the adversarial vehicle, persons and objects- In this offer it is included."},{"name":"Theft protection","currency":"USD","amount":"0.00","desc":"In case of theft, there is an excess of 800 EUR.  Please note: this does not cover loss of personal items."},{"name":"Unlimited Mileage","currency":"USD","amount":"0.00","desc":"No further costs apply for extra miles driven"},{"name":"Airport Service Charge","currency":"USD","amount":"0.00","desc":"Some airports charge a service fee- In this offer it is included."},{"name":"One way rental","currency":"USD","amount":"0.00","desc":"possible with additional charges"},{"name":"Fuel Information","currency":"USD","amount":"0.00","desc":"Full to Full: Pick up and drop off with a full tank. If the car is not returned with a full tank, suppliers will charge fuel plus refueling charges. "},{"name":"VAT","currency":"USD","amount":"0.00","desc":"The rate corresponds with the VAT-rate of the particular country."}]
     * cancledetail : {"Coverage":{"@attributes":{"CoverageType":"Cancellation fee","Code":"CF"},"Details":[{"Coverage":{"@attributes":{"CoverageType":"2018-02-16T13:05:42_2018-02-26T10:15:00"}},"Charge":{"@attributes":{"CurrencyCode":"EUR","Amount":"0.00","Description":"Up to 48 hours before pick-up, cancellation free of charge. Within 48 hours prior to pick-up a fee equal to the total rental price with a maximum amount of USD 125 will be charged. In the event of a no-show 100% of the sales price will be charged.","IncludedInRate":"true"}}},{"Coverage":{"@attributes":{"CoverageType":"2018-02-26T10:15:00_2018-02-28T10:15:00"}},"Charge":{"@attributes":{"CurrencyCode":"EUR","Amount":"107.87","Description":"Up to 48 hours before pick-up, cancellation free of charge. Within 48 hours prior to pick-up a fee equal to the total rental price with a maximum amount of USD 125 will be charged. In the event of a no-show 100% of the sales price will be charged."},"Calculation":{"@attributes":{"UnitCharge":"134.8972","UnitName":"PreferedCurrencyPrice: CurrencyCode=USD"}}}}]},"Charge":{"@attributes":{"IncludedInRate":"true"}}}
     * type : 0
     */

    public FeatureBean feature;
    public String category;
    public String model;
    public String model_code;
    public String image;
    public String price;
    public String currency;
    public String time_unit;
    public String time;
    public String driver_min_age;
    public String driver_max_age;
    public String opening_hours_start;
    public String opening_hours_end;
    public String deposit_currency;
    public String deposit_price;
    public String deposit_desc;
    public String deposit_name;
    public String fullprotection_currency;
    public String fullprotection_amount;
    public String supplier;
    public String supplier_city;
    public String supplier_logo;
    public Object drop_city;
    public String tc;
    public CancledetailBean cancledetail;
    public String type;
    public List<String> collision_damage_waiver;
    public List<String> theft_protection;
    public List<ExtraBean> extra;
    public List<CoveragesBean> coverages;

    public static class FeatureBean {
        /**
         * aircondition : false
         * transmission : Manual
         * fueltype : Petrol
         * bag : 2
         * passenger : 4
         * door : 2
         */

        public String aircondition;
        public String transmission;
        public String fueltype;
        public String bag;
        public String passenger;
        public String door;

        public String getAircondition() {
            return aircondition;
        }

        public void setAircondition(String aircondition) {
            this.aircondition = aircondition;
        }

        public String getTransmission() {
            return transmission;
        }

        public void setTransmission(String transmission) {
            this.transmission = transmission;
        }

        public String getFueltype() {
            return fueltype;
        }

        public void setFueltype(String fueltype) {
            this.fueltype = fueltype;
        }

        public String getBag() {
            return bag;
        }

        public void setBag(String bag) {
            this.bag = bag;
        }

        public String getPassenger() {
            return passenger;
        }

        public void setPassenger(String passenger) {
            this.passenger = passenger;
        }

        public String getDoor() {
            return door;
        }

        public void setDoor(String door) {
            this.door = door;
        }
    }


    public FeatureBean getFeature() {
        return feature;
    }

    public void setFeature(FeatureBean feature) {
        this.feature = feature;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel_code() {
        return model_code;
    }

    public void setModel_code(String model_code) {
        this.model_code = model_code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getTime_unit() {
        return time_unit;
    }

    public void setTime_unit(String time_unit) {
        this.time_unit = time_unit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDriver_min_age() {
        return driver_min_age;
    }

    public void setDriver_min_age(String driver_min_age) {
        this.driver_min_age = driver_min_age;
    }

    public String getDriver_max_age() {
        return driver_max_age;
    }

    public void setDriver_max_age(String driver_max_age) {
        this.driver_max_age = driver_max_age;
    }

    public String getOpening_hours_start() {
        return opening_hours_start;
    }

    public void setOpening_hours_start(String opening_hours_start) {
        this.opening_hours_start = opening_hours_start;
    }

    public String getOpening_hours_end() {
        return opening_hours_end;
    }

    public void setOpening_hours_end(String opening_hours_end) {
        this.opening_hours_end = opening_hours_end;
    }

    public String getDeposit_currency() {
        return deposit_currency;
    }

    public void setDeposit_currency(String deposit_currency) {
        this.deposit_currency = deposit_currency;
    }

    public String getDeposit_price() {
        return deposit_price;
    }

    public void setDeposit_price(String deposit_price) {
        this.deposit_price = deposit_price;
    }

    public String getDeposit_desc() {
        return deposit_desc;
    }

    public void setDeposit_desc(String deposit_desc) {
        this.deposit_desc = deposit_desc;
    }

    public String getDeposit_name() {
        return deposit_name;
    }

    public void setDeposit_name(String deposit_name) {
        this.deposit_name = deposit_name;
    }

    public String getFullprotection_currency() {
        return fullprotection_currency;
    }

    public void setFullprotection_currency(String fullprotection_currency) {
        this.fullprotection_currency = fullprotection_currency;
    }

    public String getFullprotection_amount() {
        return fullprotection_amount;
    }

    public void setFullprotection_amount(String fullprotection_amount) {
        this.fullprotection_amount = fullprotection_amount;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplier_city() {
        return supplier_city;
    }

    public void setSupplier_city(String supplier_city) {
        this.supplier_city = supplier_city;
    }

    public String getSupplier_logo() {
        return supplier_logo;
    }

    public void setSupplier_logo(String supplier_logo) {
        this.supplier_logo = supplier_logo;
    }

    public Object getDrop_city() {
        return drop_city;
    }

    public void setDrop_city(Object drop_city) {
        this.drop_city = drop_city;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public CancledetailBean getCancledetail() {
        return cancledetail;
    }

    public void setCancledetail(CancledetailBean cancledetail) {
        this.cancledetail = cancledetail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getCollision_damage_waiver() {
        return collision_damage_waiver;
    }

    public void setCollision_damage_waiver(List<String> collision_damage_waiver) {
        this.collision_damage_waiver = collision_damage_waiver;
    }

    public List<String> getTheft_protection() {
        return theft_protection;
    }

    public void setTheft_protection(List<String> theft_protection) {
        this.theft_protection = theft_protection;
    }

    public List<ExtraBean> getExtra() {
        return extra;
    }

    public void setExtra(List<ExtraBean> extra) {
        this.extra = extra;
    }

    public List<CoveragesBean> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<CoveragesBean> coverages) {
        this.coverages = coverages;
    }
}

