package com.carshiring.activities.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.carshiring.adapters.CountryAdapter;
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

public class Pay1Activity extends AppBaseActivity implements CountryAdapter.OnItemClickListener{
    ActionBar actionBar;

    private TextView txtPay, txtTotalAmyVal,txtDob,txtPAyAmt,txtWalletBal,txtPointValueAmt, txtWalletValueAmt,
            txtCoupanValue,txtPointVal,txtFullProAmt, txtEarnedPoint, txtApply,txtExtraCharge,txtNonUsablePoint;
    private CheckBox txtcheckPoint,txtCheckPay, txtCheckWallet;
    private String price="", email="",sdk_token="";
    private FortCallBackManager fortCallback;
    private double extraPrice;

/*NCszWushUPEjueRWnLti
new access code
Merchant Identifier: daouwTJI
*/
// for live
    final String ACCESS_TOKEN =  "5JCpZWu9J5Hlz3IoK1KN";
    final String MERCHANT_IDENTIFIER = "daouwTJI";
    final String REQUEST_PHRASE = "1985" ;

/*//for test
    final String ACCESS_TOKEN = "qa2s6awTpBNc04Q65T8v";
    final String MERCHANT_IDENTIFIER = "GjitDYjm";
    final String REQUEST_PHRASE = "PASS" ;*/

    private LinearLayout fullProtectionLayout;
    private String name="",sarname="",set="",number="",address="",city="",zipcode="",countrycode="",car_id="",
            type="",rtype="",coupoun="",user_age,time_zone="",
            fullprotection="",flight_no="",dob="",user_id="",pick_date="",
            drop_date="", pick_city="",acName,drop_city="",protection_val="",booking_point="",booking_wallet="",
            booking_payfort="",transaction_id="",merchant_reference="",language="", earnPoint,curen;
    private TinyDB tinyDB;
    private List<WalletHistoryData> walletHistoryData = new ArrayList<>();
    private List<PointHistoryData>pointHistoryData = new ArrayList<>();
    private double creditAmt, debitAmt,walletAmt, totalDebit, totalPrice,totalCredit,totalPoint, earnedPointValue,totalDebitPoint,
            totalCreditPoint, creditPoint,debitPoint,couponvalue, payfortAmt, discountvalue,pointBal, nonUsablePoint;
    private EditText edtCoupon;
    private Gson gson = new Gson();
    boolean isCouponApplied;
    private UserDetails userDetails = new UserDetails();
    private String TAG = Pay1Activity.class.getName();
    private BookingRequest bookingRequest = new BookingRequest();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay1);

        time_zone = TimeZone.getDefault().getID()+"";
        actionBar = getSupportActionBar() ;
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        if (getIntent().hasExtra("activity")){
            acName = getIntent().getStringExtra("activity");
        }

        if (getIntent().hasExtra("extraPrice")){
            extraPrice = getIntent().getDoubleExtra("extraPrice",0);
        }
        appGlobal.context = getApplicationContext();
        dialog = new Dialog(Pay1Activity.this);

        tinyDB = new TinyDB(getApplicationContext());
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


        fortCallback = FortCallback.Factory.create();

//        comment for testing
        /*fullprotection = BookCarActivity.fullProtection;
        if (fullprotection.equalsIgnoreCase("yes")){
            protection_val =String.valueOf( CarDetailActivity.fullAmtValue);
        }*/
//        extraData = BookCarActivity.extraData;

        txtNonUsablePoint = findViewById(R.id.txtNonUsable);
        fullProtectionLayout = findViewById(R.id.activity_pay_full_pro_layout);
        edtCoupon = findViewById(R.id.activity_pay_edtCoupon);
        txtEarnedPoint = findViewById(R.id.txtEarnedPoint);
        txtApply = findViewById(R.id.activity_pay_btnApply);
        txtCoupanValue = findViewById(R.id.activity_pay_txtCouponValue);
        txtPointValueAmt = findViewById(R.id.txtPointValueAmt);
        txtWalletValueAmt = findViewById(R.id.txtWaletValueAmt);
        txtFullProAmt = findViewById(R.id.activity_pay_txtFullProtectionAmtValue);
        txtExtraCharge = findViewById(R.id.activity_pay_txtExtra);
        txtPAyAmt = findViewById(R.id.activity_pay_txtPayAmt);
        txtCheckPay = findViewById(R.id.check_pay_online);
        txtcheckPoint = findViewById(R.id.check_points);
        txtCheckWallet = findViewById(R.id.check_wallet);
        txtWalletBal = findViewById(R.id.txtWaletValue);
        txtPointVal = findViewById(R.id.txtPointValue);
        txtPay = findViewById(R.id.txtPay);
        txtTotalAmyVal = findViewById(R.id.txtTotalPayValue);

        if (fullprotection.equalsIgnoreCase("yes")){
            protection_val =String.valueOf( CarDetailActivity.fullAmtValue);
        }
        if (fullprotection.equalsIgnoreCase("yes")){
            fullProtectionLayout.setVisibility(View.GONE);//should be visible
            txtFullProAmt.setText(curen +Utility.convertCuurency(CarDetailActivity.fullAmtValue, getApplicationContext()));
        } else {
            fullProtectionLayout.setVisibility(View.GONE);
        }
        double extra = 0;
        if (extraPrice>0||
                CarsResultListActivity.driverSurCharge!=null )
            extra = extraPrice;
        double  oneway = 0,youngDriver = 0;
        if (CarsResultListActivity.oneWayFee!=null)
            oneway = Double.parseDouble(CarsResultListActivity.oneWayFee);
        if (CarsResultListActivity.driverSurCharge!=null)
            youngDriver = Double.parseDouble(CarsResultListActivity.driverSurCharge);
        double total = extra+ oneway
                +youngDriver;
        if (total>0){
            txtExtraCharge.setText(getResources().getString(R.string.selectedExtra)+" : "+curen
                    + Utility.convertCuurency(total,getApplicationContext()));
            txtExtraCharge.setVisibility(View.VISIBLE);
        } else txtExtraCharge.setVisibility(View.GONE);

        price = CarDetailActivity.carPrice;
        fullprotection="no";//for testing
        if (fullprotection!=null && fullprotection.equalsIgnoreCase("yes")){
            if (price!=null){
                totalPrice = Double.parseDouble(price);
                totalPrice = Double.parseDouble(price)+Double.parseDouble(protection_val);
            }
        } else {
            if (price!=null){
                totalPrice = Double.parseDouble(price);
            }
        }

        earnPoint = String.valueOf(CarDetailActivity.point);
        txtEarnedPoint.setText(getResources().getString(R.string.colletcted_point )+" "+ earnPoint );

        if (tinyDB.contains("login_data")){
            String data = tinyDB.getString("login_data");
            userDetails = gson.fromJson(data, UserDetails.class);
            MainActivity.logout.setTitle(getResources().getString(R.string.logout));
            if (userDetails.getUser_id()!=null){
                user_id = userDetails.getUser_id();
            }
        }
        txtApply.setOnClickListener(view -> {
            if (tinyDB.contains("login_data")){
                String data = tinyDB.getString("login_data");
                userDetails = gson.fromJson(data, UserDetails.class);
                MainActivity.logout.setTitle(getResources().getString(R.string.logout));
                if (userDetails.getUser_id()!=null){
                    user_id = userDetails.getUser_id();
                }
            }
            coupoun = edtCoupon.getText().toString().trim();
            if (user_id!=null&&user_id.length()>0){
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
                    txtPointValueAmt.setVisibility(View.GONE);
                } else {
                    Utility.message(getApplication(), getResources().getString(R.string.enterCoupn));
                }
            } else {
                set = "login";
                setupoverlay(set);
                Utility.message(getApplicationContext(),"Login required...");
            }
        });

        txtPay.setOnClickListener(view -> {
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
                    if (user_age!=null ){
                        if (tinyDB1.getBoolean("isSkipLogin")){
                            if (tinyDB1.getString("dAge")!=null && tinyDB1.getString("dAge").length()==0){
                                final Calendar c = Calendar.getInstance();
                                int mYear = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(driver_age);
                                int mMonth = c.get(Calendar.MONTH);
                                int mDay = c.get(Calendar.DAY_OF_MONTH);
                                String month, day ;
                                if (mMonth<10){
                                    month = "0"+(mMonth+1);
                                } else month = (mMonth+1)+"";

                                if (mDay<10){
                                    day = "0"+mDay;
                                } else day = mDay+"";
                                dob = mYear+ "-" + month  + "-" + day;
                                pay();
                            } else if (user_age.equals(driver_age)){
                                /*dob = dateFormat(dob);*/

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
                                    /*dob= dateFormat(dob);*/
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
        });

        txtTotalAmyVal.setText(curen + Utility.convertCuurency(Double.parseDouble(CarDetailActivity.carPrice),
                getApplicationContext())+ "/ "
                +CarsResultListActivity.day + " "+ CarsResultListActivity.time);
        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                +Utility.convertCuurency(totalPrice, getApplicationContext()));

        getPoint(user_id);
        getWal(user_id);
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

    private void ageDialog(){
        final Dialog  dialog;
        dialog = new Dialog(Pay1Activity.this);
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
                Intent i =new Intent(Pay1Activity.this,MainActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void pay(){
        CarsResultListActivity.driverSurCharge = null;
        CarsResultListActivity.oneWayFee = null;
        if (!txtcheckPoint.isChecked()&&!txtCheckWallet.isChecked()){
            totalPayableAmt = totalPrice;
        }
        if (totalPayableAmt>0){
            if (usePointValue>0){
                booking_point = usePointValue+"";
            }
            if (useWalletValue>0){
                booking_wallet = useWalletValue+"";
            }
            requestPurchase(totalPayableAmt+"");
        } else {
            booking_point = usePointValue+"";
            booking_wallet = useWalletValue+"";
            String s = gson.toJson(setBooking());
            Log.d(TAG, "rakhi: booking request"+s);
//            bookCar(s);

               bookCar(s,usePointValue,useWalletValue,totalPayableAmt);
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
        bookingRequest.setDriver_age(SearchCarFragment.driver_age);
        bookingRequest.setCity(city);
        bookingRequest.setZipcode(zipcode);
        bookingRequest.setCountrycode(countrycode);
        bookingRequest.setCar_id(car_id);
        bookingRequest.setType(type);
        bookingRequest.setCar_type(CarDetailActivity.car_type);
        bookingRequest.setRtype(rtype);
        fullprotection="";
        bookingRequest.setFullprotection(fullprotection);
        bookingRequest.setFlight_no(flight_no);
        bookingRequest.setDob(dob);
        bookingRequest.setUser_id(user_id);
        bookingRequest.setPick_date(pick_date +" "+SearchCarFragment.pickTime);
        bookingRequest.setDrop_date(drop_date +" "+SearchCarFragment.dropTime);
        bookingRequest.setPick_city(pick_city);
        bookingRequest.setDrop_city(drop_city);
        protection_val = "";
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

    @SuppressLint("SetTextI18n")
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (isCouponApplied){
            totalPrice = discountedPrice;
        }
        clicked(view,checked);
    }

    private double totalPayableAmt,temp_point, temp_price, temp_wallet,usePointValue, useWalletValue;
    @SuppressLint("SetTextI18n")
    private void clicked(View view, boolean checked){
        switch (view.getId()){
            case R.id.check_points:
                if (checked){
                    temp_price = totalPrice;
                    temp_point = earnedPointValue;
                    temp_wallet = walletAmt;
                    useWalletValue = 0;
                    usePointValue=0;
                    if (txtCheckWallet.isChecked()){
                        if (temp_wallet>=temp_price){
                            temp_wallet = temp_wallet-temp_price;
                            totalPayableAmt = 0;
                            useWalletValue = temp_price;
                        } else {
                            txtcheckPoint.setEnabled(true);
                            totalPayableAmt = temp_price - temp_wallet;
                            useWalletValue = temp_wallet;
                            temp_wallet=0;
                        }
                        txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                +Utility.convertCuurency(temp_wallet,getApplicationContext())+")");
                        txtWalletValueAmt.setText(getResources().getString(R.string.txtWal)+" : "+curen
                                + Utility.convertCuurency(useWalletValue, getApplicationContext()));
                        txtWalletValueAmt.setVisibility(View.VISIBLE);
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                    }
                    else {
                        useWalletValue = 0;
                        txtcheckPoint.setEnabled(true);
                        txtWalletValueAmt.setVisibility(View.GONE);
                        if (temp_point>=temp_price){
                            totalPayableAmt = 0;
                            txtCheckWallet.setEnabled(false);
                            temp_point = temp_point-temp_price;
                            usePointValue = temp_price;
                        } else {
                            totalPayableAmt= temp_price-temp_point;
                            txtCheckWallet.setEnabled(true);
                            usePointValue = temp_point;
                            temp_point = 0;
                        }
                        txtPointValueAmt.setText(getResources().getString(R.string.txtPointValue)+" : "
                                +curen+ Utility.convertCuurency(usePointValue,getApplicationContext()));
                        txtPointValueAmt.setVisibility(View.VISIBLE);
                        txtPointVal.setText("("+getResources().getString(R.string.txtRemainPtVa)
                                +" : "+curen+Utility.convertCuurency(temp_point, getApplicationContext())+")");
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                    }
                } else {
                    temp_price = totalPrice;
                    temp_wallet = walletAmt;
                    temp_point = earnedPointValue;

                    txtPointValueAmt.setVisibility(View.GONE);
                    txtCheckWallet.setEnabled(true);

                    if (txtCheckWallet.isChecked()){
                        if (temp_wallet>=temp_price){
                            temp_wallet = temp_wallet-temp_price;
                            totalPayableAmt = 0;
                            txtcheckPoint.setEnabled(false);
                            useWalletValue = temp_price;
                        } else {
                            txtcheckPoint.setEnabled(true);
                            totalPayableAmt = temp_price - temp_wallet;
                            useWalletValue = temp_wallet;
                            temp_wallet=0;
                        }
                        txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                +Utility.convertCuurency(temp_wallet,getApplicationContext())+")");
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                        txtWalletValueAmt.setText(getResources().getString(R.string.txtWal)+" : "+curen
                                + Utility.convertCuurency(useWalletValue, getApplicationContext()));
                        txtWalletValueAmt.setVisibility(View.VISIBLE);
                    }
                    else {
                        txtWalletValueAmt.setVisibility(View.GONE);
                        totalPayableAmt = totalPrice;
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                        txtPointVal.setText("("+getResources().getString(R.string.txtRemainPtVa)
                                +" : "+curen+Utility.convertCuurency(temp_point, getApplicationContext())+")");
                    }
                }
                break;
            case R.id.check_wallet:
                temp_price =totalPrice;
                temp_wallet = walletAmt;
                usePointValue=0;
                useWalletValue=0;
                if (checked){
                    temp_price = totalPrice;
                    temp_point = earnedPointValue;
                    if (txtcheckPoint.isChecked()){
                        if (temp_point>=temp_price){
                            temp_point = temp_point-temp_price;
                            totalPayableAmt = 0;
                            usePointValue = temp_price;
                        } else {
                            txtcheckPoint.setEnabled(true);
                            totalPayableAmt = temp_price - temp_point;
                            temp_price = temp_price-temp_point;
                            usePointValue = temp_point;
                            temp_point = 0;
                            if (temp_wallet<=temp_price){
                                totalPayableAmt = temp_price-temp_wallet;
                                useWalletValue = temp_wallet;
                            } else {
                                totalPayableAmt = 0;
                                useWalletValue=temp_price;
                                temp_wallet = temp_wallet-temp_price;
                            }
                        }
                        txtWalletValueAmt.setText(getResources().getString(R.string.txtWal)+" : "+curen
                                + Utility.convertCuurency(useWalletValue, getApplicationContext()));
                        txtWalletValueAmt.setVisibility(View.VISIBLE);
                        txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                +Utility.convertCuurency(temp_wallet,getApplicationContext())+")");
                        txtPointVal.setText("("+getResources().getString(R.string.txtRemainPtVa)
                                +" : "+curen+Utility.convertCuurency(temp_point, getApplicationContext())+")");
                        txtPointValueAmt.setText(getResources().getString(R.string.txtPointValue)+" : "
                                +curen+ Utility.convertCuurency(usePointValue,getApplicationContext()));
                        txtPointValueAmt.setVisibility(View.VISIBLE);
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                    } else {
                        if (temp_wallet<=temp_price){
                            totalPayableAmt = temp_price-temp_wallet;
                            useWalletValue = temp_wallet;
                            temp_wallet=0;
                        } else {
                            totalPayableAmt = 0;
                            temp_wallet = temp_wallet-temp_price;
                            useWalletValue = temp_price;
                            txtcheckPoint.setEnabled(false);
                        }
                        txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                +Utility.convertCuurency(temp_wallet,getApplicationContext())+")");
                        txtPointValueAmt.setVisibility(View.GONE);
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                        txtWalletValueAmt.setText(getResources().getString(R.string.txtWal)+" : "+curen
                                + Utility.convertCuurency(useWalletValue, getApplicationContext()));
                        txtWalletValueAmt.setVisibility(View.VISIBLE);
                    }
                } else {
                    temp_price = totalPrice;
                    temp_point = earnedPointValue;
                    temp_wallet = walletAmt;
                    txtWalletValueAmt.setVisibility(View.GONE);
                    txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                            +Utility.convertCuurency(temp_wallet,getApplicationContext())+")");
                    if (txtcheckPoint.isChecked()){
                        if (temp_point>=temp_price){
                            temp_point = temp_point-temp_price;
                            txtCheckWallet.setEnabled(false);
                            usePointValue = temp_price;
                            totalPayableAmt = 0;
                        } else {
                            txtcheckPoint.setEnabled(true);
                            totalPayableAmt = temp_price - temp_point;
                            temp_price = temp_price-temp_point;
                            usePointValue = temp_point;
                            temp_point =0;
                        }

                        txtPointValueAmt.setText(getResources().getString(R.string.txtPointValue)+" : "
                                +curen+ Utility.convertCuurency(usePointValue,getApplicationContext()));
                        txtPointValueAmt.setVisibility(View.VISIBLE);
                        txtWalletBal.setText("("+getResources().getString(R.string.txtAvailable)+" "+curen
                                +Utility.convertCuurency(temp_wallet,getApplicationContext())+")");
                        txtPointVal.setText("("+getResources().getString(R.string.txtRemainPtVa)
                                +" : "+curen+Utility.convertCuurency(temp_point, getApplicationContext())+")");

                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+": "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                    } else {
                        txtcheckPoint.setEnabled(true);
                        totalPayableAmt = totalPrice;
                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                        txtPointValueAmt.setVisibility(View.GONE);
                    }
                }
                break;
            default:
                totalPayableAmt = totalPrice;
                txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                        + Utility.convertCuurency(totalPayableAmt,getApplicationContext()));
                txtPointValueAmt.setVisibility(View.GONE);
                txtWalletValueAmt.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.txtPayNow));

        getSDKToken(language);
    }

    private static DecimalFormat df2 = new DecimalFormat("#0.00");

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        tinyDB.remove("extra_added");
//        tinyDB.remove("full_prot");

        if (extraData!=null){
            extraData.clear();
        }

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
        Log.d(TAG, "requestOperation: "+requestMap);

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
        boolean showLoading= true;
/*
        try {
            FortSdk.getInstance().registerCallback(this, fortRequest,FortSdk.ENVIRONMENT.PRODUCTION,
                    5, fortCallback,showLoading, new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map<String, Object> map, Map<String, Object> map1) {
                            Log.d(TAG, "onCancel: "+map1.get("response_message"));
                            payfortAmt = 0;
                            totalPayableAmt = 0;
                            booking_payfort = totalPayableAmt+"";
                            Toast.makeText(getApplicationContext(),  (String)map1.get("response_message"),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(Map<String, Object> map, Map<String, Object> map1) {
                            Log.d(TAG, "onSuccess: "+map1.toString());
                          */
/*  Toast.makeText(getApplicationContext(), (String) map1.get("response_message"),
                                    Toast.LENGTH_SHORT).show();*//*

                            transaction_id = (String) map1.get("fort_id");
                            booking_payfort = totalPayableAmt+"";
                            merchant_reference = (String) map1.get("merchant_reference");
                            bookingRequest.setMerchant_reference(merchant_reference);
                            bookingRequest.setTransaction_id(transaction_id);
                            String s = gson.toJson(setBooking());
                            Log.d(TAG, "onSuccess: "+s);
                            bookCar(s,usePointValue,useWalletValue,totalPayableAmt);
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
*/
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
        JSONObject jsonObject = new JSONObject() ;
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

        Log.d(TAG, "createFORTMobileSDKToken: "+jsonObject);
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request;
        request = new Request.Builder()
               .url("https://paymentservices.payfort.com/FortAPI/paymentApi") // for live
        //.url("https://sbpaymentservices.payfort.com/FortAPI/paymentApi") // for test
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(body)
                .addHeader("Content-Type", "application/json; charset=utf8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String mMessage = response.body().string();
                Log.w("success Response", mMessage);

                if (response.isSuccessful()){
                    try {
                        JSONObject json = new JSONObject(mMessage);
                         Gson gson = new Gson();
                        TokenResponse tokenResponse;
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
                .url(RetrofitApiBuilder.CarHires_BASE_URL+"discountCoupon")
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
                                            txtCoupanValue.setText("Coupon value : "+curen
                                                    + Utility.convertCuurency(((totalPrice*couponvalue)/100)
                                                    ,getApplicationContext())+ "("+String.valueOf(couponvalue)+"%)");
                                        }
                                        txtPAyAmt.setText(getResources().getString(R.string.txtTotalPayableAmt)+" : "+curen
                                                + Utility.convertCuurency(dis, getApplicationContext()));
                                        Utility.message(getApplicationContext(), "Coupon applied ");
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utility.message(getApplicationContext(),"Invalid Coupon");
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
    public void bookCar(String cate, double usepoint, double usewallet, double payableAmt) {
        double debitPoint, debitWallet;
        if (usepoint>0 && usewallet>0){
            debitPoint = usepoint/0.02;
            debitWallet = usewallet;
            Log.d(TAG, "bookCar: "+debitPoint  + "\n"+debitWallet+"\n"+payableAmt);
            booking_wallet = debitWallet+"";
            booking_point = debitPoint+"";
            debitPoint(booking, user_id,debitPoint+"");
            debitWallet(booking,user_id,debitWallet+"");
            makeBooking(cate);
        } else if (usepoint>0){
            debitPoint = usepoint/0.02;
            debitWallet = usewallet;
            Log.d(TAG, "bookCar: "+debitPoint  + "\n"+debitWallet+"\n"+payableAmt);
            booking_point = debitPoint+"";
            debitPoint(booking, user_id,debitPoint+"");
            makeBooking(cate);
        } else if (usewallet>0){
            debitWallet = usewallet;
            debitPoint = usepoint;
            Log.d(TAG, "bookCar: "+debitPoint  + "\n"+debitWallet+"\n"+payableAmt);
            debitWallet(booking,user_id,debitWallet+"");
            makeBooking(cate);
        } else {
            debitPoint =usepoint;
            debitWallet = usewallet;
            Log.d(TAG, "bookCar: "+debitPoint  + "\n"+debitWallet+"\n"+payableAmt);
            makeBooking(cate);
        }
    }

    private String lastPointid, lastWalletId;

    public void makeBooking(String cateRequest){
        Log.d(TAG, "makeBooking: "+cateRequest);
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

                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "rakhi: "+e.getMessage());
                    String msg = e.getMessage();
                    Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
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
                                    // tested with manual supplier
                                   /*  String booking = null;
                                            try {
                                                JSONObject jsonObject1 = jsonObject.getJSONObject("response");

                                                booking = jsonObject1.getString("booking_id");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            if (couponvalue==100){
                                                earnPoint="0.0";
                                                creditPoint(booking,user_id,earnPoint);
                                            } else {
                                                if (booking!=null&&booking.length()>0){
                                                    creditPoint(booking,user_id,earnPoint);
                                                }
                                            }
*/

                                    runOnUiThread(() -> {
                                        if (jsonObject.has("bookingStatus")){
                                            try {
                                                if(jsonObject.getString("bookingStatus").equals("Confirmed")){
                                                    try {
                                                        String booking = jsonObject.getString("bookingId");
                                                        if (lastPointid!=null){
                                                            updatePointId(lastPointid,booking);
                                                        }
                                                        if (lastWalletId!=null){
                                                            updateWalletId(lastWalletId,booking);
                                                        }
                                                        if (couponvalue==100){
                                                            earnPoint="0.0";
                                                            creditPoint(booking,user_id,earnPoint);
                                                        } else {
                                                            if (booking!=null&&booking.length()>0){
                                                                creditPoint(booking,user_id,earnPoint);
                                                            }
                                                        }
                                                        if (extraData!=null){
                                                            extraData.clear();
                                                        }
                                                        Toast.makeText(Pay1Activity.this, "Booking success",
                                                                Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Pay1Activity.this, ThankYou.class).putExtra("bookingid", booking));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                } else if (jsonObject.getString("bookingStatus").equals("In process")){
                                                    if (jsonObject.has("error_msg")){
                                                        try {
                                                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                                            String booking = jsonObject1.getString("booking_id");
                                                            if (lastPointid!=null){
                                                                updatePointId(lastPointid,booking);
                                                            }
                                                            if (lastWalletId!=null){
                                                                updateWalletId(lastWalletId,booking);
                                                            }
                                                            if (couponvalue==100){
                                                                earnPoint="0.0";
                                                                creditPoint(booking,user_id,earnPoint);
                                                            } else {
                                                                if (booking!=null&&booking.length()>0){
                                                                    creditPoint(booking,user_id,earnPoint);
                                                                }
                                                            }
                                                            if (extraData!=null){
                                                                extraData.clear();
                                                            }
                                                            Toast.makeText(appGlobal, ""+jsonObject.has("error_msg"), Toast.LENGTH_SHORT).show();

                                                            startActivity(new Intent(Pay1Activity.this, ThankYou.class).putExtra("bookingid", booking));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } else if (jsonObject.getString("bookingStatus").equals("Fail")){
                                                    if (jsonObject.has("error_msg")){
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    setErrorDialog(jsonObject.getString("error_msg"));
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (jsonObject.getString("bookingStatus").equals("Fail")){
                                                    if (jsonObject.has("error_msg")){
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    setErrorDialog(jsonObject.getString("error_msg"));
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
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

    private Dialog dialog ;

    private void setErrorDialog(String msg){
        dialog = new Dialog(Pay1Activity.this);
        dialog.setContentView(R.layout.error_dialog);
        TextView txtMSg = dialog.findViewById(R.id.txtMsg);
        txtMSg.setText(msg);
        TextView txtOk = dialog.findViewById(R.id.error_dialog_txtOk);
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Pay1Activity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
                                totalDebit += debitAmt;
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
                        } else {
                            txtWalletBal.setVisibility(View.GONE);
                            txtCheckWallet.setVisibility(View.GONE);
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
                if (response!=null && response.body()!=null){
                    if ( response.body().status){
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

                                if (walletHistoryData1.getUse_point()==1){

                                    creditPoint = Double.parseDouble(debit);
                                    totalCreditPoint+= creditPoint;
                                } else {
                                    creditPoint = Double.parseDouble(debit);
                                    nonUsablePoint+= creditPoint;
                                }
                            }
                        }

                        Log.d(TAG, "onResponse: "+nonUsablePoint);
                        txtNonUsablePoint.setVisibility(View.VISIBLE);
                        txtNonUsablePoint.setText("Non usable points : "+nonUsablePoint);
                        totalPoint = totalCreditPoint/*-totalDebitPoint*/;
                        earnedPointValue = totalPoint*0.02;
//                     txtPointVal.setText(String.valueOf(totalPoint));
                        if (totalPoint>0){
                            txtcheckPoint.setVisibility(View.VISIBLE);
                            if (totalPoint>0.0){
                                txtPointVal.setText("("+ df2.format(totalPoint) +" "
                                        +getResources().getString(R.string.txtPointValue)+" is : "+curen
                                        /*+getResources().getString(R.string.txtPointValue)+" is : SAR "*/
                                        +Utility.convertCuurency(earnedPointValue, getApplicationContext())+")");
//                                booking_point = String.valueOf(earnedPointValue);
                                txtPointVal.setVisibility(View.VISIBLE);
                                txtcheckPoint.setVisibility(View.VISIBLE);
                            } else {
//                                booking_point = String.valueOf(earnedPointValue);
                                totalPoint = totalCreditPoint-totalDebitPoint;
                                txtcheckPoint.setVisibility(View.VISIBLE);
                                txtPointVal.setVisibility(View.VISIBLE);
                                txtPointVal.setText("("+String.valueOf(df2.format(totalPoint))+
                                        getResources().getString(R.string.txtPointValue)+"  is : "+curen
                                        +Utility.convertCuurency(earnedPointValue, getApplicationContext())+")");
                                txtcheckPoint.setClickable(false);
                                txtcheckPoint.setEnabled(false);
                            }
                        } else {
                            txtcheckPoint.setVisibility(View.GONE);

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
                        Toast.makeText(Pay1Activity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Pay1Activity.this, ""+response.body().msg, Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(Pay1Activity.this,ForgotPasswordActivity.class));
    }

    private TextView txtCountry;
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
                    startActivity(new Intent(Pay1Activity.this, SignUpActivity.class)
                            .putExtra("booking","booksign"));
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
            txtCountry = dialog.findViewById(R.id.countryList);
            edtLname = dialog.findViewById(R.id.etUserLastName);
            edtemail = dialog.findViewById(R.id.etUserEmail);
            edtFname.setText(userDetails.getUser_username());
            edtemail.setText(userDetails.getUser_email());
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

            final ArrayList<String>county = new ArrayList<>();
            county .addAll( SplashActivity.counrtyList);
            txtCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countryDialog(county);
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
                        license = " ";
                    }
                    if (!fname.isEmpty()) {
                        if (!lname.isEmpty()){
                            if (Utility.checkemail(email)){
                                if (Utility.checkphone(phone)){
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

    private Dialog dialogCountry;
    private void countryDialog(ArrayList<String> county){
        dialogCountry = new Dialog(Pay1Activity.this);
        dialogCountry.setContentView(R .layout.popup_country_code);
        dialogCountry.setCanceledOnTouchOutside(false);
        dialogCountry.setCancelable(false);
        dialogCountry.show();
        RecyclerView recyclerView = dialogCountry.findViewById(R.id.popupCountryRecycler);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        CountryAdapter countryAdapter =new CountryAdapter(county,getApplicationContext(),this);
        recyclerView.setAdapter(countryAdapter);
    }
    private String fname, lname, phone, zip, license, licenseorigin,  emaillogin, pass;

    private void updateProfile(String userid, String fname) {
        if (!Utility.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(Pay1Activity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
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
                    String st = appGlobal.getUser_id();
                    dialog.dismiss();
                    Utility.message(getApplicationContext(), response.body().msg);
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
            Toast.makeText(Pay1Activity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            return;
        }

        Utility.showloadingPopup(this);
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall = retroFitApis.login(user, pass);
        responseCall.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                Utility.hidepopup();
                if (response.body().status) {
                    UserDetails userDetails = new UserDetails();
                    userDetails = response.body().response.user_detail;
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

    long timeInMilliseconds;

    public void dob_pick() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = Calendar.getInstance().get(Calendar.YEAR) - 21;
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        timeInMilliseconds = Utility.getTimeDate(mYear + "-" + mMonth + "-" + mDay);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        txtDob.setText(Utility.convertSimpleDate(dob));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(timeInMilliseconds);
        datePickerDialog.show();

    }

    @Override
    public void onItemClickCategory(int position) {
        String item = SplashActivity.counrtyList.get(position);
        licenseorigin = (String) getKeyFromValue(SplashActivity.country,item);
        txtCountry.setText(item);
        dialogCountry.dismiss();
    }


}
