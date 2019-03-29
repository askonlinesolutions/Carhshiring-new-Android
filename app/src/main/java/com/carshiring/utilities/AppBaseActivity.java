package com.carshiring.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.interfaces.IBaseActivity;
import com.carshiring.interfaces.IRefreshToken;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.mukesh.tinydb.TinyDB;

import com.carshiring.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rakhi on 8/2/2018.
 */

public class AppBaseActivity extends AppCompatActivity implements IBaseActivity,IRefreshToken {

    int CALLBACK ;
   public ActionBar actionBar ;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }
    public void checkGPSStatus()
    {
        LocationManager locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSProviderEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isGPSProviderEnable)
        {
            showSettingsAlert();
        }
    }
    private void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    public void getToken(final IRefreshToken iRefreshToken){
        final TinyDB sharedpref=new TinyDB(getApplicationContext());
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCarGatesapi();
        String grant_type = "client_credentials";
        String client_id = "developer";
        String client_secret = "5a633cf4392e8";

        Call<ApiResponse> apiResponseCall = retroFitApis.token(grant_type, client_id, client_secret);
        apiResponseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                String token = response.body().access_token;
                sharedpref.putString("access_token",token);
                iRefreshToken.refreshTokenCallBack();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AppBaseActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void refreshTokenCallBack() {

    }

    @Override
    public void setLanguage() {

    }
}
