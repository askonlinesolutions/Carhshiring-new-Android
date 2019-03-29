package com.carshiring.activities.home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.os.ConfigurationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.mainsetup.LoginActivity;
import com.carshiring.adapters.CountryAdapter;
import com.carshiring.fragments.AccountFragment;
import com.carshiring.fragments.CurrencyFragment;
import com.carshiring.fragments.MyBookingsFragment;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.interfaces.ISubViewSetupHandler;
import com.carshiring.models.CurrencyResponse;
import com.carshiring.models.UserDetails;
import com.carshiring.splash.SplashActivity;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.Utility;
import com.google.gson.Gson;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.mukesh.tinydb.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rakhi on 9/2/2018.
 * Contact Number : +91 9958187463
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ISubViewSetupHandler,CountryAdapter.OnItemClickListener {
    //public static int bookingHistory;
    NavigationView navigationView;
    public static Toolbar toolbar;
    public SearchQuery searchQuery = new SearchQuery();
    View v;
    String qu,set,fname,lname,email,phone,zip,license,dob,licenseorigin,city,address;
    TinyDB tinyDB;
    DrawerLayout drawer;
    String userId,language_code;
    Dialog dialog;
    TextView txtemail, txtusername;

    UserDetails userDetails = new UserDetails();
    Gson gson = new Gson();

    //    ////
//chnage by vaibhav
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        checkGPSStatus();

        tinyDB = new TinyDB(getApplicationContext());
        language_code = tinyDB.getString("language_code");
        updateRes(language_code);
        setContentView(R.layout.activity_home);
        appGlobal.context = getApplicationContext();
        if(tinyDB.contains("login_data"))
        {
            String data = tinyDB.getString("login_data");
            userDetails = gson.fromJson(data,UserDetails.class);
            if (userDetails!=null){
                if (userDetails.getUser_id()!=null){
                    userId = userDetails.getUser_id();
                }
            }
        }


        //  userId = tinyDB.getString("userid");
        v = MainActivity.this.findViewById(android.R.id.content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dialog=new Dialog(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_home);

        // language_code = tinyDB.getString("language_code") ;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu =navigationView.getMenu();

        String curre = null;
        Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
        if (locale!=null){
            curre = Currency.getInstance(locale).getCurrencyCode();
        }
        if (!tinyDB.contains("from_currency")){
            if (curre.equalsIgnoreCase("SAR")){
                tinyDB.putString("from_currency", "SAR");
                tinyDB.putString("rate", "1");
            } else {
//                tinyDB.putString("from_currency", "USD");
                tinyDB.putString("from_currency", "SAR");
                tinyDB.putString("rate", "1");
              //  convertCurrency("SAR", "USD","1");
            }
        }
        logout = menu.findItem(R.id.action_logout);
        if(tinyDB.contains("login_data"))
        {
            logout.setTitle(getResources().getString(R.string.logout));
            logout.setVisible(true);
        } else {
            logout.setTitle(getResources().getString(R.string.login));
            logout.setVisible(true);
        }

        Intent it = getIntent();
        if (it != null) {
            qu = it.getStringExtra("From Payment");
            if (qu != null) {
                if (qu.equalsIgnoreCase("Payment")) {
                    MyBookingsFragment myBookingsFragment = new MyBookingsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, myBookingsFragment)
                            .commit();
                }
            } else {
                SearchCarFragment searchCarFragment = new SearchCarFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, searchCarFragment)
                        .commit();
            }
        }

        if (tinyDB.contains("login_data")){
            String data = tinyDB.getString("login_data");
            userDetails = gson.fromJson(data,UserDetails.class);
            if ( userDetails.getUser_id() != null){
                userId = userDetails.getUser_id();
            }
            if (userDetails.getUser_lname() == null || userDetails.getUser_lname().length() == 0){
                set = "update_profile";
                setupoverlay(set);
            }
        }
    }

    private ProgressDialog progressDialog;
    private void convertCurrency(String from_Currency, final String to_Currency, String amount){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> currencyCon = fitApis.convertCurency(from_Currency, to_Currency, amount);

        currencyCon.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body()!=null){
                    progressDialog.dismiss();
                    if (response.body().status){
                        CurrencyResponse currencyResponse = new CurrencyResponse();
                        currencyResponse = response.body().response.currency;
                        Log.d("TAG", "onResponse: "+currencyResponse.getConverted_amount()
                                +"\n"+currencyResponse.getCurrency() +"\n"+currencyResponse.getConverted_currency());
                        if (!currencyResponse.getConverted_amount().equals("0.00"))
                            tinyDB.putString("rate", currencyResponse.getConverted_amount());
                        else {
                            tinyDB.putString("rate","1");
                            tinyDB.putString("from_currency", "SAR");
                        }
                        //  getFragmentManager().popBackStackImmediate();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressDialog.dismiss();
                Utility.message(getApplicationContext(), getResources().getString(R.string.error));
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onPrepareOptionsMenu(menu);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    public void checkGPSStatus() {
        LocationManager locationManager =(LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSProviderEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isGPSProviderEnable)
        {
            showSettingsAlert();
        }
    }

    private void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.gps_sett));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.gps_not_enabled));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
//                checkGPSStatus();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {

        //   super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.exit));
            builder.setMessage(getResources().getString(R.string.wantExit));
            builder.setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public static MenuItem logout;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        qu = "";

        int id = item.getItemId();

        setupSubView(id);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        qu = "";
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.action_about_us:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;

            case R.id.action_contact_us:
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                break;
            case R.id.action_logout:
                tinyDB.remove("login_data");
                tinyDB.remove("extra_added");
                tinyDB.remove("full_prot");
                tinyDB.remove("from_currency");
                tinyDB.remove("isSkipLogin");
                tinyDB.remove("access_token");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.action_language:
                startActivity(new Intent(MainActivity.this, Language.class));
                break;
         /*   case R.id.action_convert_currency:

                break;*/
            case R.id.action_convert_currency:
               // startActivity(new Intent(MainActivity.this, TermsActivity.class));

                getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, new CurrencyFragment())
                        .addToBackStack(CurrencyFragment.class.getName()).commit();
                toolbar.setTitle(getResources().getString(R.string.action_convert_currency));

                break;
            case R.id.action_accounts:
                if (checkLogin()) {
                    AccountFragment myaccountFragment = new AccountFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, myaccountFragment)
                            .addToBackStack("null").commit();
                    toolbar.setTitle(getResources().getString(R.string.action_accounts));
                }
                break;
            case R.id.action_booking:
                if (checkLogin()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, new MyBookingsFragment())
                            .addToBackStack("null").commit();
                    toolbar.setTitle(getResources().getString(R.string.mybooking));
                }
                break;

            case R.id.action_search_car:
                Bundle bundle = new Bundle();
                bundle.putSerializable("query", searchQuery);
                SearchCarFragment searchCarFragment = new SearchCarFragment();
                searchCarFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, searchCarFragment)
                        .commit();

                toolbar.setTitle(getResources().getString(R.string.action_search_car));
                break;
        }

        return true;
    }

    AppGlobal appGlobal = AppGlobal.getInstancess();

    private void updateRes(String lang){
        Resources res = getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang.toLowerCase())); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }


    @Override
    public void setupSubView(int id) {

        switch (id){
            case R.id.action_accounts:
                if (checkLogin()) {
                    AccountFragment myaccountFragment = new AccountFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, myaccountFragment)
                            .addToBackStack("null").commit();
                    toolbar.setTitle(getResources().getString(R.string.action_accounts));
                }
                break;

            case R.id.action_booking:
                if (checkLogin()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, new MyBookingsFragment())
                            .addToBackStack("null").commit();
                    toolbar.setTitle(getResources().getString(R.string.mybooking));                }
                break;

            case R.id.action_search_car:
                Bundle bundle = new Bundle();
                bundle.putSerializable("query", searchQuery);
                SearchCarFragment searchCarFragment = new SearchCarFragment();
                searchCarFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, searchCarFragment)
                        .addToBackStack("search").commit();
                toolbar.setTitle(getResources().getString(R.string.action_search_car));
                break;

        }
    }

    private boolean checkLogin() {
        if (userId != null && !userId.trim().isEmpty()) {
            return true;
        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return false;
        }
    }

    private Button btupdate,btnCancel;
    private TextView txtDob,txtCountry;;

    private void setupoverlay(String set) {

        final EditText edtFname, edtLname, edtemail,edtPhone,edtZip, edtLicense,edtCity, edtAddress;
        Spinner edtLicenseOrign;

//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (set.equals("update_profile")){
            dialog.setContentView(R.layout.popup_updateprofile);
            edtFname = dialog.findViewById(R.id.etUserFirstName);
            edtLname = dialog.findViewById(R.id.etUserLastName);
            edtemail = dialog.findViewById(R.id.etUserEmail);
            txtCountry = dialog.findViewById(R.id.countryList);
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
                    licenseorigin = (String) getKeyFromValue(SplashActivity.country,item);
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
                    if (!fname.isEmpty()&&fname.length()>3){
                        if (!lname.isEmpty()&&lname.length()>3){
                            if (Utility.checkemail(email)){
                                if (Utility.checkphone(phone)){
                                    if (dob!=null && !dob.isEmpty()){
                                        updateProfile(userId,fname);
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
                            Utility.message(getApplication(),getResources().getString(R.string.please_enter_last_name));
                        }
                    } else {
                        Utility.message(getApplication(),getResources().getString(R.string.please_enter_first_name));
                    }
                }
            });
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    private Dialog dialogCountry;
    private void countryDialog(ArrayList<String> county){
        dialogCountry = new Dialog(MainActivity.this);
        dialogCountry.setContentView(R.layout.popup_country_code);
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

    public void dob_pick(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = Calendar.getInstance().get(Calendar.YEAR) - 22;
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

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    private void updateProfile(String userid, String fname) {
        if(!Utility.isNetworkConnected(getApplicationContext())){
            Toast.makeText(MainActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            return;
        }
        Utility.showloadingPopup(this);
        RetroFitApis retroFitApis= RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall=retroFitApis.updateprofile(userid,fname,lname,email,phone,zip,license,
                licenseorigin,dob,city,address);

        Log.d("TAG", "updateProfile: "+responseCall.request().body());

        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();
                if(response.body().status==true)
                {
                    Log.d("TAG", "onResponse: "+response.body().msg);
                    Toast.makeText(MainActivity.this, ""+ response.body().msg, Toast.LENGTH_SHORT).show();
                    userDetails = response.body().response.user_detail;
                    String logindata=gson.toJson(userDetails);
                    appGlobal.setLoginData(logindata);
                    dialog.dismiss();
                }
                else{
                    Utility.message(getApplicationContext(), response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Utility.message(getApplicationContext(),getResources().getString(R.string.no_internet_connection));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checknetwork();
    }

    public void checknetwork() {
        if (!Utility.isNetworkConnected(MainActivity.this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
           /* Snackbar.make(v, getResources().getString(R.string.check_internet), Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checknetwork();
                }
            }).setActionTextColor(getResources().getColor(R.color.redStrong)).show();*/
        }
    }

    @Override
    public void onItemClickCategory(int position) {
        String item = SplashActivity.counrtyList.get(position);
        licenseorigin = (String) getKeyFromValue(SplashActivity.country,item);
        txtCountry.setText(item);
        dialogCountry.dismiss();
    }
}

