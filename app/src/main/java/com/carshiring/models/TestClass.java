package com.carshiring.models;

import java.util.List;

/**
 * Created by lenovo on 4/7/2018.
 */

public class TestClass {
    List<BookingHistory>bookingHistories;
    List<CancledetailBean>cancledetailBeans;

    public List<BookingHistory> getBookingHistories() {
        return bookingHistories;
    }

    public void setBookingHistories(List<BookingHistory> bookingHistories) {
        this.bookingHistories = bookingHistories;
    }

    public List<CancledetailBean> getCancledetailBeans() {
        return cancledetailBeans;
    }

    public void setCancledetailBeans(List<CancledetailBean> cancledetailBeans) {
        this.cancledetailBeans = cancledetailBeans;
    }
}
