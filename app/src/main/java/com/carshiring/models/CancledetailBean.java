package com.carshiring.models;

/**
 * Created by Rakhi.
 * Contact Number : +91 9958187463
 */
public class CancledetailBean {

    /**
     * error_code : 101
     * response : {"cancel_detail":{"booking_cancel_booking_id":"CH1523082225355","booking_cancel_booking_amount":"426.53","booking_cancel_cancel_charge":"853.06","booking_cancel_refundable_amount":0,"booking_cancel_credit_amount":"0.00","booking_cancel_wallet_amount":"0.00","booking_cancel_point_amount":"0.00"}}
     * status : true
     * msg : User Profile Details successfully
     */

    private String error_code;
    private ResponseBean response;
    private boolean status;
    private String msg;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ResponseBean {
        /**
         * cancel_detail : {"booking_cancel_booking_id":"CH1523082225355","booking_cancel_booking_amount":"426.53","booking_cancel_cancel_charge":"853.06","booking_cancel_refundable_amount":0,"booking_cancel_credit_amount":"0.00","booking_cancel_wallet_amount":"0.00","booking_cancel_point_amount":"0.00"}
         */

        private CancelDetailBean cancel_detail;

        public CancelDetailBean getCancel_detail() {
            return cancel_detail;
        }

        public void setCancel_detail(CancelDetailBean cancel_detail) {
            this.cancel_detail = cancel_detail;
        }

        public static class CancelDetailBean {
            /**
             * booking_cancel_booking_id : CH1523082225355
             * booking_cancel_booking_amount : 426.53
             * booking_cancel_cancel_charge : 853.06
             * booking_cancel_refundable_amount : 0
             * booking_cancel_credit_amount : 0.00
             * booking_cancel_wallet_amount : 0.00
             * booking_cancel_point_amount : 0.00
             */

            private String booking_cancel_booking_id;
            private String booking_cancel_booking_amount;
            private String booking_cancel_cancel_charge;
            private String booking_cancel_refundable_amount;
            private String booking_cancel_credit_amount;
            private String booking_cancel_wallet_amount;
            private String booking_cancel_point_amount;

            public String getBooking_cancel_booking_id() {
                return booking_cancel_booking_id;
            }

            public void setBooking_cancel_booking_id(String booking_cancel_booking_id) {
                this.booking_cancel_booking_id = booking_cancel_booking_id;
            }

            public String getBooking_cancel_booking_amount() {
                return booking_cancel_booking_amount;
            }

            public void setBooking_cancel_booking_amount(String booking_cancel_booking_amount) {
                this.booking_cancel_booking_amount = booking_cancel_booking_amount;
            }

            public String getBooking_cancel_cancel_charge() {
                return booking_cancel_cancel_charge;
            }

            public void setBooking_cancel_cancel_charge(String booking_cancel_cancel_charge) {
                this.booking_cancel_cancel_charge = booking_cancel_cancel_charge;
            }

            public String getBooking_cancel_refundable_amount() {
                return booking_cancel_refundable_amount;
            }

            public void setBooking_cancel_refundable_amount(String booking_cancel_refundable_amount) {
                this.booking_cancel_refundable_amount = booking_cancel_refundable_amount;
            }

            public String getBooking_cancel_credit_amount() {
                return booking_cancel_credit_amount;
            }

            public void setBooking_cancel_credit_amount(String booking_cancel_credit_amount) {
                this.booking_cancel_credit_amount = booking_cancel_credit_amount;
            }

            public String getBooking_cancel_wallet_amount() {
                return booking_cancel_wallet_amount;
            }

            public void setBooking_cancel_wallet_amount(String booking_cancel_wallet_amount) {
                this.booking_cancel_wallet_amount = booking_cancel_wallet_amount;
            }

            public String getBooking_cancel_point_amount() {
                return booking_cancel_point_amount;
            }

            public void setBooking_cancel_point_amount(String booking_cancel_point_amount) {
                this.booking_cancel_point_amount = booking_cancel_point_amount;
            }
        }
    }
}
