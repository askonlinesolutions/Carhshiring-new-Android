package com.carshiring.activities.mainsetup;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.home.MainActivity;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppBaseActivity implements View.OnClickListener,TextView.OnEditorActionListener {

    AppGlobal appGlobal=AppGlobal.getInstancess();
    TextView txtWelcome, txtEmail, txtPass, txtLoginForgot;
    EditText username,password;
    LinearLayout ll_forgetpassword;
    Button  btnSkip,bt_signup;
    TinyDB sharedpref;
    String token,language_code;
    Toolbar toolbar;
    Gson gson = new Gson();
    private String isSave;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpref=new TinyDB(getApplicationContext());
        language_code = sharedpref.getString("language_code") ;
        token=sharedpref.getString("access_token");
        updateRes(language_code);
        setContentView(R.layout.activity_login);

        appGlobal.context=getApplicationContext();

//        find id
        ll_forgetpassword= (LinearLayout) findViewById(R.id.ll_forgetpassword);
        bt_signup=  findViewById(R.id.bt_signup);
        txtWelcome = (TextView) findViewById(R.id.login_welcome);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPass = (TextView) findViewById(R.id.txtPass);
        username= (EditText) findViewById(R.id.et_email);
        password= (EditText) findViewById(R.id.et_password);
        txtLoginForgot = (TextView) findViewById(R.id.txtloginForget);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        if (getIntent().hasExtra("save")){
            isSave = getIntent().getStringExtra("save");
        }

//        updateViews(language_code);

        bt_signup.setOnClickListener(this);
        ll_forgetpassword.setOnClickListener(this);
        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            /*actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);*/
        }

        setuptoolbar();
    }
    private void updateRes(String lang){
        Resources res = getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang.toLowerCase())); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }


    private void setuptoolbar() {
        toolbar =  findViewById(R.id.bottomToolBar);
        TextView textView = toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.skip));
        toolbar.setOnClickListener(v -> {
            sharedpref.putBoolean("isSkipLogin",true);
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        });

    }

    private void login() {
        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(toolbar.getWindowToken(),0);

        String user=username.getText().toString().trim();
        String pass=password.getText().toString().trim();


        if (!user.isEmpty() && !pass.isEmpty())
        {
            if(!Utility.isNetworkConnected(getApplicationContext())){
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                return;
            }
            Utility.showloadingPopup(this);
            RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
            Call<ApiResponse> responseCall=retroFitApis.login(user,pass);
            responseCall.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Utility.hidepopup();
                    if (response.body() != null) {
                        if(response.body().status)
                        {
                            UserDetails userDetails;
                            userDetails = response.body().response.user_detail;
                            String logindata=gson.toJson(userDetails);
                            Log.d("TAG", "onResponse: "+logindata);
                            appGlobal.setLoginData(logindata);
                            sharedpref.remove("isSkipLogin");
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Utility.message(getApplicationContext(), "Username or password invalid");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Utility.hidepopup();
                    Utility.message(getApplicationContext(),getResources().getString(R.string.no_internet_connection));
                }
            });
        }
        else {
            if (user.isEmpty() && pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_email_pass), Toast.LENGTH_SHORT).show();
            } else {
                if (Utility.checkemail(user)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_uname), Toast.LENGTH_SHORT).show();
                }
                if (pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_pass), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void skipLogin(View view) {
        login();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(R.string.login);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.ll_forgetpassword:
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                break;
            case R.id.bt_signup:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_password:
                login();
                break;
        }
        return false;
    }
}
