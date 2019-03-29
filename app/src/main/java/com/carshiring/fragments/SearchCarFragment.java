package com.carshiring.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carshiring.R;
import com.carshiring.activities.home.CarsResultListActivity;
import com.carshiring.activities.home.LocationSelectionActivity;
import com.carshiring.activities.home.MainActivity;
import com.carshiring.activities.home.SearchQuery;
import com.carshiring.activities.home.SearchbyMapActivity;
import com.carshiring.interfaces.IRefreshToken;
import com.carshiring.models.Category;

import com.carshiring.models.MArkupdata;
import com.carshiring.models.MapData;
import com.carshiring.models.Point;
import com.carshiring.models.SearchData;
import com.carshiring.models.TestData;
import com.carshiring.models.UseLocationData;
import com.carshiring.models.UserDetails;
import com.carshiring.splash.SplashActivity;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.Location;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mukesh.tinydb.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchCarFragment extends BaseFragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, IRefreshToken {
    private static final int REQUEST_PICKUP_LOCATION = 500;
    private static final int REQUEST_DESTINATION_LOCATION = 501;
    private static final int REQUEST_BY_MAP_LOCATION = 502;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private Location location = new Location();
    private EditText et_return_location, et_pickup_location, et_driver_age;

    private MainActivity activity;
    private SearchQuery searchQuery;
    private SwitchCompat switchSameDestLocation, switchDriverAge;
    private GoogleApiClient mgoogleApiclient;
    private CheckBox chkUseCurrentLocation;
    private TinyDB tinyDB;
    private Calendar calendar_pick, calendar_drop;

    public static String pickName = "", dropName = "", drop_date = "", pick_date = "",
            drop_hour = "", driver_age = "", location_type="",drop_minute = "",
            pick_minute = "", markup = "", pointper = "", pick_hour = "", pickTime = "", dropTime = "",
            drop_city_id="",pick_city_id="",languagecode="";

    private int useCurrentLocation = 0;
    private int useSameDestLocation = 1, isBetweenDriverAge;
    int pick_hours, pick_minutes, drop_hours, drop_minutes;
    private double currentLat, currentLng;
    private String token, location_code = "", location_iata = "", location_code_drop = "", location_iata_drop = "",
            location_type_drop = "", country_code = " ", city = "", user_age;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_car, container, false);

        init();
        return view;
    }

    private void init() {
        tinyDB = new TinyDB(getActivity().getApplicationContext());
        languagecode = tinyDB.getString("language_code");
        if (tinyDB.contains("login_data")) {
            String log = tinyDB.getString("login_data");
            UserDetails userDetails;
            userDetails = gson.fromJson(log, UserDetails.class);
            if (userDetails.getUser_age() != null && !userDetails.getUser_age().equals("0")) {
                driver_age = userDetails.getUser_age();
            } else {
                driver_age = "30";
            }
        } else {
            driver_age = "30";
        }
        et_return_location = view.findViewById(R.id.et_return_location);
        et_return_location.setOnClickListener(this);
        et_pickup_location = view.findViewById(R.id.et_pickup_location);

        et_pickup_location.setOnClickListener(this);

        et_driver_age = view.findViewById(R.id.et_driver_age);

        final TextView btn_search_car = view.findViewById(R.id.btn_search_car);
        btn_search_car.setOnClickListener(this);

        switchSameDestLocation = (SwitchCompat) view.findViewById(R.id.switchSameDestLocation);
        switchSameDestLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et_return_location.setVisibility(View.GONE);

                } else {
                    et_return_location.setVisibility(View.VISIBLE);
                }
            }
        });

        switchDriverAge = (SwitchCompat) view.findViewById(R.id.switchDriverAge);
        switchDriverAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (tinyDB.contains("login_data")) {
                        String log = tinyDB.getString("login_data");
                        userDetails = gson.fromJson(log, UserDetails.class);
                        if (userDetails.getUser_age() != null && !userDetails.getUser_age().equals("0")) {
                            driver_age = userDetails.getUser_age();
                            user_age = userDetails.getUser_age();
                        } else {
                            driver_age = "30";
                        }
                    } else {
                        driver_age = "30";
                    }
                    et_driver_age.setVisibility(View.GONE);
                } else {
                    if (tinyDB.contains("login_data")) {
                        et_driver_age.setText(driver_age);
                    } else et_driver_age.setText("");
                    et_driver_age.setVisibility(View.VISIBLE);
                }
            }
        });

        final LinearLayout dt_picker_journey = view.findViewById(R.id.dt_picker_journey);
        final TextView tvJourneyDatePicker = dt_picker_journey.findViewById(R.id.tvDateDayValue);
        TextView txtPic = dt_picker_journey.findViewById(R.id.lblDate);
        txtPic.setText(getResources().getString(R.string.pickup));
        tvJourneyDatePicker.setOnClickListener(view -> showDataPicker("journey"));
        final TextView tvJourneyFullTimePicker = dt_picker_journey.findViewById(R.id.tvFullTime);
        tvJourneyFullTimePicker.setOnClickListener(view -> showTimePicker("journey"));
        final LinearLayout dt_picker_returning = view.findViewById(R.id.dt_picker_returning);
        final TextView tvReturningDatePicker = dt_picker_returning.findViewById(R.id.tvDateDayValue);
        final TextView txtDrop = dt_picker_returning.findViewById(R.id.lblDate);
        txtDrop.setText(getResources().getString(R.string.dropage));
        tvReturningDatePicker.setOnClickListener(view -> showDataPicker("returning"));
        final TextView tvReturningFullTimePicker = dt_picker_returning.findViewById(R.id.tvFullTime);
        tvReturningFullTimePicker.setOnClickListener(view -> showTimePicker("returning"));

        chkUseCurrentLocation = view.findViewById(R.id.chkUseCurrentLocation);
        chkUseCurrentLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //comment for testing
            if (isChecked) {
                et_pickup_location.setText("");
                et_pickup_location.setEnabled(false);
                if (Utility.checkGooglePlayService(getActivity()))
                    setupLocation();
            } else {
                et_pickup_location.setEnabled(true);
                et_pickup_location.setText("");
            }
        });
    }

    private  UserDetails userDetails = new UserDetails();

    private void showTimePicker(final String type) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                (timePicker, i, i1) -> bindTimeToGUI(type, i, i1), hour, minute, false);
        timePickerDialog.show();
    }

    private void bindTimeToGUI(String type, int i, int i1) {
        String timeStr = setFullTime(i, i1);
        DateFormat monthFormat = new SimpleDateFormat("HH:mm", Locale.US);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        String output = null;
        try {
            //Converting the input String to Date
            date = monthFormat.parse(timeStr);
            //Changing the format of date and storing it in String
            output = dateFormat.format(date);
            //Displaying the date
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        calendar_drop.set(calendar_drop.get(Calendar.YEAR), calendar_drop.get(Calendar.MONTH),
                calendar_drop.get(Calendar.DAY_OF_MONTH), i, i1);
        switch (type) {
            case "journey":
                pick_hours = i;
                pick_minutes = i1;
                final LinearLayout dt_picker_journey = view.findViewById(R.id.dt_picker_journey);
                final TextView tvPickFullTime = (TextView) dt_picker_journey.findViewById(R.id.tvFullTime);
                tvPickFullTime.setText(getResources().getString(R.string.txtTime) + " : " + output);
                pickTime = timeStr;
                calendar_pick.set(calendar_pick.get(Calendar.YEAR), calendar_pick.get(Calendar.MONTH),
                        calendar_pick.get(Calendar.DAY_OF_MONTH), i, i1);
                bindTimeToGUI("returning", calendar_drop.get(Calendar.HOUR_OF_DAY), calendar_drop.get(Calendar.MINUTE));
                break;

            case "returning":
                drop_hours = i;
                drop_minutes = i1;
                final LinearLayout dt_picker_returning = view.findViewById(R.id.dt_picker_returning);
                final TextView tvDropFullTime = (TextView) dt_picker_returning.findViewById(R.id.tvFullTime);
                dropTime = timeStr;
                tvDropFullTime.setText(getResources().getString(R.string.txtTime) + " : " + output);
                break;
        }
    }

    private String setFullTime(int hoursOfDay, int minutes) {

        String result;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 3, 1, hoursOfDay, minutes);
//        SimpleDateFormat monthFormat = new SimpleDateFormat("h:mm:a", Locale.US);
        SimpleDateFormat monthFormat = new SimpleDateFormat("HH:mm", Locale.US);
        result = monthFormat.format(calendar.getTime());
        return result;
    }

    private void showDataPicker(final String type) {
        Calendar calendar = calendar_pick;
        if (type.equals("returning")) {
            calendar = calendar_drop;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (datePicker, i, i1, i2) -> bindDateToGUI(type, i, i1, i2), calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void bindDateToGUI(String type, int i, int i1, int i2) {

        String dayMon = setFullDate(i, i1, i2);
        final String dateValueString = setValueFullDate(i, i1, i2);
        switch (type) {
            case "journey":
                final LinearLayout dt_picker_journey = view.findViewById(R.id.dt_picker_journey);
                final TextView tvJourneyDatePicker = dt_picker_journey.findViewById(R.id.tvDateDayValue);
                final TextView tvJourneyDateDayNameWithMonthName = dt_picker_journey.findViewById(R.id.tvDateDayNameWithMonthName);

                calendar_pick.set(i, i1, i2);
                tvJourneyDatePicker.setText(String.valueOf(i2));
                tvJourneyDateDayNameWithMonthName.setText(dayMon);
                pick_date = dateValueString;
                calendar_drop = (Calendar) calendar_pick.clone();
                calendar_drop.add(Calendar.DATE, 2);

                bindDateToGUI("returning", calendar_drop.get(Calendar.YEAR), calendar_drop.get(Calendar.MONTH), calendar_drop.get(Calendar.DAY_OF_MONTH));
                break;

            case "returning":
                final LinearLayout dt_picker_returning = view.findViewById(R.id.dt_picker_returning);
                final TextView tvReturningDatePicker = dt_picker_returning.findViewById(R.id.tvDateDayValue);
                final TextView tvReturningDateDayNameWithMonthName = dt_picker_returning.findViewById(R.id.tvDateDayNameWithMonthName);
                tvReturningDatePicker.setText(String.valueOf(i2));
                tvReturningDateDayNameWithMonthName.setText(dayMon);
                drop_date = dateValueString;
                calendar_drop.set(i, i1, i2);
                break;
        }

    }

    private String setValueFullDate(int year, int month, int day) {
        String result;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        result = monthFormat.format(calendar.getTime());
        return result;
    }

    private String setFullDate(int year, int month, int day) {
        String result;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat monthFormat = new SimpleDateFormat("EEE | MMMM", Locale.US);
        result = monthFormat.format(calendar.getTime());
        return result;
    }

    @Override
    public void setDefaultSettings() {

        activity = (MainActivity) getActivity();
        searchQuery = activity.searchQuery;

        et_driver_age.setVisibility(View.GONE);
        et_return_location.setVisibility(View.GONE);
        et_return_location.setInputType(InputType.TYPE_NULL);
        et_return_location.setFocusable(false);

        et_pickup_location.setInputType(InputType.TYPE_NULL);
        et_pickup_location.setFocusable(false);

        switchSameDestLocation = view.findViewById(R.id.switchSameDestLocation);
        switchSameDestLocation.setChecked(searchQuery.isDestAsPickup);

        switchDriverAge = view.findViewById(R.id.switchDriverAge);
        switchDriverAge.setChecked(searchQuery.isDriverAged);

        final SwitchCompat switchSearchByMap = view.findViewById(R.id.switchSearchByMap);
        switchSearchByMap.setChecked(searchQuery.isSearchByMap);

        calendar_pick = Calendar.getInstance();
        calendar_pick.add(Calendar.DATE, 2);
        bindDateToGUI("journey", calendar_pick.get(Calendar.YEAR), calendar_pick.get(Calendar.MONTH), calendar_pick.get(Calendar.DAY_OF_MONTH));
        bindTimeToGUI("journey", 10, 15/* calendar_pick.get(Calendar.HOUR_OF_DAY), calendar_pick.get(Calendar.MINUTE)*/);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = MainActivity.toolbar;
        toolbar.setTitle(getResources().getString(R.string.action_search_car));
        getMarkUp();

//        checkGPSStatus();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Preserve Data
        switchSameDestLocation = view.findViewById(R.id.switchSameDestLocation);
        searchQuery.isDestAsPickup = switchSameDestLocation.isChecked();

        switchDriverAge = view.findViewById(R.id.switchDriverAge);
        searchQuery.isDriverAged = switchDriverAge.isChecked();

        final SwitchCompat switchSearchByMap = view.findViewById(R.id.switchSearchByMap);
        searchQuery.isSearchByMap = switchSearchByMap.isChecked();

        activity.searchQuery = searchQuery;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_pickup_location:
                startActivityForResult(new Intent(getActivity(), LocationSelectionActivity.class), REQUEST_PICKUP_LOCATION);
                break;

            case R.id.et_return_location:
                startActivityForResult(new Intent(getActivity(), LocationSelectionActivity.class), REQUEST_DESTINATION_LOCATION);
                break;

            case R.id.btn_search_car:
//                requestForSearchCar();
                requestForSearchCar1();

        }
    }

    private Gson gson = new Gson();
    private String TAG = SearchCarFragment.class.getName();

    public static List<TestData> catBeanList = new ArrayList<>();
    public static List<MapData> mapDataList = new ArrayList<>();

    public static Category category = new Category();

    private void chooseSearchAction(List<SearchData> car_list) {

        final SwitchCompat switchSearchByMap = view.findViewById(R.id.switchSearchByMap);
        Intent intent;
        if (switchSearchByMap.isChecked()) {
            intent = new Intent(getActivity(), SearchbyMapActivity.class);
            startActivity(intent);
        } else {
            getPoint();
        }
    }

    public static List<SearchData> searchData = new ArrayList<>();
    private boolean status;
    public static HashMap<String, List<String>> catPriceMap;
    private SearchData.FeatureBean feature;
    private String category1, model, model_code, passanger_number = "", image, price,time_unit,car_type,
            time, id_context, refer_type, msg, supplier, supplier_city, supplier_logo, drop_city, tc, type;

    private List<SearchData.CoveragesBean> coverages;

    private void requestForSearchCar1() {
        token = tinyDB.getString("access_token");

        if (!validateData()) {
            return;
        }
        if (searchData != null) {
            searchData.clear();
        }

        catPriceMap = new HashMap<>();
        Utility.showLoading(getActivity(), getResources().getString(R.string.searching_cars));

        pick_hour = String.valueOf(pick_hours > 9 ? pick_hours : "0" + pick_hours);
        pick_minute = String.valueOf(pick_minutes > 9 ? pick_minutes : "0" + pick_minutes);

        drop_minute = String.valueOf(drop_minutes > 9 ? drop_minutes : "0" + drop_minutes);
        drop_hour = String.valueOf(drop_hours > 9 ? drop_hours : "0" + drop_hours);

        Log.d(TAG, "requestForSearchCar: " + token + "\n" + pickName + "\n" +
                pick_date + "\n" + pick_hour + "\n" +
                pick_minute + "\n" + dropName + "\n" + drop_date + "\n" + drop_hour + "\n" + drop_minute + "\n" + driver_age + "\n" +
                useCurrentLocation + "\n" + useSameDestLocation + "\n" + isBetweenDriverAge + "\n" + currentLat + "\n" +
                currentLng + "\n" + location_code + "\n" + location_iata + "\n" +
                location_type + "\n" + location_code_drop + "\n" + location_iata_drop + "\n" + location_type_drop + "\n" + languagecode);

//        String url = "https://carsgates.com/demo/webservice/mapsearch"; // for demo
        String url = RetrofitApiBuilder.CarGates_BASE_WEBSERVICE_URL + "webservice/mapsearch"; // for live

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                response -> {
                    Utility.hidepopup();
                    Log.d(TAG, "rakhi: " + response);
                    try {
                        /*{"response_code":102,"status":false,"msg":"invalid token or expired."}*/
                        JSONObject jsonObject = new JSONObject(response);
                        status = jsonObject.getBoolean("status");
                        if (jsonObject.has("msg")) {
                            msg = jsonObject.getString("msg");
                        }
                        if (jsonObject.getInt("response_code") != 102) {
                            if (status) {
                                if (catBeanList != null) {
                                    catBeanList.clear();
                                }
                                JSONObject responseObject = jsonObject.getJSONObject("response");
                                JSONObject car_listObject = responseObject.getJSONObject("car_list");
                                String mapDataArray = jsonObject.getString("map_data");
                                if (mapDataList != null)
                                    mapDataList.clear();
                                if (mapDataArray != null) {
                                    Type listType1 = new TypeToken<List<MapData>>() {
                                    }.getType();
                                    mapDataList = gson.fromJson(mapDataArray, listType1);
                                }
                                if (car_listObject != null && car_listObject.length() > 0) {
                                    Iterator<String> iter = car_listObject.keys();
                                    while (iter.hasNext()) {
                                        String key = iter.next();
                                        try {
                                            Object value = car_listObject.get(key);
                                            if (value instanceof JSONArray) {
                                                if (key.equals("category_list")) {
                                                    // It's an array
                                                    JSONArray jsonArray = (JSONArray) value;
                                                    for (int i =0; i<jsonArray.length();i++){
                                                        TestData testData = new TestData();
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        testData.setCat_id(jsonObject1.getString("cat_id"));
                                                        testData.setCat_min_price(jsonObject1.getString("cat_min_price"));
                                                        testData.setCat_name(jsonObject1.getString("cat_name"));
                                                        List<String>code = new ArrayList<>();
                                                        JSONArray jsonArray1 = jsonObject1.getJSONArray("cat_code");
                                                        for (int j=0; j<jsonArray1.length();j++){
                                                            code.add(jsonArray1.getString(j));
                                                        }
                                                        testData.setCat_code(code);
                                                        catBeanList.add(testData);
                                                    }
                                                    List<TestData> allEvents = catBeanList;
                                                            List<TestData> noRepeat = new ArrayList<>();
                                                    for (TestData event : allEvents) {
                                                        boolean isFound = false;
                                                        // check if the event name exists in noRepeat
                                                        for (TestData e : noRepeat) {
                                                            if (e.getCat_id().equals(event.getCat_id()) || (e.equals(event))) {
                                                                isFound = true;
                                                                break;
                                                            }
                                                        }
                                                        if (!isFound) noRepeat.add(event);
                                                    }
                                                    if (catBeanList!=null)
                                                        catBeanList.clear();
                                                    catBeanList.addAll(noRepeat);
                                                    Log.d(TAG, "noRepeat: "+gson.toJson(noRepeat));
                                                }
                                            }
                                            else if (value instanceof JSONObject){
                                                if (key.equals("price_list1")){

                                                } else if (key.equals("size_list")){

                                                } else {
                                                    JSONObject object = (JSONObject) value;
                                                    if (object.has("feature")) {
                                                        feature = new SearchData.FeatureBean();
                                                        JSONObject featureObject = object.getJSONObject("feature");
                                                        feature.setAircondition(featureObject.get("aircondition") + "");
                                                        feature.setBag(featureObject.get("bag") + "");
                                                        feature.setFueltype(featureObject.getString("fueltype"));
                                                        feature.setTransmission(featureObject.getString("transmission"));
                                                        feature.setDoor(featureObject.getString("door"));
                                                        if (featureObject.has("PassengerQuantity")) {
                                                            passanger_number = featureObject.getString("PassengerQuantity");
                                                            feature.setPassengerQuantity(passanger_number);
                                                        }
                                                    }
                                                    if (object.has("category"))
                                                        category1 = (String) object.get("category");
                                                    if (object.has("model"))
                                                        model = object.getString("model");
                                                    if (object.has("model_code"))
                                                        model_code = object.getString("model_code");
                                                    if (object.has("image"))
                                                        image = (String) object.get("image");
                                                    if (object.has("price"))
                                                        price = object.getString("price");
                                                    if (object.has("time_unit"))
                                                        time_unit = object.getString("time_unit");
                                                    if (object.has("time"))
                                                        time = object.getString("time");
                                                    if (object.has("id_context"))
                                                        id_context = (String) object.get("id_context");
                                                    if (object.has("refer_type"))
                                                        refer_type = object.getString("refer_type");
                                                    if (object.has("supplier"))
                                                        supplier = object.getString("supplier");
                                                    if (object.has("supplier_city"))
                                                        supplier_city = (String) object.get("supplier_city");
                                                    if (object.has("supplier_logo"))
                                                        supplier_logo = object.getString("supplier_logo");
                                                    if ( object.has("drop_city"))
                                                        drop_city = object.getString("drop_city");
                                                    if (object.has("tc"))
                                                        tc = (String) object.get("tc");
                                                    if (object.has("type"))
                                                        type = (String) object.get("type");
                                                    JSONArray coveragesArray = null;
                                                    if (object.has("coverages"))
                                                    {
                                                        coveragesArray = object.getJSONArray("coverages");
                                                        if (coveragesArray!=null && coveragesArray.length()>0){
                                                            coverages = new ArrayList<>();
                                                            for (int i = 0; i < coveragesArray.length(); i++) {
                                                                SearchData.CoveragesBean bean = new SearchData.CoveragesBean();
                                                                JSONObject jsonObject1 = (JSONObject) coveragesArray.get(i);
                                                                String code = "", name="",desc="";
                                                                if (jsonObject1.has("code")){
                                                                    code = jsonObject1.getString("code");
                                                                }
                                                                if (jsonObject1.has("name")){
                                                                    name = jsonObject1.getString("name");
                                                                }
                                                                if (jsonObject1.has("desc"))
                                                                    desc = jsonObject1.getString("desc");
                                                                String amount2 = "", currency2 = "";
                                                                if (jsonObject1.has("amount2"))
                                                                    amount2 = jsonObject1.getString("amount2");
                                                                if (jsonObject1.has("currency2"))
                                                                    currency2 = jsonObject1.getString("currency2");

                                                                if (code != null) {
                                                                    bean.setCode(code);
                                                                }
                                                                if (name != null) {
                                                                    bean.setName(name);
                                                                }
                                                                if (currency2 != null) {
                                                                    bean.setCurrency2(currency2);
                                                                }
                                                                if (desc != null) {
                                                                    bean.setDesc(desc);
                                                                }
                                                                if (amount2 != null) {
                                                                    bean.setAmount2(amount2);
                                                                }
                                                                coverages.add(bean);
                                                            }
                                                        }
                                                    }

                                                    if (object.has("car_type")){
                                                        car_type = object.getString("car_type");
                                                    } else {
                                                        car_type="";
                                                    }
                                                    SearchData carData = new SearchData();
                                                    carData.setCar_type(car_type);
                                                    carData.setFeature(feature);
                                                    carData.setCategory(category1);
                                                    carData.setModel(model);
                                                    carData.setModel_code(model_code);
                                                    carData.setImage(image);
                                                    carData.setPrice(price);
                                                    carData.setTime(time);
                                                    carData.setTime_unit(time_unit);
                                                    carData.setId_context(id_context);
                                                    carData.setRefer_type(refer_type);
                                                    carData.setSupplier(supplier);
                                                    carData.setSupplier_city(supplier_city);
                                                    carData.setSupplier_logo(supplier_logo);
                                                    carData.setDrop_city(drop_city);
                                                    carData.setTc(tc);
                                                    carData.setCoverages(coverages);
                                                    carData.setType(type);
                                                    searchData.add(carData);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            Log.d(TAG, "exception: "+e.getMessage());
                                        }
                                    }
                                }
                                if (searchData != null && searchData.size() > 0) {
                                    chooseSearchAction(searchData);
                                } else {
                                    Utility.message(getContext(), "No record found ");
                                }
                            }  /*  if (msg!=null)
                            Utility.message(getContext(),msg);*/
                        } else if (jsonObject.getInt("response_code") == 102) {
                            getToken();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Utility.hidepopup();
                    Utility.message(getContext(),"There is some connectivity error. Please try after sometime. ");
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                //    token = "f7f97ec1f764b658082ef2a1bceb59fef02563a3";
                HashMap<String, String> serachRequest = new HashMap<>();
                serachRequest.put("access_token", token);
                serachRequest.put("pick_city", pickName);
                serachRequest.put("pick_date", pick_date);
                serachRequest.put("pick_houre", pick_hour);
                serachRequest.put("pick_minute", pick_minute);
                serachRequest.put("drop_city", dropName);
                serachRequest.put("drop_date", drop_date);
                serachRequest.put("drop_houre", drop_hour);
                serachRequest.put("drop_minute", drop_minute);
                serachRequest.put("driver_age", driver_age);
                serachRequest.put("use_current_location", String.valueOf(useCurrentLocation));
                serachRequest.put("sameas_pick_location", String.valueOf(useSameDestLocation));
                serachRequest.put("between_driver_age", String.valueOf(isBetweenDriverAge));
                serachRequest.put("lat", String.valueOf(currentLat));
                serachRequest.put("long", String.valueOf(currentLng));
                serachRequest.put("location_code", location_code);
                serachRequest.put("location_iata", location_iata);
                serachRequest.put("location_type", location_type);
                serachRequest.put("location_code_drop", location_code_drop);
                serachRequest.put("location_iata_drop", location_iata_drop);
                serachRequest.put("location_type_drop", location_type_drop);
                serachRequest.put("pick_city_id",pick_city_id);
                serachRequest.put("drop_city_id",drop_city_id);

             /*   if (chkUseCurrentLocation.isChecked()){
                serachRequest.put("search_type1", location_type);
                serachRequest.put("country_code",country_code);
                serachRequest.put("city", city);
                }*/
                serachRequest.put("search_type1", location_type);
                serachRequest.put("country_code", country_code);
                serachRequest.put("city", city);
                serachRequest.put("language_code", languagecode);
                Log.d(TAG, "getParams: " + serachRequest);
                return serachRequest;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void getToken() {
        final TinyDB sharedpref = new TinyDB(getContext());
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCarGatesapi();
        String grant_type = "client_credentials";
        String client_id = "developer";
        String client_secret = "5a633cf4392e8";

        Call<ApiResponse> apiResponseCall = retroFitApis.token(grant_type, client_id, client_secret);
        apiResponseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                String token1 = response.body().access_token;
                sharedpref.putString("access_token", token1);
                token = tinyDB.getString("access_token");
                requestForSearchCar1();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getPoint() {
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall = retroFitApis.point(" ");
        Log.d(TAG, "getPoint: "+responseCall.request().url());
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();

                if (response.body() != null) {
                    Point point;
                    if (response.body().status) {
                        point = response.body().response.point;
                        pointper = point.getPoint_percentage();
                        Intent intent = new Intent(getActivity(), CarsResultListActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMarkUp() {
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();

        Call<ApiResponse> responseCall = retroFitApis.markup(" ");
        final Gson gson = new Gson();
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();
                MArkupdata point;
                if (response.body() != null) {
                    Log.d(SplashActivity.TAG, "onResponse: point " + gson.toJson(response.body().response.point));

                    if (response.body().status) {
                        point = response.body().response.markup;
                        markup = point.getMarkup_percentage();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Toast.makeText(getContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            location = (Location) data.getSerializableExtra(LocationSelectionActivity.RESPONSE_DATA);
        }
        if (resultCode == 0) {
            if (requestCode == REQUEST_BY_MAP_LOCATION) {

            }
        } else if (resultCode == LocationSelectionActivity.RESPONSE_LOCATION) {
            if (requestCode == REQUEST_PICKUP_LOCATION) {
                city = location.getCity_name();
                et_pickup_location.setText(location.getCity_name());
                location_code = location.getCode();
                location_iata = location.getIata();
                pick_city_id = location.getCity_id();
                location_type = location.getType();
                country_code = location.getCountry_code();
            } else if (requestCode == REQUEST_DESTINATION_LOCATION) {
                et_return_location.setText(location.getCity_name());
                location_code_drop = location.getCode();
                location_iata_drop = location.getIata();
                location_type_drop = location.getType();
                drop_city_id = location.getCity_id();
            }
        }

    }

    protected synchronized void setupLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }
        checkGPSStatus();
        mgoogleApiclient = new GoogleApiClient.Builder(getActivity()).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
        mgoogleApiclient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        android.location.Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiclient);

        if (mLocation != null) {
            currentLat = mLocation.getLatitude();
            currentLng = mLocation.getLongitude();
         /*   currentLat = 24.697252;
            currentLng = 46.704506;*/
            StringBuffer stringBuffer;
            try {
                stringBuffer = getAddress(new LatLng(currentLat, currentLng));
                if (stringBuffer != null) {
                    getPlace(stringBuffer.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public StringBuffer getAddress(LatLng latLng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses = null;
        StringBuffer result = new StringBuffer();
        Locale locale = new Locale(languagecode);
        geocoder = new Geocoder(getContext(), locale);

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && addresses.size() != 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city1 = addresses.get(0).getLocality();
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String subadmin = addresses.get(0).getSubAdminArea();
                String knownName = addresses.get(0).getFeatureName();
                Log.d(TAG, "getAddress: "+city1+state+country+"\n"+knownName+"\n"+address+"\n"+
                        addresses.get(0).getSubAdminArea()+"\n"+ addresses.get(0).getPremises());
                String city = null;
                if (city1==null&&subadmin!=null){
                    city = subadmin;
                } else if (city1==null&&knownName!=null){
                    if (knownName.contains("Unnamed Road")){
                        if (address.contains("Unnamed Road,")) {
                            city = address.replace("Unnamed Road,","");
                        }
                    } else city = knownName;
                } else {
                    city = city1;
                }
                result.append(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    setupLocation();
                } else {
                    chkUseCurrentLocation.setChecked(false);
                }
            }
        }
    }

    private boolean validateData() {
        pickName = et_pickup_location.getText().toString().trim();
        dropName = et_return_location.getText().toString().trim();
        // Location
        if (!chkUseCurrentLocation.isChecked()) {
            useCurrentLocation = 0;
            currentLng = 0.0;
            currentLat = 0.0;

            if (switchSameDestLocation.isChecked()) {
                dropName = pickName;
                drop_city_id = pick_city_id;
                location_code_drop = location_code;
                location_iata_drop = location_iata;
                location_type_drop = location_type;
            }

            if (pickName.isEmpty()) {
                Toast.makeText(activity, getResources().getString(R.string.pick_up_location), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (switchSameDestLocation.isChecked()) {
                useSameDestLocation = 0;
            } else {
                if (dropName.isEmpty()) {
                    useSameDestLocation = 1;
                    Toast.makeText(activity, getResources().getString(R.string.pleae_select_drop_loc), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } else {
            useCurrentLocation = 1;
            //currentLat
            // currentLng

            if (switchSameDestLocation.isChecked()) {
                dropName = pickName;
                drop_city_id = pick_city_id;
                location_code_drop = location_code;
                location_iata_drop = location_iata;
                location_type_drop = location_type;
            }

            if (!switchSameDestLocation.isChecked()) {

                if (dropName == null || dropName.trim().isEmpty()) {
                    Toast.makeText(activity, getResources().getString(R.string.pleae_select_drop_loc), Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                useSameDestLocation = 0;
            }

        }

        if (calendar_drop.compareTo(calendar_pick) <= 0) {
            Toast.makeText(activity, getResources().getString(R.string.selsect_valid_drop_date), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Age
        if (switchDriverAge.isChecked()) {
            isBetweenDriverAge = 1;
/*
            driver_age = "30";
*/
            tinyDB.putString("dAge","");
        } else {
            isBetweenDriverAge = 0;
            driver_age = et_driver_age.getText().toString().trim();
            tinyDB.putString("dAge",driver_age);
            if (driver_age.isEmpty()) {
                Toast.makeText(activity, getResources().getString(R.string.enter_driver_age), Toast.LENGTH_SHORT).show();
                return false;
            } else {
//                return true;
                if (tinyDB.contains("login_data")) {
                    String log = tinyDB.getString("login_data");
                    UserDetails userDetails = new UserDetails();
                    userDetails = gson.fromJson(log, UserDetails.class);
                    user_age = userDetails.getUser_age();
                    if (userDetails.getUser_age() != null && !userDetails.getUser_age().equals("0")) {
                        String age = userDetails.getUser_age();
                        if (age != null && age.equals(driver_age)) {
                            return true;
                        } else {
                            ageDialog();
//                            Utility.message(getContext(),"Entered age is not matched your date of birth.");
                            return false;
                        }
                    } else {
                        driver_age = et_driver_age.getText().toString().trim();
                    }
                }
            }
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void ageDialog() {
        final Dialog dialog;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_age_error);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        TextView txtOk = dialog.findViewById(R.id.dialog_age_error_ok);
        TextView txtDetail = dialog.findViewById(R.id.dialog_age_error_txtInfo);
        txtDetail.setText("We have found that you are searching with driver age (" + driver_age
                + "), but your age according to your profile is " + user_age + ", Please change " +
                "your search age or update your DOB in profile section and search again.");

        txtOk.setOnClickListener(view -> dialog.dismiss());
    }

    @Override
    public void refreshTokenCallBack() {
        token = tinyDB.getString("access_token");
        requestForSearchCar1();
    }

    private void getPlace(String city_name) {
        Utility.showloadingPopup(getActivity());
        RetroFitApis fitApis = RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> bookingDataCall = fitApis.getPlace(city_name, languagecode);

        bookingDataCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + gson.toJson(response.body()));

                    if (response.body().status) {
                        List<UseLocationData> useLocationData;
                        useLocationData = response.body().response.location2;
                        for (UseLocationData locationBean : useLocationData) {
                            city = locationBean.getCity_name();

                            location_code = locationBean.getCode();
                            location_iata = locationBean.getIata();
                            location_type = locationBean.getType();
                            pick_city_id = locationBean.getCity_id();
                            country_code = locationBean.getCountry_code();

                            if (country_code != null) {
                                Locale loc = new Locale(languagecode, country_code);
                                String s = loc.getDisplayCountry();
                                locationBean.setCity_name(locationBean.getCity_name() + " , " + s);
                            }
                            if (locationBean.getCity_name()!=null)
                            et_pickup_location.setText(locationBean.getCity_name());
                            else Utility.message(getContext(), "Sorry service not available for this location please search manually.");

                        }
                    } else Utility.message(getContext(), "Sorry service not available for this location please search manually.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Utility.message(getContext(), "Sorry service not available for this location please search manually.");
            }
        });
    }

}
