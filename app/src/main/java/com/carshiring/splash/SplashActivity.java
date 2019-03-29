package com.carshiring.splash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.os.ConfigurationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.home.MainActivity;
import com.carshiring.activities.mainsetup.LoginActivity;
import com.carshiring.models.CurrencyResponse;
import com.carshiring.models.LanguageModel;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppBaseActivity {
    public static HashMap<String, String> country = new HashMap<>();
    private String accessToken,languageselected;
    private TinyDB sharedpref;
    TextView txten, txtAr;
    View v;
    LinearLayout layout;
    TextView txtChoose;
    ArrayList<String> langlistname,langlistcode,langlistId  ;
    String[] lan;
    public static ArrayList<String>counrtyList = new ArrayList<>(), countryCode=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        country.put("AD","Andorra");
        country.put("AE","United Arab of Emirates");
        country.put("AF","Afghanistan");
        country.put("AG","Antigua and Barbuda");
        country.put("AI","Anguilla");
        country.put("AL","Albania");
        country.put("AM","Armenia");
        country.put("AO","Angola");
        country.put("AR","Argentina");
        country.put("AS","American Samoa");
        country.put("AT","Austria");
        country.put("AU","Australia");
        country.put("AW","Aruba");
        country.put("AZ","Azerbaijan");
        country.put("BA","Bosnia Herzegovina");
        country.put("BB","Barbados");
        country.put("BD","Bangladesh");
        country.put("BE","Belgium");
        country.put("BF","Burkina Faso");
        country.put("BG","Bulgaria");
        country.put("BH","Bahrain");
        country.put("BI","Burundi");
        country.put("BJ","Benin");
        country.put("BL","Guadeloupe");
        country.put("BM","Bermuda");
        country.put("BN","Brunei Darussalam");
        country.put("BO","Bolivia");
        country.put("BQ","Bonaire");
        country.put("BR","Brazil");
        country.put("BS","Bahamas");
        country.put("BT","Bhutan");
        country.put("BW","Botswana");
        country.put("BY","Belarus");
        country.put("BZ","Belize");
        country.put("CA","Canada");
        country.put("CC","Cocos (Keeling); Islands");
        country.put("CD","Democratic Republic of The Congo");
        country.put("CF","Central African Republic");
        country.put("CG","Congo");
        country.put("CH","Switzerland");
        country.put("CI","Cote dIvoire");
        country.put("CK","Cook Islands");
        country.put("CL","Chile");
        country.put("CM","Cameroon");
        country.put("CN","China");
        country.put("CO","Colombia");
        country.put("CR","Costa Rica");
        country.put("CU","Cuba");
        country.put("CV","Cape Verde");
        country.put("CW","Curacao");
        country.put("CX","Christmas Island");
        country.put("CY","Cyprus");
        country.put("CZ","Czech Republic");
        country.put("DE","Germany");
        country.put("DJ","Djibouti");
        country.put("DK","Denmark");
        country.put("DM","Dominica");
        country.put("DO","Dominican Republic");
        country.put("DZ","Algeria");
        country.put("EC","Ecuador");
        country.put("EE","Estonia");
        country.put("EG","Egypt");
        country.put("EH","Morocco");
        country.put("ER","Eritrea");
        country.put("ES","Spain");
        country.put("ET","Ethiopia");
        country.put("FI","Finland");
        country.put("FJ","Fiji");
        country.put("FK","Falkland Islands");
        country.put("FM","Micronesia");
        country.put("FO","Faroe Islands");
        country.put("FR","France");
        country.put("GA","Gabon");
        country.put("GB","United Kingdom");
        country.put("GD","Grenada");
        country.put("GE","Georgia");
        country.put("GF","French Guiana");
        country.put("GG","Alderney, United Kingdom");
        country.put("GH","Ghana");
        country.put("GI","Gibraltar");
        country.put("GL","Greenland");
        country.put("GM","Gambia");
        country.put("GP","Gua deloupe");
        country.put("GQ","Equatorial Guinea");
        country.put("GR","Greece");
        country.put("GT","Guatemala");
        country.put("GU","Guam");
        country.put("GW","Guinea-Bissau");
        country.put("GY","Guinea");
        country.put("HK","Hong Kong");
        country.put("HN","Honduras");
        country.put("HR","Croatia");
        country.put("HT","Haiti");
        country.put("HU","Hungary");
        country.put("ID","Indonesia");
        country.put("IE","Ireland");
        country.put("IL","Israel");
        country.put("IN","India");
        country.put("IO","central Indian Ocean");
        country.put("IQ","Iraq");
        country.put("IR","Iran");
        country.put("IS","Iceland");
        country.put("IT","Italy");
        country.put("JE","Jersey, United Kingdom");
        country.put("JM","Jamaica");
        country.put("JO","Jordan");
        country.put("JP","Japan");
        country.put("KE","Kenya");
        country.put("KG","Kyrgyzstan");
        country.put("KH","Cambodia");
        country.put("KI","Kiribati");
        country.put("KM","Comoros");
        country.put("KN","St. Christopher (St. Kitts); Nevis");
        country.put("KP","North Korea");
        country.put("KR","South Korea");
        country.put("KW","Kuwait");
        country.put("KY","Cayman Islands");
        country.put("KZ","Kazakstan");
        country.put("LA","Lao Peoples Democratic Republic");
        country.put("LB","Lebanon");
        country.put("LC","St. Lucia");
        country.put("LK","Sri Lanka");
        country.put("LR","Liberia");
        country.put("LS","Lesotho");
        country.put("LT","Lithuania");
        country.put("LU","Luxembourg");
        country.put("LV","Latvia");
        country.put("LY","Libya");
        country.put("MA","Mor occo");
        country.put("MD","Moldova");
        country.put("ME","Montenegro");
        country.put("MF","St Martin, Guadeloupe");
        country.put("MG","Madagascar");
        country.put("MH","Marshall Islands");
        country.put("MK","Macedonia");
        country.put("ML","Mali");
        country.put("MM","Myanmar");
        country.put("MN","Mongolia");
        country.put("MO","Macau");
        country.put("MP","Northern Mariana Islands");
        country.put("MQ","Martinique");
        country.put("MR","Mauritania");
        country.put("MS","Montserrat");
        country.put("MT","Malta");
        country.put("MU","Mauritius");
        country.put("MV","Maldives");
        country.put("MW","Malawi");
        country.put("MX","Mexico");
        country.put("MY","Malaysia");
        country.put("MZ","Mozambique");
        country.put("NA","Namibia");
        country.put("NC","New Caledonia");
        country.put("NE","Niger");
        country.put("NF","Norfolk Island");
        country.put("NG","Nigeria");
        country.put("NI","Nicaragua");
        country.put("NL","Netherlands");
        country.put("NO","Norway");
        country.put("NP","Nepal");
        country.put("NR","Nauru");
        country.put("NU","Niue");
        country.put("NZ","New Zealand");
        country.put("OM","Oman");
        country.put("PA","Panama");
        country.put("PE","Peru");
        country.put("PF","French Polynesia");
        country.put("PG","Papua New Guinea");
        country.put("PH","Philippines");
        country.put("PK","Pakistan");
        country.put("PL","Poland");
        country.put("PM","St. Pierre and Miquelon");
        country.put("PR","Puerto Rico, United States");
        country.put("PS","Palestine");
        country.put("PT","Portugal");
        country.put("PW","Palau");
        country.put("PY","Paraguay");
        country.put("QA","Qatar");
        country.put("RE","Reunion");
        country.put("RO","Romania");
        country.put("RS","Kosovo");
        country.put("RU","Russia");
        country.put("RW","Rwanda");
        country.put("SA","Saudi Arabia");
        country.put("SB","Solomon Islands");
        country.put("SC","Seychelles");
        country.put("SD","Sudan");
        country.put("SE","Sweden");
        country.put("SG","Singapore");
        country.put("SI","Slovenia");
        country.put("SK","Slovakia");
        country.put("SL","Sierra Leone");
        country.put("SN","Senegal");
        country.put("SO","Somalia");
        country.put("SR","Suriname");
        country.put("SS","Juba, Sudan");
        country.put("ST","Sao Tome and Principe");
        country.put("SV","El Salvador");
        country.put("SX","Netherlands Antilles");
        country.put("SY","Syria");
        country.put("SZ","Swaziland");
        country.put("TC","Turks and Caicos Islands");
        country.put("TD","Chad");
        country.put("TG","Togo");
        country.put("TH","Thailand");
        country.put("TJ","Tajikistan");
        country.put("TL","East Timor");
        country.put("TM","Turkmenistan");
        country.put("TN","Tunisia");
        country.put("TO","Tonga");
        country.put("TR","Turkey");
        country.put("TT","Trinidad and Tobago");
        country.put("TV","Tuvalu");
        country.put("TW","Taiwan");
        country.put("TZ","Tanzania");
        country.put("UA","Ukraine");
        country.put("UG","Uganda");
        country.put("US","United States");
        country.put("UY","Uruguay");
        country.put("UZ","Uzbekistan");
        country.put("VC","St. Vincent and The Grenadines");
        country.put("VE","Venezuela");
        country.put("VG","British Virgin Islands");
        country.put("VI","British VirginIslands");
        country.put("VN","Vietnam");
        country.put("VU","Vietnamm");
        country.put("WF","Wallis and Futuna Islands");
        country.put("WS","Samoa");
        country.put("YE","Yemen");
        country.put("YT","Mayotte");
        country.put("ZA","South Africa");
        country.put("ZM","Zambia");
        country.put("ZW","Zimbabwe");

        Set keys = country.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) country.get(key);
            counrtyList.add(value);
            countryCode.add(key);

        }
        Set<String>strings = new HashSet<>();
        strings.addAll(counrtyList);
        counrtyList.clear();
        counrtyList.addAll(strings);
        Collections.sort(counrtyList);

        sharedpref=new TinyDB(getApplicationContext());
        layout = findViewById(R.id.language_layout);
        txtChoose = findViewById(R.id.textView);
        String language_code = sharedpref.getString("language_code") ;
        boolean isSkipLogin = sharedpref.getBoolean("isSkipLogin");

        txten = findViewById(R.id.splashtxtEn);
        txtAr = findViewById(R.id.splashtxtAr);

        if(language_code!=null && !language_code.isEmpty()) {
//            updateResources(this, language_code);
           /* if (isSkipLogin) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                },2000);

            } else*/ if (sharedpref.contains("login_data")){
                layout.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                },2000);

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));finish();

                        finish();
                    }
                },2000);
            }
            return ;
        } else {
            layout.setVisibility(View.VISIBLE);
            txtChoose.setVisibility(View.VISIBLE);
        }

        v=findViewById(android.R.id.content);
        langlistname=new ArrayList<>();
        langlistcode=new ArrayList<>();
        langlistId=new ArrayList<>();
        lan=new String[langlistname.size()];
        if(actionBar!=null) actionBar.hide();
        accessToken = sharedpref.getString("access_token");
       /* if(accessToken!=null && !accessToken.isEmpty()) {
            getLanguageList();
        }else{
            getAccessToken();
        }*/
        getAccessToken();
    }

    private void updateRes(String lang){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang.toLowerCase())); // API 17+ only.
        res.updateConfiguration(conf, dm);
    }

    public static String TAG = SplashActivity.class.getName();

    private void getLanguageList() {
        final  SplashActivity _this =  this ;
        RetroFitApis apis = RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> responseCall = apis.lang_list(" ");
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d(TAG, "onResponse: "+new Gson().toJson(response.body()));
                if(response.body()!=null) {
                    if(response.body().status) {
                        List<LanguageModel> language_list = response.body().response.language_list;
                        if (language_list != null) {
                            for (int i = 0; i < language_list.size(); i++) {
                                LanguageModel languages = language_list.get(i);
                                if (languages.language_code.equalsIgnoreCase("en")){
                                    txten.setText(languages.language_code);
                                } else if (languages.language_code.equalsIgnoreCase("ar")){
                                    txtAr.setText(languages.language_code);
                                }

                                String languageId = languages.language_id;
                                String languageCode = languages.language_code;
                                String languageName = languages.language_name;
                                langlistname.add(languageName);
                                langlistcode.add(languageCode);
                                langlistId.add(languageId);
                                String curre = null;
                                Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
                                if (locale!=null){
                                    curre = Currency.getInstance(locale).getCurrencyCode();
                                }
                                Log.d(TAG, "onResponse: "+sharedpref.getString("from_currency"));
                                if (!sharedpref.contains("from_currency")){
                                    if (curre.equalsIgnoreCase("SAR")){
                                        sharedpref.putString("from_currency", "SAR");
                                        sharedpref.putString("rate", "1");
                                    } else {
//                                        sharedpref.putString("from_currency", "USD"); //for test
                                        sharedpref.putString("from_currency", "SAR");
                                        sharedpref.putString("rate", "1");

                                        //   convertCurrency("SAR", "USD","1");
                                    }
                                } /*else {
                                    if (curre.equalsIgnoreCase("SAR")){
                                        sharedpref.putString("from_currency", "SAR");
                                        sharedpref.putString("rate", "1");
                                    } else {
                                        sharedpref.putString("from_currency", "USD");
                                        convertCurrency("SAR", "USD","1");
                                    }
                                }*/
                            }
                            lan = langlistname.toArray(lan);
                            handlespinner();
                        } else {
                            Utility.message(SplashActivity.this, response.body().message);
                        }
                    }else{
                        if(response.body().error_code==102){
                        }
                        Toast.makeText(_this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SplashActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(SplashActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAccessToken() {
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCarGatesapi();
        String grant_type = "client_credentials";
        String client_id = "developer";
        String client_secret = "5a633cf4392e8";
        Call<ApiResponse> apiResponseCall = retroFitApis.token(grant_type, client_id, client_secret);
        apiResponseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body()!=null){
                    accessToken = response.body().access_token;
                    if(accessToken!=null && !accessToken.isEmpty()) {
                        sharedpref.putString("access_token", accessToken);
                        getLanguageList();
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(SplashActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlespinner() {

        txtAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language_code  = txtAr.getText().toString().trim();
                sharedpref.putString("language_code",language_code);
                updateRes(language_code);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

        txten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language_code  = txten.getText().toString().trim();
                sharedpref.putString("language_code",language_code);
                updateRes(language_code);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

        final ArrayAdapter<String> adapt=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,lan);
        adapt.setDropDownViewResource(R.layout.spinner_item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checknetwork();
    }

    public void checknetwork() {
        if(!Utility.isNetworkConnected(this))
        {
            Utility.message(this, getResources().getString(R.string.no_internet_connection));
        }
    }

    private ProgressDialog progressDialog;
    private void convertCurrency(String from_Currency, final String to_Currency, String amount){
        progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> currencyCon = fitApis.convertCurency(from_Currency, to_Currency, amount);

        currencyCon.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressDialog.dismiss();
                if (response.body()!=null){
                    if (response.body().status){
                        CurrencyResponse currencyResponse = new CurrencyResponse();
                        currencyResponse = response.body().response.currency;
                        Log.d("TAG", "onResponse: "+currencyResponse.getConverted_amount()
                                +"\n"+currencyResponse.getCurrency() +"\n"+currencyResponse.getConverted_currency());
                        if (!currencyResponse.getConverted_amount().equals("0.00"))
                            sharedpref.putString("rate", currencyResponse.getConverted_amount());
                        else {
                            sharedpref.putString("from_currency", "SAR");
                            sharedpref.putString("rate", "1");
                        }
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

}
