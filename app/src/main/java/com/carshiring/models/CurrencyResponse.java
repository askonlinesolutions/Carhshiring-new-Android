package com.carshiring.models;

/**
 * Created by Rakhi on 9/5/2018.
 */
public class CurrencyResponse {


            /**
             * currency : SAR
             * amount : 462.00
             * conversion_number : 153612953900069213
             * converted_currency : USD
             * converted_amount : 123.20
             * response_message : Success
             */

            private String currency;
            private String amount;
            private String conversion_number;
            private String converted_currency;
            private String converted_amount;
            private String response_message;

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

            public String getConversion_number() {
                return conversion_number;
            }

            public void setConversion_number(String conversion_number) {
                this.conversion_number = conversion_number;
            }

            public String getConverted_currency() {
                return converted_currency;
            }

            public void setConverted_currency(String converted_currency) {
                this.converted_currency = converted_currency;
            }

            public String getConverted_amount() {
                return converted_amount;
            }

            public void setConverted_amount(String converted_amount) {
                this.converted_amount = converted_amount;
            }

            public String getResponse_message() {
                return response_message;
            }

            public void setResponse_message(String response_message) {
                this.response_message = response_message;
            }

}
