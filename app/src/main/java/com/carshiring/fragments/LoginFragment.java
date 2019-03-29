package com.carshiring.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.home.MainActivity;
import com.carshiring.activities.mainsetup.ForgotPasswordActivity;
import com.carshiring.activities.mainsetup.LoginActivity;
import com.carshiring.activities.mainsetup.SignUpActivity;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener,TextView.OnEditorActionListener{
    AppGlobal appGlobal=AppGlobal.getInstancess();
    TextView txtWelcome, txtEmail, txtPass, txtLoginForgot;
    EditText username,password;
    LinearLayout ll_forgetpassword;
    Button  btnSkip,bt_signup;
    TinyDB sharedpref;
    String token,language_code;
    Toolbar toolbar;
    Gson gson = new Gson();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.activity_login, container, false);
        sharedpref=new TinyDB(getContext());
        language_code = sharedpref.getString("language_code") ;
        token=sharedpref.getString("access_token");
        appGlobal.context=getContext();

//        find id
        ll_forgetpassword= (LinearLayout) view.findViewById(R.id.ll_forgetpassword);
        bt_signup=  view.findViewById(R.id.bt_signup);
        txtWelcome = (TextView) view.findViewById(R.id.login_welcome);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtPass = (TextView) view.findViewById(R.id.txtPass);
        username= (EditText) view.findViewById(R.id.et_email);
        password= (EditText) view.findViewById(R.id.et_password);
        txtLoginForgot = (TextView) view.findViewById(R.id.txtloginForget);
        btnSkip = (Button) view.findViewById(R.id.btnSkip);

//        updateViews(language_code);

        setuptoolbar();
        bt_signup.setOnClickListener(this);
        ll_forgetpassword.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.ll_forgetpassword:
                startActivity(new Intent(getActivity(),ForgotPasswordActivity.class));
                break;
            case R.id.bt_signup:
                startActivity(new Intent(getActivity(),SignUpActivity.class));
                break;
            case R.id.btnSkip:
                sharedpref.putBoolean("isSkipLogin",true);
//        startActivity(new Intent(LoginActivity.this,MainActivity.class));
               /* SearchCarFragment searchCarFragment = new SearchCarFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.subview_container, searchCarFragment)
                        .commit();*/

               getActivity().onBackPressed();
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

    private void login() {
        InputMethodManager methodManager= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(toolbar.getWindowToken(),0);

        String user=username.getText().toString().trim();
        String pass=password.getText().toString().trim();


        if (!user.isEmpty() && !pass.isEmpty())
        {
            if(!Utility.isNetworkConnected(getContext())){
                Toast.makeText(getContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                return;
            }
            Utility.showloadingPopup(getActivity());
            RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis();
            Call<ApiResponse> responseCall=retroFitApis.login(user,pass);
            responseCall.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Utility.hidepopup();
                    if(response.body().status)
                    {
                        UserDetails userDetails = new UserDetails();
                        userDetails = response.body().response.user_detail;
                        String logindata=gson.toJson(userDetails);
                        Log.d("TAG", "onResponse: "+logindata);
                        appGlobal.setLoginData(logindata);
                        String st =  appGlobal.getUser_id();
                     /*   Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);*/
                        Utility.message(getContext(), "Login success");

                        SearchCarFragment searchCarFragment = new SearchCarFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.subview_container, searchCarFragment)
                                .commit();
                        getActivity().onBackPressed();
                    }
                    else{
                        Utility.message(getContext(), "Username or password invalid");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Utility.hidepopup();
                    Utility.message(getContext(),getResources().getString(R.string.no_internet_connection));
                }
            });
        }
        else {
            if (user.isEmpty() && pass.isEmpty()) {
                Toast.makeText(getContext(), getResources().getString(R.string.enter_email_pass), Toast.LENGTH_SHORT).show();
            } else {
                if (Utility.checkemail(user)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_uname), Toast.LENGTH_SHORT).show();
                }
                if (pass.isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_pass), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void setuptoolbar() {
        toolbar= (Toolbar) view.findViewById(R.id.bottomToolBar);
        TextView textView= (TextView)toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.sign_in));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

}
