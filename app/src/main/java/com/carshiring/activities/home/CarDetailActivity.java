package com.carshiring.activities.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.carshiring.R;
import com.carshiring.adapters.Page_Adapter;
import com.carshiring.fragments.CarDetailTab1Fragment;
import com.carshiring.fragments.CarDetailTab2Fragment;
import com.carshiring.fragments.CarDetailTab3Fragment;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.CarDetailBean;
import com.carshiring.models.CoveragesBean;
import com.carshiring.models.ExtraBean;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarDetailActivity extends AppCompatActivity {
    String token="fa6e9fcf829fbf3217d8e361421acf2c588903aa";
    public static String type="0";
    public static String refer_type="16", day="2", car_type="";

    public static String id_context="6226549648764871585476470";
    TabLayout tabLayout;
    Page_Adapter adapter;
    ActionBar actionBar;
    TinyDB tinyDB ;
    double markUp;
    public static double point;
    public static String logo,oneway,driverSur,carPrice,carImage,modelname,currency,suppliername,suppliercity,termsurl
            ,fullprotectioncurrency,fullprotectionammount,fullProcted,time, driver_minage,driver_maxage,CDW,THP,carid;
    Gson gson = new Gson();
    public static double fullProAmt, fullAmtValue;
    public static ArrayList<ExtraBean>extralist;
    public static List<CarDetailBean.FeatureBean> carSpecificationList=new ArrayList<>();
    public static List<CoveragesBean>coveragelist;
    public static List<String> theft_protection=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        actionBar = getSupportActionBar() ;
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        extralist=new ArrayList<>();

        tinyDB = new TinyDB(getApplicationContext());
        token = tinyDB.getString("access_token");
        type = getIntent().getStringExtra("type");
        refer_type = getIntent().getStringExtra("refer_type");
        if (refer_type == null || refer_type.length()==0){
            refer_type ="0";
        }
        day = getIntent().getStringExtra("day");
        car_type = getIntent().getStringExtra("car_type");
        id_context = getIntent().getStringExtra("id_context");
        if (getIntent().hasExtra("point_earn")){
            String s = getIntent().getStringExtra("point_earn");
            if (s!=null){
                point = Double.parseDouble(s);
            }
        }

        if (getIntent().hasExtra("one_way_fee")){
            oneway = getIntent().getStringExtra("one_way_fee");
        }
        if (getIntent().hasExtra("driverSur")){
            driverSur = getIntent().getStringExtra("driverSur");
        }

//        call api

//        setupApi();
        getCarDetail();
    }


    JSONArray collision_damage_waiver;

    private void getCarDetail(){

        Utility.showloadingPopup(this);
        coveragelist=new ArrayList<>();
        String url = RetrofitApiBuilder.CarGates_BASE_WEBSERVICE_URL + "webservice/car_detail_api";
        final CarDetailBean carDetailBean = new CarDetailBean();
        StringRequest  stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            String s =response;
            Log.d("TAG", "onResponse: details"+response);
            Utility.hidepopup();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status")){
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        JSONObject responseObject = jsonObject.getJSONObject("response");
                        JSONObject car_detailObject = responseObject.getJSONObject("car_detail");
                        JSONObject featureObject= null;
                        if (car_detailObject.has("feature")){
                            CarDetailBean.FeatureBean featureBean = new CarDetailBean.FeatureBean();

                            featureObject = car_detailObject.getJSONObject("feature");
                            if (featureObject.has("aircondition")){
                                String aircondition = featureObject.getString("aircondition");
                                featureBean.setAircondition(aircondition);
                            }
                            if (featureObject.has("transmission")){
                                String transmission = featureObject.getString("transmission");
                                featureBean.setTransmission(transmission);
                            }
                            if (featureObject.has("fueltype")){
                                String fueltype = featureObject.getString("fueltype");
                                featureBean.setFueltype(fueltype);
                            }
                            if (featureObject.has("bag")){
                                String bag = featureObject.getString("bag");
                                featureBean.setBag(bag);
                            }
                            if (featureObject.has("door")){
                                String door = featureObject.getString("door");
                                featureBean.setDoor(door);
                            }
                            if (featureObject.has("passenger")){
                                String passenger = featureObject.getString("passenger");
                                featureBean.setPassenger(passenger);
                            } else if (featureObject.has("PassengerQuantity")){
                                String passenger = featureObject.getString("PassengerQuantity");
                                featureBean.setPassenger(passenger);
                            }

                            carSpecificationList= Arrays.asList(featureBean);

                            carDetailBean.setFeature(featureBean);
                        }
                        String category = "",model_code="",opening_hours_start="",opening_hours_end="";
                        if (car_detailObject.has("category"))
                            category = car_detailObject.getString("category");
                        if (car_detailObject.has("model"))
                            modelname = car_detailObject.getString("model");
                        if ( car_detailObject.has("model_code"))
                            model_code = car_detailObject.getString("model_code");
                        if (car_detailObject.has("image"))
                            carImage = car_detailObject.getString("image");
                        if (car_detailObject.has("price")){
                            String price = car_detailObject.getString("price");
                            markUp = Double.parseDouble(SearchCarFragment.markup);
                            double d = Double.parseDouble(price);
                            double priceNew  = d+(d*markUp)/100;
                            String curen = tinyDB.getString("from_currency");
                            currency = curen;
                            carPrice= priceNew+"";
                            carDetailBean.setPrice(price);

                        }
                     /*   if (car_detailObject.has("currency"))
                            currency = car_detailObject.getString("currency");*/
                        if (car_detailObject.has("time_unit"))
                            day = car_detailObject.getString("time_unit");
                        if (car_detailObject.has("time"))
                            time = car_detailObject.getString("time");
                        if (car_detailObject.has("driver_min_age"))
                            driver_minage = car_detailObject.getString("driver_min_age");
                        if (car_detailObject.has("driver_max_age"))
                            driver_maxage = car_detailObject.getString("driver_max_age");
                        if (car_detailObject.has("opening_hours_start")) {
                            opening_hours_start = car_detailObject.getString("opening_hours_start");
                        }
                        if (car_detailObject.has("opening_hours_end")){
                            opening_hours_end = car_detailObject.getString("opening_hours_end");
                        }

                        if (car_detailObject.has("theft_protection")&&car_detailObject.getString("theft_protection").length()>0) {
                            Object json = new JSONTokener(car_detailObject.getString("collision_damage_waiver")).nextValue();
                            if (json instanceof String) {
                                String collision_damage_waiver = car_detailObject.getString("collision_damage_waiver");
                            } else if (json instanceof JSONArray) {
                                collision_damage_waiver = car_detailObject.getJSONArray("collision_damage_waiver");
                            }
                        }

                        carDetailBean.setCategory(category);
                        carDetailBean.setModel(modelname);
                        carDetailBean.setModel_code(model_code);
                        carDetailBean.setImage(carImage);
                        carDetailBean.setCurrency(currency);
                        carDetailBean.setTime(time);
                        carDetailBean.setTime_unit(day);
                        carDetailBean.setDriver_min_age(driver_minage);
                        carDetailBean.setDriver_max_age(driver_maxage);
                        carDetailBean.setOpening_hours_start(opening_hours_start);
                        carDetailBean.setOpening_hours_end(opening_hours_end);
                        carDetailBean.setTime(time);
                        List<String>collosion = new ArrayList<>();
                        List<String>theft_protectionList = new ArrayList<>();
                        if (collision_damage_waiver!=null){
                            for (int i=0;i<collision_damage_waiver.length();i++){
                                CDW = collision_damage_waiver.getString(i);
                                collosion.add(collision_damage_waiver.getString(i));
                            }
                        }

                        carDetailBean.setCollision_damage_waiver(collosion);
                        if (car_detailObject.has("theft_protection")&&car_detailObject.getString("theft_protection").length()>0){
                            Object json = new JSONTokener(car_detailObject.getString("theft_protection")).nextValue();
                            if (json instanceof String){
                                String theft_protection = car_detailObject.getString("theft_protection");
                            }
                            else if (json instanceof JSONArray){
                                JSONArray theft_protectionjsonArray = car_detailObject.getJSONArray("theft_protection");
                                for (int i=0;i<theft_protectionjsonArray.length();i++){
                                    theft_protectionList.add(theft_protectionjsonArray.getString(i));
                                    THP = theft_protectionjsonArray.getString(i);
                                }
                                carDetailBean.setTheft_protection(theft_protectionList);
                                theft_protection.addAll(theft_protectionList);
                            }
                        }

                        if ( car_detailObject.has("deposit_currency")){
                            String deposit_currency = car_detailObject.getString("deposit_currency");
                            carDetailBean.setDeposit_currency(deposit_currency);
                        }
                        if (car_detailObject.has("deposit_price")){
                            String deposit_price = car_detailObject.getString("deposit_price");
                            carDetailBean.setDeposit_price(deposit_price);
                        }
                        if (car_detailObject.has("deposit_desc")){
                            String deposit_desc = car_detailObject.getString("deposit_desc");
                            carDetailBean.setDeposit_desc(deposit_desc);
                        }
                        if (car_detailObject.has("deposit_name")){
                            String deposit_name = car_detailObject.getString("deposit_name");
                            carDetailBean.setDeposit_name(deposit_name);
                        }
                        if (car_detailObject.has("fullprotection_amount")&& car_detailObject
                                .getString("fullprotection_amount")!=null){
                            fullprotectionammount = car_detailObject.getString("fullprotection_amount")+"";
                            if (fullprotectionammount!=null&&!fullprotectionammount.equalsIgnoreCase("null")){
                                if (fullprotectionammount.length()>0)
                                    try {
                                        fullProAmt = Double.parseDouble(fullprotectionammount);
                                        fullAmtValue = Double.parseDouble(df2.format(fullProAmt));

                                    } catch (Exception e){
                                        Log.d("TAG", "onResponse: "+e.getMessage());
                                    }

                                //   double du = fullProAmt*3.75;
                                //  fullAmtValue = Double.parseDouble(Utility.convertCuurency(fullProAmt, getApplicationContext()));

                            }
                        }

                        if (car_detailObject.has("fullprotection_currency")&&
                                car_detailObject.getString("fullprotection_currency")!=null){
                            fullprotectioncurrency="";
                            fullprotectioncurrency=car_detailObject.getString("fullprotection_currency");
                            tinyDB.putString("full_prot", fullprotectioncurrency+" "+String.valueOf(fullAmtValue));
                            carDetailBean.setFullprotection_currency(fullprotectioncurrency);
                            carDetailBean.setFullprotection_amount(fullprotectionammount);
                        }

                        if ( car_detailObject.has("supplier"))
                            suppliername = car_detailObject.getString("supplier");
                        if (car_detailObject.has("supplier_city"))
                            suppliercity = car_detailObject.getString("supplier_city");
                        if (car_detailObject.has("supplier_logo"))
                            logo = car_detailObject.getString("supplier_logo");
                        String drop_city = car_detailObject.getString("drop_city");
                        if (car_detailObject.has("tc"))
                            termsurl = car_detailObject.getString("tc");
                        JSONArray extraJsonArray = null;
                        if ( car_detailObject.has("extra")){
                            extraJsonArray = car_detailObject.getJSONArray("extra");
                            for (int i =0; i<extraJsonArray.length();i++){
                                ExtraBean extraBean = new ExtraBean();
                                JSONObject jsonObject1 = extraJsonArray.getJSONObject(i);
                                if (jsonObject1.getString("name")!=null){
                                    extraBean.setName(jsonObject1.getString("name"));
                                    extraBean.setCurrency(jsonObject1.getString("currency"));
                                    extraBean.setPrice(jsonObject1.getString("price"));
                                    extraBean.setType(jsonObject1.getString("type"));
                                    if (jsonObject1.has("charg_type"))
                                    extraBean.setCharg_type(jsonObject1.getString("charg_type"));
                                    extralist.add(extraBean);
                                }
                            }

                            try {
                                if (extralist!=null && extralist.size()>0){
                                    for (int i=0; i<extralist.size();i++){
                                        if (extralist.get(i).name!=null){
                                            if (extralist.get(i).getName().equalsIgnoreCase("Full Protection")){
                                                extralist.remove(i);
                                            }
                                        }
                                    }

                                    for (int i =0;i<extralist.size();i++){

                                        if (extralist.get(i).getCharg_type().equalsIgnoreCase("per-day")){
                                            double price = Double.parseDouble(extralist.get(i).getPrice())
                                                    *Integer.parseInt(time);
                                            extralist.get(i).setPrice(price+"");
                                        }
                                    }
                                }

                                carDetailBean.setExtra(extralist);
                            } catch (Exception e){
                                Log.d("TAG", "getCarDetail: "+e.getMessage());
                            }

                        }

                        if (car_detailObject.has("coverages")){
                            JSONArray coveragesArray = car_detailObject.getJSONArray("coverages");
                            List<CoveragesBean>coveragesBeans = new ArrayList<>();
                            for (int i=0;i<coveragesArray.length();i++){
                                CoveragesBean coveragesBean = new CoveragesBean();
                                JSONObject jsonObject1 = coveragesArray.getJSONObject(i);
                                coveragesBean.setAmount(jsonObject1.getString("amount"));
                                if (jsonObject1.has("currency"))
                                coveragesBean.setCurrency(jsonObject1.getString("currency"));
                                if (jsonObject1.has("desc"))
                                coveragesBean.setDesc(jsonObject1.getString("desc"));
                                coveragesBean.setName(jsonObject1.getString("name"));
                                coveragelist.add(coveragesBean);
                                carDetailBean.setCoverages(coveragesBeans);
                            }
                        }

                        carDetailBean.setSupplier(suppliername);
                        carDetailBean.setSupplier_city(suppliercity);
                        carDetailBean.setSupplier_logo(logo);
//                        carDetailBean.setd(drop_city);
                        carDetailBean.setTc(termsurl);

                        handletablayout();

                        Log.d("TAG", "onResponse: carsdta"+gson.toJson(carDetailBean));
                    }
                } else {
                    Toast.makeText(CarDetailActivity.this, "No", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.d("TAG", "Excepti: "+e.getMessage());
                e.printStackTrace();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onFailure: detail"+error);
                Utility.hidepopup();
                Utility.message(getApplicationContext(), getResources().getString(R.string.no_internet_connection));
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String>cateRequest = new HashMap<>();
                cateRequest.put("access_token",token);
                cateRequest.put("id_context",id_context);
                cateRequest.put("type",type);
                cateRequest.put("day",day);
                cateRequest.put("refer_type",refer_type);
                cateRequest.put("car_type",car_type);
                cateRequest.put("pick_city_id",SearchCarFragment.pick_city_id);
                cateRequest.put("drop_city_id",SearchCarFragment.drop_city_id);
                cateRequest.put("pick_date",SearchCarFragment.pick_date);
                cateRequest.put("driver_age",SearchCarFragment.driver_age);
                cateRequest.put("language_code",SearchCarFragment.languagecode);

                Log.d("TAG", "getParams: "+cateRequest);
                return cateRequest;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private static DecimalFormat df2 = new DecimalFormat(".##");

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.car_details));
        setupToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                CarsResultListActivity.driverSurCharge = null;
                CarsResultListActivity.oneWayFee = null;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handletablayout() {
        Utility.hidepopup();
        View view1=getLayoutInflater().inflate(R.layout.tabstyle,null);
        View view2=getLayoutInflater().inflate(R.layout.tabstyle,null);
        View view3=getLayoutInflater().inflate(R.layout.tabstyle,null);
        view1.findViewById(R.id.img_tab).setBackgroundResource(R.mipmap.ic_launcher);
        ImageView tab2 = view2.findViewById(R.id.img_tab);
        Glide.with(getApplicationContext()).load(logo).into(tab2);
        view3.findViewById(R.id.img_tab).setBackgroundResource(R.drawable.ic_tab3);
        tabLayout = findViewById(R.id.cardet_Tab);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        CarDetailTab1Fragment tab1Fragment=new CarDetailTab1Fragment();
        CarDetailTab2Fragment tab2Fragment=new CarDetailTab2Fragment();
        CarDetailTab3Fragment tab3Fragment=new CarDetailTab3Fragment();
        final ViewPager pager = findViewById(R.id.cardet_viewpager);
        adapter=new Page_Adapter(getSupportFragmentManager(),tabLayout.getTabCount(),tab1Fragment,tab2Fragment,tab3Fragment);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.bottomToolBar);
        TextView textView = toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.book_this_car));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(CarDetailActivity.this, BookCarActivity.class);
                it.putExtra("get","FromActi");
                startActivity(it);
            }
        });
    }



}
