package com.carshiring.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovo on 3/16/2018.
 */

public class WalletHistoryData {

            /**
             * Booking_id : DT1520936034094
             * booking_date : 2018-03-13
             * Wallet Amount : 234.46
             * Wallet Type : debit
             * Wallet Affetcted Amount : 112.54
             */

            private String Booking_id;
            private String booking_date;
            @SerializedName("Wallet Amount")
            private String _$WalletAmount169; // FIXME check this code
            @SerializedName("Wallet Type")
            private String _$WalletType204; // FIXME check this code
            @SerializedName("Wallet Affetcted Amount")
            private String _$WalletAffetctedAmount6; // FIXME check this code

            public String getBooking_id() {
                return Booking_id;
            }

            public void setBooking_id(String Booking_id) {
                this.Booking_id = Booking_id;
            }

            public String getBooking_date() {
                return booking_date;
            }

            public void setBooking_date(String booking_date) {
                this.booking_date = booking_date;
            }

            public String get_$WalletAmount169() {
                return _$WalletAmount169;
            }

            public void set_$WalletAmount169(String _$WalletAmount169) {
                this._$WalletAmount169 = _$WalletAmount169;
            }

            public String get_$WalletType204() {
                return _$WalletType204;
            }

            public void set_$WalletType204(String _$WalletType204) {
                this._$WalletType204 = _$WalletType204;
            }

            public String get_$WalletAffetctedAmount6() {
                return _$WalletAffetctedAmount6;
            }

            public void set_$WalletAffetctedAmount6(String _$WalletAffetctedAmount6) {
                this._$WalletAffetctedAmount6 = _$WalletAffetctedAmount6;
            }

}
