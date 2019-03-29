package com.carshiring.activities.mainsetup;

import android.content.Context;
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

import com.carshiring.R;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppBaseActivity implements TextView.OnEditorActionListener {
    EditText username;
    View v;
    TinyDB sharedpref;
    String email,Message;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        v=findViewById(android.R.id.content);
        sharedpref=new TinyDB(getApplicationContext());

        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        username= (EditText) findViewById(R.id.et_email);
        username.setOnEditorActionListener(this);
        setuptoolbar();
    }

    private void setuptoolbar() {
        toolbar= (Toolbar) findViewById(R.id.bottomToolBar);
        TextView textView= (TextView) toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.recoverpassword));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendemail();
            }
        });
    }

    private void sendemail() {
        final  ForgotPasswordActivity _this = this ;
        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(username.getWindowToken(),0);
        email=username.getText().toString().trim();
        if(!email.isEmpty())
        {
            if(Utility.checkemail(email))
            {
                Utility.showloadingPopup(this);
                RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
                Call<ApiResponse> responseCall=fitApis.forgot_pass(email);
                responseCall.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Log.d("TAG", "onResponse: dataa"+new Gson().toJson(response.body()));
                        if(response.body().status) {
                            Message = response.body().msg;
                            Utility.message(ForgotPasswordActivity.this, Message);
                            Utility.hidepopup();
                            finish();
                        } else {
                            Message = response.body().msg;
                            Utility.message(ForgotPasswordActivity.this, Message);
                            Utility.hidepopup();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d("TAG", "onFailure: "+t.getMessage());
                        Utility.message(ForgotPasswordActivity.this,getResources().getString(R.string.no_internet_connection));
                        Utility.hidepopup();
                    }
                });
            }
            else
            {
                Utility.message(this,getResources().getString(R.string.please_enter_valid_email));
            }
        }
        else
        {
            Utility.message(this,getResources().getString(R.string.please_enter_valid_email));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.title_forgetpassword));
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (v.getId())
        {
            case R.id.et_email:
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    sendemail();
                }
                break;
        }
        return false;
    }

}
