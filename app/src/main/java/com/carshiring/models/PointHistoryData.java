package com.carshiring.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovo on 3/16/2018.
 */

public class PointHistoryData {

            /**
             * Booking_id : DT1521022631787
             * bokking_date : 2018-03-14
             * Booking Point : 1200.00
             * Booking Point Type : credit
             * Booking Affetcted Point : 1200.00
             */

            private String Booking_id;
            private String bokking_date;
            @SerializedName("Booking Point")
            private String _$BookingPoint18; // FIXME check this code
            @SerializedName("Booking Point Type")
            private String _$BookingPointType184; // FIXME check this code
            @SerializedName("Booking Affetcted Point")
            private String _$BookingAffetctedPoint183; // FIXME check this code

            public String getBooking_id() {
                return Booking_id;
            }

            public void setBooking_id(String Booking_id) {
                this.Booking_id = Booking_id;
            }

            public String getBokking_date() {
                return bokking_date;
            }

            public void setBokking_date(String bokking_date) {
                this.bokking_date = bokking_date;
            }

            public String get_$BookingPoint18() {
                return _$BookingPoint18;
            }

            public void set_$BookingPoint18(String _$BookingPoint18) {
                this._$BookingPoint18 = _$BookingPoint18;
            }

            public String get_$BookingPointType184() {
                return _$BookingPointType184;
            }

            public void set_$BookingPointType184(String _$BookingPointType184) {
                this._$BookingPointType184 = _$BookingPointType184;
            }

            public String get_$BookingAffetctedPoint183() {
                return _$BookingAffetctedPoint183;
            }

            public void set_$BookingAffetctedPoint183(String _$BookingAffetctedPoint183) {
                this._$BookingAffetctedPoint183 = _$BookingAffetctedPoint183;
            }

}
