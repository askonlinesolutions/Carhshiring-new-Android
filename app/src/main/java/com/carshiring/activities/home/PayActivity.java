package com.carshiring.activities.home;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.mainsetup.ForgotPasswordActivity;
import com.carshiring.activities.mainsetup.SignUpActivity;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.BookingRequest;
import com.carshiring.models.CouponRequest;
import com.carshiring.models.DiscountData;
import com.carshiring.models.PointHistoryData;
import com.carshiring.models.TokenResponse;
import com.carshiring.models.UserDetails;
import com.carshiring.models.WalletHistoryData;
import com.carshiring.splash.SplashActivity;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.payfort.sdk.android.dependancies.base.FortInterfaces;
import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

import static com.carshiring.activities.home.BookCarActivity.extraData;
import static com.carshiring.activities.home.MainActivity.getKeyFromValue;
import static com.carshiring.fragments.SearchCarFragment.driver_age;

public class PayActivity extends AppBaseActivity {
    ActionBar actionBar;

    private TextView txtPay, txtTotalAmyVal,txtDob,txtPAyAmt,txtWalletBal,txtPointValueAmt, txtWalletValueAmt,
            txtCoupanValue,txtPointVal,txtFullProAmt, txtEarnedPoint, txtApply, txtExtraCharge;
    CheckBox txtcheckPoint,txtCheckPay, txtCheckWallet;
    public String price="", email="",sdk_token="";
    public FortCallBackManager fortCallback;

/*NCszWushUPEjueRWnLti
new access code
Merchant Identifier: daouwTJI
*/

// for live mode
/*

    final String ACCESS_TOKEN =  "5JCpZWu9J5Hlz3IoK1KN"; // new live
 //final String ACCESS_TOKEN =  "NCszWushUPEjueRWnLti"; // old live
    final String MERCHANT_IDENTIFIER = "daouwTJI";
    final String REQUEST_PHRASE = "1985" ;
*/

//  for test mode
    final String ACCESS_TOKEN =  "qa2s6awTpBNc04Q65T8v";
    final String MERCHANT_IDENTIFIER =  "GjitDYjm";
    final String REQUEST_PHRASE = "PASS" ;

    private LinearLayout fullProtectionLayout;
    private String name="",sarname="",set="",number="",address="",city="",zipcode="",countrycode="",car_id="",
            type="",rtype="",coupoun="",acName,time_zone,
            fullprotection="",flight_no="",dob="",user_id="",pick_date="",
            drop_date="", pick_city="",drop_city="",protection_val="",booking_point="",booking_wallet="",
            booking_payfort="",transaction_id="",merchant_reference="",language="", earnPoint,curen;
    private TinyDB tinyDB;
    private List<WalletHistoryData> walletHistoryData = new ArrayList<>();
    private List<PointHistoryData>pointHistoryData = new ArrayList<>();
    private double creditAmt, debitAmt,walletAmt, totalDebit, totalPrice,totalCredit,totalPoint,pointValue,totalDebitPoint,
            totalCreditPoint, creditPoint,payfortAmt1, debitPoint,couponvalue, walBal,payfortAmt, discountvalue,pointBal,
            usepoint, useWallet;
    private EditText edtCoupon;
    private Gson gson = new Gson();
    boolean isCouponApplied;
    UserDetails userDetails = new UserDetails();
    String TAG = "PAY";
    BookingRequest bookingRequest = new BookingRequest();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        time_zone = TimeZone.getDefault().getID()+"";

        actionBar = getSupportActionBar() ;
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        if (getIntent().hasExtra("activity")){
            acName = getIntent().getStringExtra("activity");
        }

        initUI();

        initVariable();

        setListener();

        callMethod();
    }

    private void callMethod() {

        getPoint(user_id);
        getWal(user_id);
    }

    private void setListener() {
        txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coupoun = edtCoupon.getText().toString().trim();
                if (!coupoun.isEmpty()){
                    txtWalletValueAmt.setVisibility(View.GONE);
                    if (fullprotection.equalsIgnoreCase("yes")){
                        if (price!=null){
                            totalPrice = Double.parseDouble(price);
                            totalPrice = Double.parseDouble(price)+Double.parseDouble(protection_val);
                        }
                    } else {
                        if (price!=null){
                            totalPrice = Double.parseDouble(price);
                        }
                    }
                    validateCoupon(coupoun);
                    txtCheckPay.setChecked(false);
                    txtCheckWallet.setChecked(false);
                    txtcheckPoint.setChecked(false);
                } else {
                    Utility.message(getApplication(), getResources().getString(R.string.enterCoupn));
                }
            }
        });

        txtPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TinyDB tinyDB1 = new TinyDB(getApplicationContext());
                if (tinyDB1.contains("login_data")){
                    String data = tinyDB1.getString("login_data");
                    userDetails = gson.fromJson(data, UserDetails.class);
                    if (userDetails.getUser_id()!=null){
                        user_id = userDetails.getUser_id();
                    }
                }
                if (tinyDB1.contains("login_data")) {
                    if (userDetails!=null&&userDetails.getUser_lname() == null || userDetails.getUser_lname().length() == 0) {
                        set = "update_profile";
                        setupoverlay(set);
                    }
                    else {
                        acName = "";
                        if (userDetails!=null&&userDetails.getUser_id()!=null){
                            user_id = userDetails.getUser_id();
                            name = userDetails.getUser_name();
                            sarname = (String) userDetails.getUser_lname();
                            number = userDetails.getUser_phone();
                            email = userDetails.getUser_email();
                            address = userDetails.getUser_address();
                            city = (String) userDetails.getUser_city();
                            zipcode = userDetails.getUser_zipcode();
                            countrycode = (String) userDetails.getUser_country();
                            dob = userDetails.getUser_dob();
                            user_age = userDetails.getUser_age();
                        }
                      /*  if (isBetweenDriverAge==1){

                        }*/

                        if (user_age!=null ){
                            if (tinyDB1.getBoolean("isSkipLogin")){
                                if (tinyDB1.getString("dAge")!=null && tinyDB1.getString("dAge").length()==0){
                                    final Calendar c = Calendar.getInstance();
                                    int mYear = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(driver_age);
                                    int mMonth = c.get(Calendar.MONTH);
                                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                                    dob = mYear+ "-" + (mMonth + 1)  + "-" + mDay;
                                    pay();
                                } else if (user_age.equals(driver_age)){
                                    dob = dateFormat(dob);
                                    pay();
                                } else {
                                    ageDialog();
                                }
                            } else {
                                if (tinyDB1.getString("dAge")!=null && tinyDB1.getString("dAge").length()==0){
                                    final Calendar c = Calendar.getInstance();
                                   /* int mYear = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(driver_age);
                                    int mMonth = c.get(Calendar.MONTH);
                                    int mDay = c.get(Calendar.DAY_OF_MONTH);*/
                                    /*dob= dateFormat(dob);*/
                                    pay();
                                }
                                else {
                                    if (user_age.equals(driver_age)){
                                        dob = dateFormat(dob);
                                        pay();
                                    } else {
                                        ageDialog();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    set = "login";
                    setupoverlay(set);
                }
            }
        });

    }

    private String dateFormat(String start_dt){
        DateFormat formatter = new SimpleDateFormat("MM-DD-yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(start_dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-DD");
        String finalString;
        finalString = newFormat.format(date);
        return finalString;
    }

    @SuppressLint("SetTextI18n")
    private void initVariable() {
        appGlobal.context = getApplicationContext();
        dialog = new Dialog(PayActivity.this);

        tinyDB = new TinyDB(getApplicationContext());
        if (tinyDB.contains("login_data")){
            String data = tinyDB.getString("login_data");
            userDetails = gson.fromJson(data, UserDetails.class);
            MainActivity.logout.setTitle(getResources().getString(R.string.logout));
            if (userDetails.getUser_id()!=null){
                user_id = userDetails.getUser_id();
            }
        }
        language=tinyDB.getString("language_code");
        curen = tinyDB.getString("from_currency");
        flight_no = BookCarActivity.flight_no;
        car_id = CarsResultListActivity.id_context;
        type = CarsResultListActivity.type;
        rtype = CarsResultListActivity.refertype;
        pick_city = SearchCarFragment.pickName;
        pick_date = SearchCarFragment.pick_date;
        drop_city = SearchCarFragment.dropName;
        drop_date = SearchCarFragment.drop_date;
        fullprotection = BookCarActivity.fullProtection;
        if (fullprotection.equalsIgnoreCase("yes")){
            protection_val =String.valueOf( CarDetailActivity.fullAmtValue);
        }
        if (fullprotection.equalsIgnoreCase("yes")){
            fullProtectionLayout.setVisibility(View.GONE);//should be visible
            txtFullProAmt.setText(curen +Utility.convertCuurency(CarDetailActivity.fullAmtValue, getApplicationContext()));
        } else {
            fullProtectionLayout.setVisibility(View.GONE);
        }
        if (Extras.totalExtra>0 && Extras.extraData != null && Extras.extraData.size()>0){
            txtExtraCharge.setText("Selected Extras charge payable at the time of pickup : "+curen
                    + Utility.convertCuurency(Extras.totalExtra,getApplicationContext()));
            txtExtraCharge.setVisibility(View.VISIBLE);
        }
        price = CarDetailActivity.carPrice;
        fullprotection="no";//for testing
        if (fullprotection.equalsIgnoreCase("yes")){
            if (price!=null){
                totalPrice = Double.parseDouble(price);
                totalPrice = Double.parseDouble(price)+Double.parseDouble(protection_val);
            }
        } else {
            if (price!=null){
                totalPrice = Double.parseDouble(price);
            }
        }
        txtTotalAmyVal.setText(curen + Utility.convertCuurency(Double.parseDouble(CarDetailActivity.carPrice),
                getApplicationContext())+ "/ "
                +CarsResultListActivity.day + " "+ CarsResultListActivity.time);
        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                +Utility.convertCuurency(totalPrice, getApplicationContext()));

        earnPoint = String.valueOf(CarDetailActivity.point);
        txtEarnedPoint.setText(getResources().getString(R.string.colletcted_point )+" "+ earnPoint );
    }

    private void initUI() {
        fullProtectionLayout = findViewById(R.id.activity_pay_full_pro_layout);
        edtCoupon = findViewById(R.id.activity_pay_edtCoupon);
        txtEarnedPoint = findViewById(R.id.txtEarnedPoint);
        txtApply = findViewById(R.id.activity_pay_btnApply);
        txtCoupanValue = findViewById(R.id.activity_pay_txtCouponValue);
        txtPointValueAmt = findViewById(R.id.txtPointValueAmt);
        txtWalletValueAmt = findViewById(R.id.txtWaletValueAmt);
        txtFullProAmt = findViewById(R.id.activity_pay_txtFullProtectionAmtValue);
        txtPAyAmt = findViewById(R.id.activity_pay_txtPayAmt);
        txtCheckPay = findViewById(R.id.check_pay_online);
        txtcheckPoint = findViewById(R.id.check_points);
        txtCheckWallet = findViewById(R.id.check_wallet);
        txtWalletBal = findViewById(R.id.txtWaletValue);
        txtPointVal = findViewById(R.id.txtPointValue);
        txtPay = findViewById(R.id.txtPay);
        txtTotalAmyVal = findViewById(R.id.txtTotalPayValue);
        txtExtraCharge = findViewById(R.id.activity_pay_txtExtra);


    }

    private void pay(){
/*
        if (tinyDB.contains("login_data")){
            String log = tinyDB.getString("login_data");
            UserDetails userDetails = new UserDetails();
            userDetails = gson.fromJson(log,UserDetails.class);

            if (userDetails.getUser_age()!=null){
                String age = userDetails.getUser_age();
                if (age.equals("0")){
                    age = "30";
                }
                if (age.equals(SearchCarFragment.driver_age)){
                } else {
                    Utility.message(getApplicationContext(),"Searched age is not matched your date of birth.Please search with same age or update your profile");
                }
            }
        }
*/
        if (isCouponApplied){
            totalPrice = discountedPrice;
            if ((txtcheckPoint.isChecked()&& txtCheckWallet.isChecked())
                    ||(txtcheckPoint.isChecked()&& txtCheckWallet.isChecked()&&txtCheckPay.isChecked())){
                double d ;
                if (totalPrice>pointValue){
                    d = totalPrice - pointValue;
                    if(d > walletAmt){
                        d = d - walletAmt;
                        payfortAmt1 = d;
                        booking_payfort = String.valueOf(payfortAmt1);
                        booking_wallet = String.valueOf(walletAmt);
                        booking_point = String.valueOf(pointValue);
                        requestPurchase(booking_payfort);
                    }else{
                        booking_wallet = String.valueOf(d);
                        booking_point= String.valueOf(pointValue);
                        booking_payfort = String.valueOf(payfortAmt1);
                        String a = gson.toJson(setBooking());
                        bookCar(a);
//                                makeBooking(a);
                    }

                } else {
                    booking_point=String.valueOf(totalPrice);
                    booking_payfort = String.valueOf(payfortAmt1);
                    booking_wallet="";
                    String a = gson.toJson(setBooking());
                    bookCar(a);
//                            makeBooking(a);
                }
            }
            else if (txtcheckPoint.isChecked()){
                usepoint = pointValue;
                if (usepoint>=totalPrice){
                    booking_point=String.valueOf(totalPrice);
                    booking_payfort = String.valueOf(payfortAmt1);
                    booking_wallet="";
                    String s = gson.toJson(setBooking());
//                            makeBooking(s);
                    bookCar(s);
                } else {
                    booking_wallet="";
                    payfortAmt1 = totalPrice-usepoint;
                    booking_point = String.valueOf(usepoint);
                    booking_payfort = String.valueOf(payfortAmt1);
                    requestPurchase(booking_payfort);
                }
            } else if (txtCheckWallet.isChecked()){
                useWallet = walletAmt;
                if (useWallet>=totalPrice){
                    booking_wallet = String.valueOf(totalPrice);
                    booking_point= "";
                    String s = gson.toJson(setBooking());
//                            makeBooking(s);
                    bookCar(s);
                } else {
                    payfortAmt = totalPrice-useWallet;
                    booking_point="";
                    booking_wallet = String.valueOf(useWallet);
                    booking_payfort = String.valueOf(payfortAmt);
                    requestPurchase(booking_payfort);
                }
            } else {
                booking_payfort = String.valueOf(totalPrice);
                booking_point="";
                booking_wallet="";
                String s = gson.toJson(setBooking());
                Log.d(TAG, "onClick: booking wal"+s);
                if (totalPrice>0){
                    requestPurchase(booking_payfort);
                } else {
//                            makeBooking(s);
                    bookCar(s);
                }
            }
        }

//                if coupon not applied

        else {
            if ((txtcheckPoint.isChecked()&& txtCheckWallet.isChecked())
                    ||(txtcheckPoint.isChecked()&& txtCheckWallet.isChecked()&&txtCheckPay.isChecked())){
                double d ;
                if (totalPrice>pointValue){

                    d = totalPrice - pointValue;

                    if(d > walletAmt){
                        d = d - walletAmt;
                        booking_payfort = String.valueOf(d);
                        booking_wallet = String.valueOf(walletAmt);
                        booking_point = String.valueOf(pointValue);
                        requestPurchase(booking_payfort);

                    }else{
                        booking_wallet = String.valueOf(d);
                        booking_point= String.valueOf(pointValue);
                        String a = gson.toJson(setBooking());
//                                makeBooking(a);
                        bookCar(a);
                    }

                } else {
                    booking_point=String.valueOf(totalPrice);
                    booking_payfort="";
                    booking_wallet="";
                    String a = gson.toJson(setBooking());
//                            makeBooking(a);
                    bookCar(a);
                }
            }
            else if (txtcheckPoint.isChecked()){
                usepoint = pointValue;
                if (usepoint>=totalPrice){
                    booking_point=String.valueOf(totalPrice);
                    booking_payfort="";
                    booking_wallet="";
                    String s = gson.toJson(setBooking());
//                            makeBooking(s);
                    bookCar(s);
                } else {
                    booking_wallet="";
                    payfortAmt = totalPrice-usepoint;
                    booking_point = String.valueOf(usepoint);
                    booking_payfort = String.valueOf(payfortAmt);
                    requestPurchase(booking_payfort);
                }
            } else if (txtCheckWallet.isChecked()){
                useWallet = walletAmt;
                if (useWallet>=totalPrice){
                    booking_wallet = String.valueOf(totalPrice  );
                    booking_point= "";

                    String s = gson.toJson(setBooking());
                    Log.d(TAG, "onClick: booking wal"+s);
//                            makeBooking(s);
                    bookCar(s);

                } else {
                    payfortAmt = totalPrice-useWallet;
                    booking_point="";
                    booking_wallet = String.valueOf(useWallet);
                    booking_payfort = String.valueOf(payfortAmt);
                    requestPurchase(booking_payfort);
                }
            } else {
                booking_payfort = String.valueOf(totalPrice);
                booking_point="";
                booking_wallet="";
                requestPurchase(booking_payfort);
            }
        }

    }

    private BookingRequest setBooking(){
        bookingRequest.setName(name);
        bookingRequest.setSarname(sarname);
        bookingRequest.setNumber(number);
        bookingRequest.setEmail(email);
        bookingRequest.setPick_city_id(SearchCarFragment.pick_city_id);
        bookingRequest.setDrop_city_id(SearchCarFragment.drop_city_id);
        bookingRequest.setAddress(address);
        bookingRequest.setCity(city);
        bookingRequest.setZipcode(zipcode);
        bookingRequest.setCountrycode(countrycode);
        bookingRequest.setCar_id(car_id);
        bookingRequest.setType(type);
        bookingRequest.setCar_type(CarDetailActivity.car_type);
        bookingRequest.setRtype(rtype);
        bookingRequest.setFullprotection(fullprotection);
        bookingRequest.setFlight_no(flight_no);
        bookingRequest.setDob(dob);
        bookingRequest.setUser_id(user_id);
        bookingRequest.setPick_date(pick_date +" "+SearchCarFragment.pickTime);
        bookingRequest.setDrop_date(drop_date +" "+SearchCarFragment.dropTime);
        bookingRequest.setPick_city(pick_city);
        bookingRequest.setDrop_city(drop_city);
        bookingRequest.setProtection_val(protection_val);
        bookingRequest.setBooking_point(booking_point);
        bookingRequest.setBooking_wallet(booking_wallet);
        bookingRequest.setBooking_payfort(String.valueOf(booking_payfort));
        bookingRequest.setExtradata(extraData);
        bookingRequest.setDiscountCoupon(coupoun);
        bookingRequest.setTime_zone(time_zone);
        bookingRequest.setConerted_currency(curen);
     /*   if (CarDetailActivity.oneway!=null){
            bookingRequest.setBooking_one_way_fee(CarDetailActivity.oneway.replace("Oneway fee : ",""));
        }
        if (CarDetailActivity.driverSur!=null){
            bookingRequest.setDriver_charge(CarDetailActivity.driverSur.replace("Young Driver Surcharge :",""));
        }*/
        bookingRequest.setDiscountvalue(String.valueOf(discountvalue));
        String s = gson.toJson(bookingRequest);

        Log.d(TAG, "onClick: "+s);

        return bookingRequest;
    }
    double d =0;

    @SuppressLint("SetTextI18n")
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        int id  = view.getId();
        double p = 0, w;
        double remainPOint = 0;
        if (isCouponApplied){
            totalPrice = discountedPrice;
            switch(view.getId()) {
                case R.id.check_points:
                    if (checked){
                        if (txtCheckWallet.isChecked()){
                            w = totalPrice-walletAmt;
                            if (pointValue>=w){
                                booking_wallet=String.valueOf(walletAmt);
                                double pointBook =pointValue-w;
                                booking_point = String.valueOf(pointBook);
                                pointBal = pointValue-w;
                                txtPointVal.setText("( "+getResources().getString(R.string.txtRemainPtVa)
                                        +" : "+curen+Utility.convertCuurency(pointBal,getApplicationContext())+")");
                                payfortAmt = 0;
                                remainPOint=w;
                            } else {
                                remainPOint=pointValue;
                                payfortAmt = (w-p);
                                txtPointVal.setText("( " +getResources().getString(R.string.txtRemainPtVa)
                                        +" : "+curen+Utility.convertCuurency(pointBal,getApplicationContext())+")");
                            }
                            txtPointValueAmt.setText(getResources().getString(R.string.txtPointValue)+" : "
                                    +curen+ Utility.convertCuurency(p,getApplicationContext()));
                            txtPointValueAmt.setVisibility(View.VISIBLE);
                        }
//                        if wallet not checked 
                        else {
                            if (pointValue >= totalPrice) {
                                booking_point = String.valueOf(totalPrice);
                                pointBal = pointValue - totalPrice;
                                txtCheckWallet.setEnabled(false);
                                remainPOint = totalPrice;
                                txtPointVal.setText("( "+getResources().getString(R.string.txtRemainPtVa)
                                        +": "+curen + Utility.convertCuurency(pointBal, getApplicationContext()) + ")");
                            } else {
                                txtCheckWallet.setEnabled(true);
                                payfortAmt = totalPrice - pointValue;
                                remainPOint = pointValue;
                                txtPointVal.setText("( "+getResources().getString(R.string.txtRemainPtVa)
                                        +" : "+curen + Utility.convertCuurency(pointBal, getApplicationContext()) + ")");
                            }
                        }
                        txtPointValueAmt.setText( getResources().getString(R.string.txtPointValue)+" : "+
                                curen+ Utility.convertCuurency(remainPOint,getApplicationContext()));
                        txtPointValueAmt.setVisibility(View.VISIBLE);
                    }
                    else{
                        payfortAmt = totalPrice;
                        txtCheckWallet.setEnabled(true);
                        txtPointVal.setText("("+df2.format(totalPoint)+getResources().getString(R.string.txtPointValue)+" is : "
                                +Utility.convertCuurency(pointValue,getApplicationContext())+")");
                        txtPointValueAmt.setVisibility(View.GONE);
                    }
                    txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "
                            +curen+ Utility.convertCuurency(payfortAmt, getApplicationContext()));
                    break;
                case R.id.check_pay_online:
                    if (checked){
                        payfortAmt = totalPrice;
                        pointValue = 0;
                        walletAmt =0;
                        booking_point = String.valueOf(pointValue);
                        booking_wallet = String.valueOf(walletAmt);
                        booking_payfort = String.valueOf(payfortAmt);
                    }
                    break;
                case R.id.check_wallet:
                    if (checked){
                        double s=0;
                        if (txtcheckPoint.isChecked()){
                            s = totalPrice-pointValue;
                            if (walletAmt>=s){
                                walBal = walletAmt-s;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                        +Utility.convertCuurency(walBal,getApplicationContext())+")");
                                payfortAmt =0;
                                d=s;
                            }
                            else {
                                walBal = 0.0;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+
                                        " "+curen +Utility.convertCuurency(walBal, getApplicationContext()) +")");
                                payfortAmt = s-walletAmt;
                                d=walletAmt;
//                               txtPAyAmt.setText("Total payable amount : "+ String.valueOf(df2.format(payfortAmt)));
                            }
                        }
//                      if point not checked
                        else {
                            if (walletAmt>=totalPrice){
                                walBal = walletAmt-totalPrice;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                        +Utility.convertCuurency(walBal, getApplicationContext())+")");
                                payfortAmt =0;
                                d=totalPrice;
                                txtCheckWallet.setEnabled(true);
                                txtcheckPoint.setEnabled(false);
                            }
                            else {
                                walBal = 0.0;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                        + Utility.convertCuurency(walBal,getApplicationContext()) +")");
                                payfortAmt = totalPrice-walletAmt;
                                d=walletAmt;
                                txtCheckWallet.setEnabled(true);
                                txtcheckPoint.setEnabled(true);
                            }
                        }
                        txtWalletValueAmt.setText(getResources().getString(R.string.txtWal)+" : "+curen
                                + Utility.convertCuurency(d, getApplicationContext()));
                        txtWalletValueAmt.setVisibility(View.VISIBLE);
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "
                                +curen+ Utility.convertCuurency(payfortAmt,getApplicationContext()));

                    }
                    else {
                        booking_wallet=String.valueOf(df2.format(walletAmt));
                        txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                +Utility.convertCuurency(walletAmt,getApplicationContext())+")");
                        payfortAmt = totalPrice;
                        txtWalletValueAmt.setVisibility(View.GONE);
                        txtCheckWallet.setEnabled(true);
                        if (pointValue>0){
                            txtcheckPoint.setEnabled(true);
                        }
                    }
                    txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                            + Utility.convertCuurency(payfortAmt,getApplicationContext()));
                    break;
            }
        }
//        if coupon not applied
        else {
            switch(view.getId()) {
                case R.id.check_points:
                    if (checked){
                        double x = 0;
                        if (txtCheckWallet.isChecked()){
                            if (totalPrice>walletAmt){
                                x = totalPrice-walletAmt;
                            }
                            else {
                                x= walletAmt-totalPrice;
                            }
                            if (pointValue>=x){
                                booking_wallet=String.valueOf(walletAmt);
                               double pointBook =pointValue-x;
                                booking_point = String.valueOf(pointBook);
                                pointBal = pointValue-x;
                                txtPointVal.setText("("+getResources().getString(R.string.txtRemainPtVa)
                                        +" : "+Utility.convertCuurency(pointBal, getApplicationContext())+")");
                                payfortAmt = 0;
                                p=x;
                                txtCheckWallet.setEnabled(true);
                            } else {
                                p=pointValue;
                                payfortAmt = (x-p);
                                txtPointVal.setText("("+getResources().getString(R.string.txtRemainPtVa)
                                        +" : "+curen+Utility.convertCuurency(pointBal, getApplicationContext())+")");
                            }
                            txtPointValueAmt.setText(getResources().getString(R.string.txtPointValue)+" : "+curen
                                    + Utility.convertCuurency(p,getApplicationContext()));
                            txtPointValueAmt.setVisibility(View.VISIBLE);
                        }
//                        if wallet not checked
                        else {
                            if (pointValue>=totalPrice){
                                booking_point = String.valueOf(totalPrice);
                                pointBal = pointValue-totalPrice;
                                txtPointVal.setText("(" +getResources().getString(R.string.txtRemainPtVa)
                                        +": "+Utility.convertCuurency(pointBal, getApplicationContext())+")");
                                payfortAmt = 0;
                                p=totalPrice;
                                txtCheckWallet.setEnabled(false);
                            } else {
                                p =pointValue;
                                txtCheckWallet.setEnabled(true);
                                payfortAmt = totalPrice-pointValue;
                                txtPointVal.setText("("+getResources().getString(R.string.txtRemainPtVa)
                                        +": "+curen+Utility.convertCuurency(pointBal, getApplicationContext())+")");
                            }
                            txtPointValueAmt.setText(getResources().getString(R.string.txtPointValue)+" : "+curen
                                    + Utility.convertCuurency(p, getApplicationContext()));
                            txtPointValueAmt.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        payfortAmt = totalPrice;
                        txtCheckWallet.setEnabled(true);
                        txtPointVal.setText("("+Utility.convertCuurency(totalPoint,getApplicationContext())
                                +" "+getResources().getString(R.string.txtPointValue)+" : "
                                +Utility.convertCuurency(pointValue, getApplicationContext())+")");
                        txtPointValueAmt.setVisibility(View.GONE);
                    }
                    txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen+
                            Utility.convertCuurency(payfortAmt,getApplicationContext()));
                    break;
                case R.id.check_pay_online:
                    if (checked){
                        payfortAmt = totalPrice;
                        pointValue = 0;
                        walletAmt =0;
                        booking_point = String.valueOf(pointValue);
                        booking_wallet = String.valueOf(walletAmt);
                        booking_payfort = String.valueOf(payfortAmt);
                    }
                    break;
                case R.id.check_wallet:
                    if (checked){
                        double s=0;
                        if (txtcheckPoint.isChecked()){
                            s = totalPrice-pointValue;
                            if (walletAmt>=s){
                                walBal = walletAmt-s;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "
                                        +Utility.convertCuurency(walBal, getApplicationContext())+")");
                                payfortAmt =0;
                                d=s;
                                txtCheckWallet.setEnabled(true);
                                //  txtPAyAmt.setText("Total payable amount : "+ String.valueOf(df2.format(payfortAmt)));
                            } else {
                                walBal = 0.0;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen+
                                        Utility.convertCuurency(walBal,getApplicationContext()) +")");
                                payfortAmt = s-walletAmt;
                                d=walletAmt;
                                txtCheckWallet.setEnabled(true);
                            }
                        }
//                      if point not checked
                        else {
                            if (walletAmt>=totalPrice){
                                walBal = walletAmt-totalPrice;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                        +Utility.convertCuurency(walBal, getApplicationContext())+")");
                                payfortAmt =0;
                                d=totalPrice;
                                txtcheckPoint.setEnabled(false);
                            } else {
                                walBal = 0.0;
                                txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)
                                        + Utility.convertCuurency(walBal, getApplicationContext()) +")");
                                payfortAmt = totalPrice-walletAmt;
                                d=walletAmt;
                                if (pointValue>0){
                                    txtcheckPoint.setEnabled(true);
                                }
                            }
                        }
                        txtWalletValueAmt.setText(getResources().getString(R.string.txtWal)+" : "+curen
                                + Utility.convertCuurency(d, getApplicationContext()));
                        txtWalletValueAmt.setVisibility(View.VISIBLE);
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(payfortAmt, getApplicationContext()));
                    }
                    else {
                        booking_wallet=String.valueOf(df2.format(walletAmt));
                        txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "
                                +Utility.convertCuurency(walletAmt, getApplicationContext())+")");
                        payfortAmt = totalPrice;
                        if (pointValue>0){
                            txtcheckPoint.setEnabled(true);
                        }
                        txtWalletValueAmt.setVisibility(View.GONE);
                    }
                    txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                            + Utility.convertCuurency(payfortAmt, getApplicationContext()));

                    break;

            }
        }
    }


    /*0pyu8A1DPoNW1rpzeheA
access code
Merchant Identifier: daouwTJI
check these asap
SHA -256
1985
for both
Sha request and response pharse
*/

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.txtPayNow));
        getSDKToken(language);

    }

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* if (extraData!=null){
            extraData.clear();
        }*/
        if (tinyDB.contains("isSkipLogin"))
        tinyDB.remove("isSkipLogin");
    }

    private void requestSDKToken(String language) {
        createFORTMobileSDKToken(language);
    }

    private void requestOperation(String command, String sdk_token, String price) {

        final String ECI = "ECOMMERCE";
        final String CUSTOMER_EMAIL = email;
        final String LANGUAGE = language;
        final String CURRENCY = "SAR";
        double amt = Double.parseDouble(price)*100;
        int i = (int)amt;
        final String AMOUNT = String.valueOf(i);
        final String MERCHANT_REFERENCE = Utility.getRandomString(40) ;
        FortRequest fortRequest = new FortRequest();
        fortRequest.setShowResponsePage(true);
        final Map<String, Object> requestMap = new HashMap<>();

        requestMap.put("command",command);
        requestMap.put("merchant_reference",MERCHANT_REFERENCE);
        requestMap.put("amount",AMOUNT);
        requestMap.put("currency",CURRENCY);
        requestMap.put("language",LANGUAGE);
        requestMap.put("customer_email",CUSTOMER_EMAIL);
        requestMap.put("sdk_token",sdk_token);

//        requestMap.put("payment_option","AMEX");
//        requestMap.put("eci",ECI);
//        requestMap.put("order_description",command);
//        requestMap.put("customer_ip",command);
//        requestMap.put("customer_name",sdk_token);
//        if (mobile!=null){
//            requestMap.put("phone_number",mobile);
//        }
//        requestMap.put("settlement_reference",command);
//        requestMap.put("return_url",command);

        fortRequest.setRequestMap(requestMap);
        fortCallback = FortCallback.Factory.create();
        boolean showLoading= true;
        try {
            FortSdk.getInstance().registerCallback(this, fortRequest,FortSdk.ENVIRONMENT.TEST,
                    5, fortCallback,showLoading, new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map<String, Object> map, Map<String, Object> map1) {
                            Log.d(TAG, "onCancel: "+(String)map1.get("response_message"));
                            payfortAmt = 0;
                            booking_payfort=payfortAmt+"";
                            Toast.makeText(getApplicationContext(),  (String)map1.get("response_message"),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(Map<String, Object> map, Map<String, Object> map1) {
                            Log.d(TAG, "onSuccess: "+map1.toString());
                            Toast.makeText(getApplicationContext(), (String) map1.get("response_message"),
                                    Toast.LENGTH_SHORT).show();
                            transaction_id = (String) map1.get("fort_id");
                            merchant_reference = (String) map1.get("merchant_reference");
                            bookingRequest.setMerchant_reference(merchant_reference);
                            bookingRequest.setTransaction_id(transaction_id);
                            String s = gson.toJson(setBooking());
                            Log.d(TAG, "onSuccess: "+s);
//                            makeBooking(s);
                            bookCar(s);
                        }

                        @Override
                        public void onFailure(Map<String, Object> map, Map<String, Object> map1) {
                            Log.d(TAG, "onFailure: "+map1.get("response_message"));
                            Toast.makeText(getApplicationContext(), (CharSequence) map1.get("response_message"),
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFORTMobileSDKToken(String language) {
        OkHttpClient client = new OkHttpClient();
        String device_id = FortSdk.getDeviceId(getApplicationContext());
        StringBuilder base = new StringBuilder();
        base.append(REQUEST_PHRASE)
                .append("access_code=").append(ACCESS_TOKEN)
                .append("device_id=").append(device_id)
                .append("language=").append(language)
                .append("merchant_identifier=").append(MERCHANT_IDENTIFIER)
                .append("service_command=").append("SDK_TOKEN")
                .append(REQUEST_PHRASE);

        String signature = Utility.getSHA256Hash(base.toString());
        final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service_command","SDK_TOKEN") ;
            jsonObject.put("access_code",ACCESS_TOKEN) ;
            jsonObject.put("merchant_identifier",MERCHANT_IDENTIFIER) ;
            jsonObject.put("device_id",device_id) ;
            jsonObject.put("language",language) ;
            jsonObject.put("signature",signature) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request;
        request = new Request.Builder()
               // .url("https://paymentservices.payfort.com/FortAPI/paymentApi") //for live
                .url("https://sbpaymentservices.payfort.com/FortAPI/paymentApi") // for test
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(body)
                .addHeader("Content-Type", "application/json; charset=utf8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String mMessage = response.body().string();
                if (response.isSuccessful()){
                    try {
                        JSONObject json = new JSONObject(mMessage);
                        Gson gson = new Gson();
                        TokenResponse tokenResponse = new TokenResponse();
                        tokenResponse = gson.fromJson(json.toString(),TokenResponse.class);
                        sdk_token = tokenResponse.getSdk_token();

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            fortCallback.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void getSDKToken(String language) {
        requestSDKToken(language) ;
    }

    public void requestAuthorization() {
        requestOperation("AUTHORIZATION" ,sdk_token,"") ;
    }

    public void requestPurchase(String payfortAmt) {
        requestOperation("PURCHASE" ,sdk_token,payfortAmt) ;
    }

    public final okhttp3.MediaType MEDIA_TYPE = okhttp3.MediaType.parse("application/json");

    DiscountData discountData = new DiscountData();
    double discountedPrice , dis=0;

    public void validateCoupon(String coupounCode){
        Utility.showloadingPopup(this);
        CouponRequest couponRequest = new CouponRequest();
        couponRequest.setCode(coupounCode);
        couponRequest.setUser_id(user_id);
        String couponReq = new Gson().toJson(couponRequest);
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(MEDIA_TYPE,couponReq);

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://carshiring.com/webservice/discountCoupon")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(30000, TimeUnit.SECONDS)
                .build();
        Utility.showloadingPopup(this);
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = e.getMessage();
                        Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
                        Utility.hidepopup();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Utility.hidepopup();
                if (response!=null&&response.body().toString().length()>0){
                    if (request.body()!=null){
                        String msg = response.body().string();
                        discountData = gson.fromJson(msg, DiscountData.class);
                        if (discountData.isStatus()){
                            for (final DiscountData.ResponseBean.DiscountcouponBean.OfferDataBean discountcouponBean:
                                    discountData.getResponse().getDiscountcoupon().getOffer_data()){
                                isCouponApplied = true;
                                runOnUiThread(new Runnable() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void run() {
                                        if (discountcouponBean.getOffers_type().equals("1")){
                                            couponvalue = Double.parseDouble(discountcouponBean.getOffers_value());
                                            if (totalPrice<=couponvalue){
                                                dis = 0;
                                                txtcheckPoint.setEnabled(false);
                                                txtCheckWallet.setEnabled(false);
                                            } else if (totalPrice>couponvalue){
                                                discountedPrice = totalPrice-couponvalue;
                                                dis = discountedPrice;
                                            }
                                            discountvalue = couponvalue;
                                            txtCoupanValue.setVisibility(View.VISIBLE);
                                            txtCoupanValue.setText("Coupon value : "+curen+
                                                    Utility.convertCuurency(couponvalue,getApplicationContext()));
                                        } else {
                                            couponvalue = Double.parseDouble(discountcouponBean.getOffers_value());
                                            if (couponvalue==100){
                                                txtcheckPoint.setEnabled(false);
                                                txtCheckWallet.setEnabled(false);
                                            } else if (couponvalue>totalPrice){

                                            }
                                            discountvalue = (totalPrice*couponvalue)/100;
                                            discountedPrice = totalPrice-(totalPrice*couponvalue)/100;
                                            if (discountedPrice>totalPrice){
                                                dis = 0;
                                            } else if (totalPrice<discountedPrice){
                                                dis = 0;
                                            } else {
                                                dis = discountedPrice;
                                            }
                                            txtCoupanValue.setVisibility(View.VISIBLE);
                                            txtCoupanValue.setText(getResources().getString(R.string.activity_pay_coupon_value)+" : "+curen
                                                    + Utility.convertCuurency(((totalPrice*couponvalue)/100)
                                                    ,getApplicationContext())+ "("+String.valueOf(couponvalue)+"%)");
                                        }
                                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                                + Utility.convertCuurency(dis, getApplicationContext()));
                                        Utility.message(getApplicationContext(), getResources().getString(R.string.txt_coupon_applied));
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utility.message(getApplicationContext(), discountData.getMessage());
                                    coupoun="";
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    String booking="";
    public void bookCar(String cate){
        double pointDebit, walletValue = 0, remainingamt;
        if (isCouponApplied) {
            if (totalPrice>0){
                totalPrice = discountedPrice;
                if ((txtcheckPoint.isChecked() && txtCheckWallet.isChecked()
                        || (txtCheckWallet.isChecked() && txtcheckPoint.isChecked() && txtCheckPay.isChecked()))) {
                    if (totalPrice > pointValue) {
                        pointDebit = pointValue / .02;
                        remainingamt = totalPrice - pointValue;
                        if (remainingamt > walletAmt) {
                            walletValue = walletAmt;
                        } else {
                            walletValue = remainingamt;
                        }
                    } else {
                        pointDebit = totalPrice / .02;
                    }
                    debitPoint(booking, user_id, String.valueOf(pointDebit));
                    debitWallet(booking, user_id, String.valueOf(walletValue));
                    makeBooking(cate);
                } else if (txtcheckPoint.isChecked()) {
                    if (pointValue >= totalPrice) {
                        pointDebit = totalPrice / .02;
                    } else {
                        pointDebit = pointValue / .02;
                    }
                    debitPoint(booking, user_id, String.valueOf(pointDebit));
                    makeBooking(cate);

                } else if (txtCheckWallet.isChecked()) {
                    if (walletAmt >= totalPrice) {
                        walletValue = totalPrice;
                    } else {
                        walletValue = walletAmt;
                    }
                    debitWallet(booking, user_id, String.valueOf(walletValue));
                    makeBooking(cate);
                }

//                if any checkbox not checked
                else {
                    makeBooking(cate);
                }
            }
//            coupon value 100% applied
            else
             {
                 txtCheckWallet.setEnabled(false);
                 txtcheckPoint.setEnabled(false);
                makeBooking(cate);
            }
        }
        /*if coupon not applied */
        else {
            if ((txtcheckPoint.isChecked() && txtCheckWallet.isChecked()
                    || (txtCheckWallet.isChecked() && txtcheckPoint.isChecked() && txtCheckPay.isChecked()))) {
                if (totalPrice > pointValue) {
                    pointDebit = pointValue / .02;
                    remainingamt = totalPrice - pointValue;
                    if (remainingamt > walletAmt) {
                        walletValue = walletAmt;
                    } else {
                        walletValue = remainingamt;
                    }
                } else {
                    pointDebit = totalPrice / .02;
                }
                debitPoint(booking, user_id, String.valueOf(pointDebit));
                debitWallet(booking, user_id, String.valueOf(walletValue));
                makeBooking(cate);
            } else if (txtcheckPoint.isChecked()) {
                if (pointValue >= totalPrice) {
                    pointDebit = totalPrice / .02;
                } else {
                    pointDebit = pointValue / .02;
                }
                debitPoint(booking, user_id, String.valueOf(pointDebit));
                makeBooking(cate);

            } else if (txtCheckWallet.isChecked()) {
                if (walletAmt >= totalPrice) {
                    walletValue = totalPrice;
                } else {
                    walletValue = walletAmt;
                }
                debitWallet(booking, user_id, String.valueOf(walletValue));
                makeBooking(cate);
            } else {
                makeBooking(cate);
            }
        }
    }

    String lastPointid, lastWalletId;

    public void makeBooking(String cateRequest){
        // Utility.showloadingPopup(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Booking in processing...");
        progressDialog.show();

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(MEDIA_TYPE,cateRequest);

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(RetrofitApiBuilder.CarHires_BASE_URL+"make_test_buking")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(300000, TimeUnit.SECONDS)
                .writeTimeout(300000, TimeUnit.SECONDS)
                .readTimeout(300000, TimeUnit.SECONDS)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Log.d(TAG, "rakhi: "+e.getMessage());
                        String msg = e.getMessage();
                        Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                progressDialog.dismiss();

                if (response!=null&&response.body().toString().length()>0){
                    if (request.body()!=null){
                        String msg = response.body().string();

                        Log.d("TAG", "onResponse: booking"+ msg);
                        /*{"error_code":101,"status":true,"response":{"booking_id":"DT1521457211523"}}*/
                        try {
                            final JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("status")){
                                if (jsonObject.getBoolean("status")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PayActivity.this, "Booking success", Toast.LENGTH_SHORT).show();
                                            try {
                                                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                                String booking = jsonObject1.getString("booking_id");
                                                if (lastPointid!=null){
                                                    updatePointId(lastPointid,booking);
                                                }
                                                if (lastWalletId!=null){
                                                    updateWalletId(lastWalletId,booking);
                                                }
                                                double pointDebit, walletValue = 0, remainingamt;
                                                if (couponvalue==100){
                                                    earnPoint="0.0";
                                                  //  creditPoint(booking,user_id,earnPoint);
                                                } else {
                                                    if (booking!=null&&booking.length()>0){
                                                        creditPoint(booking,user_id,earnPoint);
                                                    }
                                                }
                                                if (extraData!=null){
                                                    extraData.clear();
                                                }

                                                startActivity(new Intent(PayActivity.this, ThankYou.class).putExtra("bookingid", booking));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            } else{
                                    setErrorDialog();
                            }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    Dialog dialog ;

    private void setErrorDialog(){
        dialog.setContentView(R.layout.error_dialog);
        TextView txtOk = dialog.findViewById(R.id.error_dialog_txtOk);
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PayActivity.this, MainActivity.class));
                finish();
            }
        });
        dialog.show();
    }

    private void updatePointId(String last_id, String booking_id){
        HashMap<String, String>pointMap = new HashMap<>();
        pointMap.put("last_id",last_id);
        pointMap.put("booking_id",booking_id);

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(MEDIA_TYPE,gson.toJson(pointMap));

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(RetrofitApiBuilder.CarHires_BASE_URL+"update_debit_point_bookingid")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(300000, TimeUnit.SECONDS)
                .writeTimeout(300000, TimeUnit.SECONDS)
                .readTimeout(300000, TimeUnit.SECONDS)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d(TAG, "onResponse: dataa"+gson.toJson(response.body()));
            }
        });

    }

    private void updateWalletId(String last_id, String booking_id){
        HashMap<String, String>pointMap = new HashMap<>();
        pointMap.put("last_id",last_id);
        pointMap.put("booking_id",booking_id);

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(MEDIA_TYPE,gson.toJson(pointMap));

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(RetrofitApiBuilder.CarHires_BASE_URL+"update_debit_walllet_bookingid")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(300000, TimeUnit.SECONDS)
                .writeTimeout(300000, TimeUnit.SECONDS)
                .readTimeout(300000, TimeUnit.SECONDS)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d(TAG, "onResponse: dataaaa"+gson.toJson(response.body()));
            }
        });

    }

    public void getWal(String user_id){
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> walList = fitApis.walletHistory(user_id);
        walList.enqueue(new retrofit2.Callback<ApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response!=null){
                    if (response.body().status){
                        walletHistoryData = response.body().response.wallet;
                        for (WalletHistoryData walletHistoryData1 : walletHistoryData){
                            if (walletHistoryData1.get_$WalletType204().equals("debit")){
                                String debit = walletHistoryData1.get_$WalletAmount169();
                                debitAmt = Double.parseDouble(debit);
                                totalDebit+= debitAmt;
                            }
                            if (walletHistoryData1.get_$WalletType204().equals("credit")){
                                String debit = walletHistoryData1.get_$WalletAmount169();
                                creditAmt = Double.parseDouble(debit);
                                totalCredit+= creditAmt;
                            }
                        }
                        walletAmt = totalCredit-totalDebit;

                        if (walletAmt>0){
                            txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                    +Utility.convertCuurency(walletAmt, getApplicationContext())+")");
                            txtWalletBal.setVisibility(View.VISIBLE);
                            txtCheckWallet.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d(TAG, "onResponse: "+response.body().msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.message(getApplicationContext(), getResources().getString(R.string.check_internet));
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }

    public void getPoint(String user_id){
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> walList = fitApis.pointHistory(user_id);
        walList.enqueue(new retrofit2.Callback<ApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response!=null){
                    if (response.body().status){
                        pointHistoryData = response.body().response.points;
                        Log.d("TAG", "onResponse: "+gson.toJson(pointHistoryData));
                        for (PointHistoryData walletHistoryData1 : pointHistoryData){
                            if (walletHistoryData1.get_$BookingPointType184().equals("debit")){
                                String debit = walletHistoryData1.get_$BookingPoint18();
                                debitPoint = Double.parseDouble(debit);
                                totalDebitPoint+= debitPoint;
                            }
                            if (walletHistoryData1.get_$BookingPointType184().equals("credit")){
                                String debit = walletHistoryData1.get_$BookingPoint18();
                                creditPoint = Double.parseDouble(debit);
                                totalCreditPoint+= creditPoint;
                            }
                        }
                        totalPoint = totalCreditPoint-totalDebitPoint;
                        pointValue = totalPoint*0.02;
//                     txtPointVal.setText(String.valueOf(totalPoint));
                        if (totalPoint>0.0){
                            txtPointVal.setText("("+String.valueOf(df2.format(totalPoint))+" "
                                    +getResources().getString(R.string.txtPointValue)+" : "+curen
                                    /*+getResources().getString(R.string.txtPointValue)+" is : SAR "*/
                                    +Utility.convertCuurency(pointValue, getApplicationContext())+")");
                            booking_point = String.valueOf(pointValue);
                            txtPointVal.setVisibility(View.VISIBLE);
                            txtcheckPoint.setVisibility(View.VISIBLE);
                        } else {
                            booking_point = String.valueOf(pointValue);
                            totalPoint = totalCreditPoint-totalDebitPoint;
                            txtcheckPoint.setVisibility(View.VISIBLE);
                            txtPointVal.setVisibility(View.VISIBLE);
                            txtPointVal.setText("("+String.valueOf(df2.format(totalPoint))+
                                    getResources().getString(R.string.txtPointValue)+"  is : "+curen
                                    +Utility.convertCuurency(pointValue, getApplicationContext())+")");
                            txtcheckPoint.setClickable(false);
                            txtcheckPoint.setEnabled(false);
                        }
                    } else {
                        Log.d(TAG, "onResponse: "+response.body().msg);
                        //  Utility.message(getApplicationContext(),response.body().message);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.message(getApplicationContext(), getResources().getString(R.string.check_internet));
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }

    public void creditPoint(String bookingId,String user_id, String booking_point){
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> creditPoint = fitApis.creditPoint(user_id,booking_point,bookingId);

        creditPoint.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                Log.d(TAG, "onResponse: data"+gson.toJson(response.body()));
                if (response.body()!=null){
                    if (response.body().status){
                         Toast.makeText(PayActivity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PayActivity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void debitPoint(String bookingId,String user_id, String booking_point){
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> creditPoint = fitApis.debitPoint(user_id,booking_point,bookingId);

        creditPoint.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                Log.d(TAG, "onResponse: debit point"+gson.toJson(response.body()));
                if (response.body()!=null){
                    if (response.body().status) {
                        lastPointid = response.body().last_insert_id;
                        //Toast.makeText(PayActivity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(PayActivity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void debitWallet(String bookingId,String user_id, String wallet_amount ){
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> creditPoint = fitApis.debitWallet(user_id,wallet_amount,bookingId);

        creditPoint.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                Log.d(TAG, "onResponse: data"+gson.toJson(response.body()));
                if (response.body()!=null){
                    if (response.body().status){
                        lastWalletId = response.body().last_insert_id;
                        // Toast.makeText(PayActivity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(PayActivity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void forgotPass(View view){
        startActivity(new Intent(PayActivity.this,ForgotPasswordActivity.class));
    }

    private void setupoverlay(String set) {

        final EditText edtFname, edtLname, edtemail, edtPhone, edtZip, edtLicense, edtCity,
                edtAddress;
        Spinner edtLicenseOrign;

        Button btupdate, btnCancel;
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (set.equals("login")) {
            dialog.setContentView(R.layout.popup_login);
            final EditText edtEmail = dialog.findViewById(R.id.et_email);
            final EditText edtPass = dialog.findViewById(R.id.et_password);
            final Button login = dialog.findViewById(R.id.bt_login);
            Button signup = (Button) dialog.findViewById(R.id.bt_signup);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    startActivity(new Intent(PayActivity.this, SignUpActivity.class)
                            .putExtra("booking","booksign"));
                    finish();
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emaillogin = edtEmail.getText().toString().trim();
                    pass = edtPass.getText().toString().trim();

                    if (Utility.checkemail(emaillogin)) {
                        if (!pass.isEmpty()) {
                            login(emaillogin, pass);
                        } else {
                            Utility.message(getApplicationContext(), "Please enter your password");
                        }
                    } else {
                        Utility.message(getApplicationContext(), "Please enter valid email");
                    }
                }
            });

        } else if (set.equals("update_profile")) {
            dialog.setContentView(R.layout.popup_updateprofile);
            edtFname = dialog.findViewById(R.id.etUserFirstName);
            edtLname = dialog.findViewById(R.id.etUserLastName);
            edtemail = dialog.findViewById(R.id.etUserEmail);
            edtemail.setText(userDetails.getUser_email());
            edtFname.setText(userDetails.getUser_username());
            edtemail.setEnabled(false);
            edtPhone = dialog.findViewById(R.id.etUserPhoneNo);
            edtZip = dialog.findViewById(R.id.etUserzip);
            edtLicense = dialog.findViewById(R.id.etlicense);
            edtLicenseOrign = dialog.findViewById(R.id.spinnerlicenseorigion);
            txtDob = dialog.findViewById(R.id.etUserDob);
            edtCity = dialog.findViewById(R.id.etcity);
            edtAddress = dialog.findViewById(R.id.etAddress);
            btupdate = dialog.findViewById(R.id.bt_update);
            btnCancel = dialog.findViewById(R.id.bt_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            txtDob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dob_pick();
                }
            });
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, SplashActivity.counrtyList);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            edtLicenseOrign.setAdapter(dataAdapter);
            edtLicenseOrign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = adapterView.getItemAtPosition(i).toString();
                    licenseorigin = (String) getKeyFromValue(SplashActivity.country, item);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

//            set onclick on update
            btupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fname = edtFname.getText().toString().trim();
                    lname = edtLname.getText().toString().trim();
                    email = edtemail.getText().toString().trim();
                    phone = edtPhone.getText().toString().trim();
                    zip = edtZip.getText().toString().trim();
                    license = edtLicense.getText().toString().trim();
                    city = edtCity.getText().toString().trim();
                    address = edtAddress.getText().toString().trim();
                    if (license.isEmpty()){
                        license = "";
                    }
                    if (!fname.isEmpty()&&fname.length()>3) {
                        if (!lname.isEmpty()) {
                            if (Utility.checkemail(email)) {
                                if (Utility.checkphone(phone)) {
                                    if (dob!=null && !dob.isEmpty()){
                                        updateProfile(user_id,fname);
                                    } else {
                                        Utility.message(getApplication(), "Please select DOB");
                                    }
                                } else {
                                    Utility.message(getApplication(), getResources().getString(R.string.please_enter_valid_phone_number));
                                }
                            } else {
                                Utility.message(getApplication(), getResources().getString(R.string.please_enter_valid_email));
                            }
                        } else {
                            Utility.message(getApplication(), getResources().getString(R.string.please_enter_last_name));
                        }
                    } else {
                        Utility.message(getApplication(), getResources().getString(R.string.please_enter_first_name));
                    }
                }
            });
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    private String fname, lname, phone, zip, license, licenseorigin,  emaillogin, pass;

    private void updateProfile(String userid, String fname) {
        if (!Utility.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(PayActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            return;
        }
        Utility.showloadingPopup(this);
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall = retroFitApis.updateprofile(userid, fname, lname, email, phone, zip, license,
                licenseorigin, dob, city, address);
        responseCall.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                Utility.hidepopup();
                if (response.body().status == true) {
                    UserDetails userDetails = new UserDetails();
                    userDetails = response.body().response.user_detail;
                    String logindata = gson.toJson(response.body().response.user_detail);
                    appGlobal.setLoginData(logindata);
                    Log.d(TAG, "onResponse: "+logindata);
                    String st = appGlobal.getUser_id();
                    dialog.dismiss();
                    if (userDetails!=null&&userDetails.getUser_id()!=null){
                        user_id = userDetails.getUser_id();
                        name = userDetails.getUser_name();
                        sarname = (String) userDetails.getUser_lname();
                        number = userDetails.getUser_phone();
                        email   = userDetails.getUser_email();
                        address = userDetails.getUser_address();
                        city = (String) userDetails.getUser_city();
                        zipcode = userDetails.getUser_zipcode();
                        countrycode = (String) userDetails.getUser_country();
                        dob = userDetails.getUser_dob();
                    }

                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (sdk_token!=null){
                                pay();
                            } else {
                                getSDKToken(language);
                            }
                        }
                    },2000);*/
                 //   Utility.message(getApplicationContext(), response.body().msg);
                } else {
                    Utility.message(getApplicationContext(), response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
            }
        });
    }

    private AppGlobal appGlobal = AppGlobal.getInstancess();

    private void login(String user, String pass) {
        if (!Utility.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(PayActivity.this,
                    getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            return;
        }

        Utility.showloadingPopup(this);
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall = retroFitApis.login(user, pass);
        responseCall.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                Utility.hidepopup();
                assert response.body() != null;
                if (response.body().status) {
                    UserDetails userDetails = response.body().response.user_detail;
                    String logindata = gson.toJson(userDetails);
                    appGlobal.setLoginData(logindata);
                    dialog.dismiss();
                    String login_data = tinyDB.getString("login_data");
                    userDetails = gson.fromJson(login_data, UserDetails.class);
                    String st = userDetails.getUser_id();
                    MainActivity.logout.setTitle(getResources().getString(R.string.logout));
                    getPoint(st);
                    getWal(st);

                } else {
                    Utility.message(getApplicationContext(), response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
            }
        });
    }


    public void dob_pick() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = Calendar.getInstance().get(Calendar.YEAR) - 21;
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        long timeInMilliseconds = Utility.getTimeDate(mYear + "-" + mMonth + "-" + mDay);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dob =(monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        txtDob.setText(dob);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(timeInMilliseconds);
        datePickerDialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    int count =1;
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        final TinyDB tinyDB1 = new TinyDB(getApplicationContext());
        if (tinyDB1.contains("login_data")){
            String data = tinyDB1.getString("login_data");
            userDetails = gson.fromJson(data, UserDetails.class);
            if (userDetails.getUser_id()!=null){
                user_id = userDetails.getUser_id();
            }
        }

        if (acName==null){
            if (tinyDB1.contains("login_data")) {
                if (userDetails!=null&&userDetails.getUser_lname() == null || userDetails.getUser_lname().length() == 0) {
                    set = "update_profile";
                    setupoverlay(set);
                }
            } else {
                set = "login";
                setupoverlay(set);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    String user_age;
    private void ageDialog(){
        final Dialog  dialog;
        dialog = new Dialog(PayActivity.this);
        dialog.setContentView(R.layout.dialog_age_error);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        TextView txtOk = dialog.findViewById(R.id.dialog_age_error_ok);
        TextView txtDetail = dialog.findViewById(R.id.dialog_age_error_txtInfo);
        txtDetail.setText("We have found that you are searching with driver age ("+driver_age
                +"), but your age according to your profile is "+user_age+", Please change " +
                "your search age or update your DOB in profile section and search again.");

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i =new Intent(PayActivity.this,MainActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }



}
