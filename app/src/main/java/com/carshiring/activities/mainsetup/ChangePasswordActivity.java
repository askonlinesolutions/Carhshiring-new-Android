package com.carshiring.activities.mainsetup;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppBaseActivity {
    TextInputEditText tiEt_currentpass,tiEt_newpass,tiEt_confirmpass;
    TextInputLayout layoutCurrentpass,layoutNewpass,layoutConfirmpass;
    private TinyDB sherprf;
    private String token;
    private String userId;
    UserDetails userDetails =new UserDetails();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sherprf = new TinyDB(getApplicationContext());
        String login = sherprf.getString("login_data");
        userDetails = gson.fromJson(login,UserDetails.class);
        userId = userDetails.getUser_id();
        //Handling Layout EditText
        layoutCurrentpass= (TextInputLayout) findViewById(R.id.lay_current_pass);
        layoutNewpass= (TextInputLayout) findViewById(R.id.lay_new_pass);
        layoutConfirmpass=(TextInputLayout) findViewById(R.id.lay_confirm_pass);

        //Handing Edit Text
        tiEt_currentpass= (TextInputEditText) findViewById(R.id.tietxt_current_pass);
        tiEt_newpass=(TextInputEditText) findViewById(R.id.tietxt_new_pass);
        tiEt_confirmpass=(TextInputEditText) findViewById(R.id.tietxt_confirmnew_pass);


        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);

        }
        setuptoolbar();
    }
    private void setuptoolbar() {
        final Toolbar toolbar= (Toolbar) findViewById(R.id.bottomToolBar);
        TextView textView= (TextView) toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.save_change));

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPass = tiEt_currentpass.getText().toString().trim() ;
                String newPass =  tiEt_newpass.getText().toString().trim() ;
                String newConfirmPass = tiEt_confirmpass.getText().toString().trim() ;
                if(!currentPass.isEmpty() && !newPass.isEmpty() && !newConfirmPass.isEmpty() && newPass.equals(newConfirmPass))
                {
                    changePassword(currentPass,newPass);
                 /*   Snackbar.make(v, getResources().getString(R.string.saved_successfully),
                            Snackbar.LENGTH_LONG).setAction("Finish",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();*/
                }
                else {
                    //layoutCurrentpass.setError("Please Check");
                    tiEt_currentpass.setError(getResources().getString(R.string.check_current_pass));
                    tiEt_confirmpass.setError(getResources().getString(R.string.check_confirm_pass));
                    tiEt_newpass.setError(getResources().getString(R.string.check_new_pass));
                    //tiEt_currentpass.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        });
    }

    private void changePassword(String currentPass, String newPass) {
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis() ;

        Call<ApiResponse> responseCall = retroFitApis.change_pass(currentPass,newPass,userId) ;
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                sherprf.remove("login_data");
                startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                Toast.makeText(ChangePasswordActivity.this, response.body().msg, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.change_your_password));
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
}
