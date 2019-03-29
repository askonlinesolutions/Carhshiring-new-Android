package com.carshiring.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakhi on 9/2/2018.
 */

public class SearchData implements Serializable{

            /**
             * status : Available
             * feature : {"aircondition":"true","transmission":"Manual","fueltype":"Petrol","bag":"0","door":"2"}
             * covprice :
             * covcurrency :
             * category : ab
             * model : FORD KA
             * model_code : MBMR
             * image : https://static.carhire-solutions.com/images/car/Alamo/large/t_MBMR_DE.jpg
             * package : SilverPackage
             * price : 332.33
             * currency : SAR
             * time_unit : Day
             * time : 3
             * id_context : 62195346536011513679453762
             * refer_type : 16
             * deposit_currency : EUR
             * deposit_price : 1150
             * deposit_desc : Upon collection of the car a security deposit will be blocked on the driver’s credit card. This deposit is determined by supplier considering your selected car category. The value of one tank of fuel and possible traffic fines can be additionally blocked on a valid credit card (prepaid debit cards, prepaid credit cards and cash cannot be accepted).
             For luxury cars two credit cards in the same name are required for all rentals. The credit card must not be from the same issuer. Please note some suppliers will not accept American Express, Visa Premier or Diners Club credit cards, we strongly recommend to use a Visa or Mastercard. In the event that you fail to produce a valid credit card or have insufficient funds available the car rental agent may refuse to release the vehicle. In this instance no funds will be reimbursed.Estimated deposit amount: EUR 1150
             * deposit_name : Estimated deposit amount
             * supplier : Alamo
             * supplier_city : BERLIN FLUGHAGEN TEGEL
             * supplier_logo : https://static.carhire-solutions.com/images/supplier/logo/logo36.png
             * drop_city : BERLIN FLUGHAGEN TEGEL
             * tc : https://createpdf.carhire-solutions.com/termsandconditions.aspx?reference=62195346536011513679453762&languageId=2
             * rule : Prepayment: Full rental price due at time of reservation. For the local pick up the card holder (DRIVER) must provide a valid credit card. Prepaid or debit cards, such as Maestro, Visa electron, Visa Premier or Carte Bleue are not accepted.
             * coverages : [{"code":"416","name":"Kilometer inclusive: 900 km (0,37 EUR/km)","currency":null,"amount":null,"desc":null,"currency2":"EUR","amount2":"0.37"},{"code":"418","name":"Other taxes and service charges","currency":null,"amount":null,"desc":null,"currency2":"EUR","amount2":"19.43"},{"code":"CDW","name":"Collision damage waiver","currency":"USD","amount":"0.00","desc":"Maximum excess of ab,000 EUR to be paid in the case of damage. Please note that comprehensive insurance does not cover damage to wheels, tires, mirrors and underbody","currency2":null,"amount2":"0.00"},{"code":"SLI","name":"Supplementary Liability Insurance","currency":"USD","amount":"0.00","desc":"Legally required, insurance for damages on the adversarial vehicle, persons and objects- In this offer it is included.","currency2":null,"amount2":"0.00"},{"code":"TP","name":"Theft protection","currency":"USD","amount":"0.00","desc":"In case of theft, there is an excess of ab,000 EUR.  Please note: this does not cover loss of personal items.","currency2":null,"amount2":"0.00"},{"code":"OW","name":"One way rental","currency":"USD","amount":"0.00","desc":"Onewayrental fees are included","currency2":null,"amount2":"0.00"},{"code":"L2L","name":"Fuel Information","currency":"USD","amount":"0.00","desc":"Level to Level: The vehicle should be returned with the same amount of fuel as delivered.","currency2":null,"amount2":"0.00"},{"code":"TAX","name":"VAT","currency":"USD","amount":"0.00","desc":"The rate corresponds with the VAT-rate of the particular country.","currency2":null,"amount2":"0.00"},{"code":"CF","name":"Cancellation fee","currency":null,"amount":null,"desc":null,"currency2":null,"amount2":null}]
             * type : 0
             */

            private String status;
            private FeatureBean feature;
            private String covprice;
            private String covcurrency;
            private String category;                              //
            private String model;
            private String model_code;
            private String image;
            @SerializedName("package")
            private String packageX;
            private String price;
            private String currency;
            private String time_unit;
            private String time;
            private String id_context;
            private String refer_type;
            private String deposit_currency;
            private String deposit_price;
            private String deposit_desc;
            private String deposit_name;
            private String supplier;
            private String supplier_city;
            private String supplier_logo;
            private String drop_city;
            private String tc;
            private String rule;
            private String type;
            private String car_type;
            private List<CoveragesBean> coverages;

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public FeatureBean getFeature() {
                return feature;
            }

            public void setFeature(FeatureBean feature) {
                this.feature = feature;
            }

            public String getCovprice() {
                return covprice;
            }

            public void setCovprice(String covprice) {
                this.covprice = covprice;
            }

            public String getCovcurrency() {
                return covcurrency;
            }

            public void setCovcurrency(String covcurrency) {
                this.covcurrency = covcurrency;
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

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
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

            public String getId_context() {
                return id_context;
            }

            public void setId_context(String id_context) {
                this.id_context = id_context;
            }

            public String getRefer_type() {
                return refer_type;
            }

            public void setRefer_type(String refer_type) {
                this.refer_type = refer_type;
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

            public String getDrop_city() {
                return drop_city;
            }

            public void setDrop_city(String drop_city) {
                this.drop_city = drop_city;
            }

            public String getTc() {
                return tc;
            }

            public void setTc(String tc) {
                this.tc = tc;
            }

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<CoveragesBean> getCoverages() {
                return coverages;
            }

            public void setCoverages(List<CoveragesBean> coverages) {
                this.coverages = coverages;
            }

            public static class FeatureBean {
                /**
                 * aircondition : true
                 * transmission : Manual
                 * fueltype : Petrol
                 * bag : 0
                 * door : 2
                 */

                private String aircondition;
                private String transmission;
                private String fueltype;
                private String bag;
                private String door;
                private String PassengerQuantity;

                public String getPassengerQuantity() {
                    return PassengerQuantity;
                }

                public void setPassengerQuantity(String passengerQuantity) {
                    PassengerQuantity = passengerQuantity;
                }

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

                public String getDoor() {
                    return door;
                }

                public void setDoor(String door) {
                    this.door = door;
                }
            }

            public static class CoveragesBean {
                /**
                 * code : 416
                 * name : Kilometer inclusive: 900 km (0,37 EUR/km)
                 * currency : null
                 * amount : null
                 * desc : null
                 * currency2 : EUR
                 * amount2 : 0.37
                 */

                private String code;
                private String name;
                private Object currency;
                private Object amount;
                private Object desc;
                private String currency2;
                private String amount2;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Object getCurrency() {
                    return currency;
                }

                public void setCurrency(Object currency) {
                    this.currency = currency;
                }

                public Object getAmount() {
                    return amount;
                }

                public void setAmount(Object amount) {
                    this.amount = amount;
                }

                public Object getDesc() {
                    return desc;
                }

                public void setDesc(Object desc) {
                    this.desc = desc;
                }

                public String getCurrency2() {
                    return currency2;
                }

                public void setCurrency2(String currency2) {
                    this.currency2 = currency2;
                }

                public String getAmount2() {
                    return amount2;
                }

                public void setAmount2(String amount2) {
                    this.amount2 = amount2;
                }
            }

}
