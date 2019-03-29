package com.carshiring.models;

import java.util.List;

/**
 * Created by lenovo on 3/29/2018.
 */

public class DiscountData {


    /**
     * message : success
     * response : {"discountcoupon":{"offer_data":[{"offers_id":"3","offers_supplier":"1","offers_fromdate":"2018-03-23","offers_todate":"2018-03-31","offers_code":"f680","offers_value":"80","offers_type":"2","offers_status":"1"}]}}
     * status : true
     */

    private String message;
    private ResponseBean response;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class ResponseBean {
        /**
         * discountcoupon : {"offer_data":[{"offers_id":"3","offers_supplier":"1","offers_fromdate":"2018-03-23","offers_todate":"2018-03-31","offers_code":"f680","offers_value":"80","offers_type":"2","offers_status":"1"}]}
         */

        private DiscountcouponBean discountcoupon;

        public DiscountcouponBean getDiscountcoupon() {
            return discountcoupon;
        }

        public void setDiscountcoupon(DiscountcouponBean discountcoupon) {
            this.discountcoupon = discountcoupon;
        }

        public static class DiscountcouponBean {
            private List<OfferDataBean> offer_data;

            public List<OfferDataBean> getOffer_data() {
                return offer_data;
            }

            public void setOffer_data(List<OfferDataBean> offer_data) {
                this.offer_data = offer_data;
            }

            public static class OfferDataBean {
                /**
                 * offers_id : 3
                 * offers_supplier : 1
                 * offers_fromdate : 2018-03-23
                 * offers_todate : 2018-03-31
                 * offers_code : f680
                 * offers_value : 80
                 * offers_type : 2
                 * offers_status : 1
                 */

                private String offers_id;
                private String offers_supplier;
                private String offers_fromdate;
                private String offers_todate;
                private String offers_code;
                private String offers_value;
                private String offers_type;
                private String offers_status;

                public String getOffers_id() {
                    return offers_id;
                }

                public void setOffers_id(String offers_id) {
                    this.offers_id = offers_id;
                }

                public String getOffers_supplier() {
                    return offers_supplier;
                }

                public void setOffers_supplier(String offers_supplier) {
                    this.offers_supplier = offers_supplier;
                }

                public String getOffers_fromdate() {
                    return offers_fromdate;
                }

                public void setOffers_fromdate(String offers_fromdate) {
                    this.offers_fromdate = offers_fromdate;
                }

                public String getOffers_todate() {
                    return offers_todate;
                }

                public void setOffers_todate(String offers_todate) {
                    this.offers_todate = offers_todate;
                }

                public String getOffers_code() {
                    return offers_code;
                }

                public void setOffers_code(String offers_code) {
                    this.offers_code = offers_code;
                }

                public String getOffers_value() {
                    return offers_value;
                }

                public void setOffers_value(String offers_value) {
                    this.offers_value = offers_value;
                }

                public String getOffers_type() {
                    return offers_type;
                }

                public void setOffers_type(String offers_type) {
                    this.offers_type = offers_type;
                }

                public String getOffers_status() {
                    return offers_status;
                }

                public void setOffers_status(String offers_status) {
                    this.offers_status = offers_status;
                }
            }
        }
    }
}
