package com.carshiring.activities.mainsetup;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.home.BookCarActivity;
import com.carshiring.activities.home.MainActivity;
import com.carshiring.activities.home.Pay1Activity;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;
import com.carshiring.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppBaseActivity implements TextView.OnEditorActionListener {

    EditText username,pass,confirmpassword, edtName;
    View v;
    TinyDB sharedpref;
    String email,password,confirmpass, fname;
    Toolbar toolbar;
    AppGlobal appGlobal=AppGlobal.getInstancess();
    String isBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        appGlobal.context=getApplicationContext();
        v=findViewById(android.R.id.content);
        sharedpref=new TinyDB(getApplicationContext());

        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        username= (EditText) findViewById(R.id.et_Signupemail);
        pass= (EditText) findViewById(R.id.et_signuppassword);
        confirmpassword= (EditText) findViewById(R.id.et_signupconfirmpassword);
        pass.setTypeface(username.getTypeface());
        confirmpassword.setTypeface(username.getTypeface());
        edtName = findViewById(R.id.et_SignupName);

        if (getIntent().hasExtra("booking")){
            isBooking = getIntent().getStringExtra("booking");
        }
        username.setOnEditorActionListener(this);
        pass.setOnEditorActionListener(this);
        confirmpassword.setOnEditorActionListener(this);

        setuptoolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.title_create_account));
        checknetwork();
    }

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
    private void setuptoolbar() {

        toolbar= (Toolbar) findViewById(R.id.bottomToolBar);
        TextView textView= (TextView)toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.sign_up));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public String Msg;
    private void signup() {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(toolbar.getWindowToken(),0);
        fname = edtName.getText().toString().trim();
        email = username.getText().toString().trim();
        password = pass.getText().toString().trim();
        confirmpass = confirmpassword.getText().toString();

        checknetwork();
        if (!email.isEmpty() && !password.isEmpty() && !confirmpass.isEmpty()&& !fname.isEmpty()) {
            if (Utility.checkemail(email)) {
                if (password.equals(confirmpass)) {
                    Utility.showloadingPopup(this);
                    RetroFitApis fitApis = RetrofitApiBuilder.getCargHiresapis();
                    Call<ApiResponse> call = fitApis.signup(email, password,fname);
                    Log.d("TAG", "signup: "+call.request().url());
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            Utility.hidepopup();
                            if (response.body()!=null ){
                                if (response.body().status){
                                    Msg = response.body().msg;
                                    Utility.message(SignUpActivity.this,Msg);
//                            finish();
                                    login();
                                } else {
                                    Msg = response.body().msg;
                                    Utility.message(SignUpActivity.this,Msg);
                                    Utility.hidepopup();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Utility.hidepopup();
                            Utility.message(SignUpActivity.this, getResources().getString(R.string.no_internet_connection));
                        }
                    });
                } else {
                    Utility.message(this, getResources().getString(R.string.pass_doesnotmatch));
                }
            } else {
                Utility.message(this, getResources().getString(R.string.please_enter_valid_email));
            }
        } else {
            if (email.isEmpty() && password.isEmpty() && confirmpass.isEmpty()&&fname.isEmpty()) {
                Utility.message(this, getResources().getString(R.string.fill_all_field));
            }
            else
            {
                if(email.isEmpty())
                {
                    Utility.message(this, getResources().getString(R.string.please_enter_email));
                }
                if(password.isEmpty())
                {
                    Utility.message(this, getResources().getString(R.string.please_enter_pass));
                }
                if(confirmpass.isEmpty())
                {
                    Utility.message(this, getResources().getString(R.string.please_enter_confirm_pass));
                }
                if(fname.isEmpty()) {
                    Utility.message(this, getResources().getString(R.string.please_enter_fname));
                }
            }
        }
    }

    private void login() {
        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(toolbar.getWindowToken(),0);
        if (!email.isEmpty() && !password.isEmpty())
        {
            if(!Utility.isNetworkConnected(getApplicationContext())){
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                return;
            }
            Utility.showloadingPopup(this);
            RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
            Call<ApiResponse> responseCall=retroFitApis.login(email,password);
            responseCall.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Utility.hidepopup();
                    if(response.body().status)
                    {
                        UserDetails userDetails = new UserDetails();
                        userDetails = response.body().response.user_detail;
                        String logindata=new Gson().toJson(userDetails);
                        Log.d("TAG", "onResponse: "+logindata);
                        appGlobal.setLoginData(logindata);
                        String st =  appGlobal.getUser_id();
                        if (isBooking!=null && isBooking.equalsIgnoreCase("booksign")){
                            Intent intent = new Intent(SignUpActivity.this,Pay1Activity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                    else{
                        Utility.message(getApplicationContext(), "Username or password invalid");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Utility.hidepopup();
                    Utility.message(getApplicationContext(),getResources().getString(R.string.no_internet_connection));
                }
            });
        }

    }


    public void checknetwork() {
        if(!Utility.isNetworkConnected(this))
        {
            Snackbar.make(v,getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checknetwork();
                        }
                    }).setActionTextColor(getResources().getColor(R.color.redStrong)).show();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        int id=v.getId();
        email = username.getText().toString().trim();
        password = pass.getText().toString().trim();
        confirmpass = confirmpassword.getText().toString();
        fname = edtName.getText().toString();
        switch (id)
        {
            case R.id.et_Signupemail:
                if(actionId== EditorInfo.IME_ACTION_NEXT)
                {
                    if(!email.isEmpty())
                    {
                        if(!Utility.checkemail(email))
                        {
                            Utility.message(this, getResources().getString(R.string.please_enter_valid_email));
                            username.setFocusableInTouchMode(true);
                       /* username.requestFocus();
                        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        methodManager.showSoftInput(username,InputMethodManager.SHOW_FORCED);*/
                        }
                    }
                    else {
                        Utility.message(SignUpActivity.this, getResources().getString(R.string.please_enter_email));
                    }

                }
                break;
            case R.id.et_signupconfirmpassword:
                if(actionId==EditorInfo.IME_ACTION_DONE)
                {
                    signup();
                }
                break;
        }
        return false;
    }
}
