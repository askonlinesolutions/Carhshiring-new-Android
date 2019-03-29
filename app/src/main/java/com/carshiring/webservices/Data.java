package com.carshiring.webservices;

import com.carshiring.models.AboutUs;
import com.carshiring.models.BookingHistory;
import com.carshiring.models.Category;
import com.carshiring.models.ContactUs;
import com.carshiring.models.CurrencyResponse;
import com.carshiring.models.LanguageModel;
import com.carshiring.models.MArkupdata;
import com.carshiring.models.Point;
import com.carshiring.models.PointHistoryData;
import com.carshiring.models.QuotesModel;
import com.carshiring.models.SearchData;
import com.carshiring.models.UseLocationData;
import com.carshiring.models.UserDetails;
import com.carshiring.models.CarDetailBean;
import com.carshiring.models.WalletHistoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class Data {

    public ArrayList<LanguageModel> language_list;
    public UserDetails user_detail;
    public List<SearchData> car_list ;
    public List<Location> location;
    public CarDetailBean car_detail;
    public Point point;
    public MArkupdata markup;
    public List<Category> cat;
    public AboutUs about_us;
    public ContactUs contact_us;
    public List<BookingHistory> booking;
    public String booking_id;
    public List<WalletHistoryData>wallet;
    public List<PointHistoryData>points;
    public CarDetailBean cancel_detail;
    public List<QuotesModel>save_later_list;
    public List<UseLocationData> location2;
    public CurrencyResponse currency;
}
