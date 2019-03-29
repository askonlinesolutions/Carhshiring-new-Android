package com.carshiring.models;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class ContactUs {

            /**
             * reservation_cancellation_issues_contact : For reservation & cancellation issues contact us on booking@carshiring.com
             * inquiry : Any general inquiry info@carshiring.com
             * addrees : Riyadh Saudi Arabia
             */

            private String reservation_cancellation_issues_contact;
            private String inquiry;
            private String addrees;

            public String getReservation_cancellation_issues_contact() {
                return reservation_cancellation_issues_contact;
            }

            public void setReservation_cancellation_issues_contact(String reservation_cancellation_issues_contact) {
                this.reservation_cancellation_issues_contact = reservation_cancellation_issues_contact;
            }

            public String getInquiry() {
                return inquiry;
            }

            public void setInquiry(String inquiry) {
                this.inquiry = inquiry;
            }

            public String getAddrees() {
                return addrees;
            }

            public void setAddrees(String addrees) {
                this.addrees = addrees;
            }

}
