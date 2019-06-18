package com.carshiring.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.home.MainActivity;

import com.carshiring.activities.home.UserDashActivity;
import com.carshiring.activities.mainsetup.ChangePasswordActivity;
import com.carshiring.activities.mainsetup.LoginActivity;
import com.carshiring.models.CurrencyResponse;
import com.carshiring.models.PointHistoryData;
import com.carshiring.models.UserDetails;
import com.carshiring.models.WalletHistoryData;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by rakhi on 13/3/2018.
 * Contact Number : +91 9958187463
 */

public class AccountFragment extends Fragment implements View.OnClickListener {
    AppGlobal global=AppGlobal.getInstancess();
    LinearLayout ll_mybooking,ll_accountdetails,ll_changepassword,li_profile;
    Toolbar toolbar;
    TinyDB tinyDB ;
    TextView txtPoint,txtWallet;
    View view;
    String user_id;
    UserDetails userDetails = new UserDetails();
    Gson gson = new Gson();
    private String from_currency;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_account,container,false);
        global.context=getContext();
        ll_mybooking= (LinearLayout) view.findViewById(R.id.ll_booking);
        ll_accountdetails= (LinearLayout) view.findViewById(R.id.ll_acccountdetails);
        ll_changepassword= (LinearLayout) view.findViewById(R.id.ll_change_password);
        li_profile= (LinearLayout) view.findViewById(R.id.ll_profile);
        txtWallet = view.findViewById(R.id.fragment_account_wallet);
        ll_mybooking.setOnClickListener(this);
        ll_accountdetails.setOnClickListener(this);
        ll_changepassword.setOnClickListener(this);
        li_profile.setOnClickListener(this);
        setuptoolbar();
        tinyDB = new TinyDB(getContext());
        String data = tinyDB.getString("login_data");
        userDetails = gson.fromJson(data, UserDetails.class);
        user_id = userDetails.getUser_id();
        txtPoint = view.findViewById(R.id.fragment_account_point);
        getWal();

        return view;
    }

    private void setuptoolbar() {
        toolbar= (Toolbar) view.findViewById(R.id.bottomToolBar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.remove("login_data");
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    public List<WalletHistoryData>walletHistoryData = new ArrayList<>();
    public void getWal(){
        if (walletHistoryData!=null){
            walletHistoryData.clear();
        }

        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> walList = fitApis.walletHistory(user_id);
        walList.enqueue(new Callback<ApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response!=null){
                    /*Log.d("TAG", "onResponse: wallet"+gson.toJson(response.body().response.wallet));*/

                    if (response.body().status){
                        if ( response.body().response.wallet!=null){
                            walletHistoryData = response.body().response.wallet;
                        }
                        for (WalletHistoryData walletHistoryData1 : walletHistoryData){
                            if (walletHistoryData1.get_$WalletType204().equals("debit")){
                                String debit = walletHistoryData1.get_$WalletAmount169();
                                debitAmt = Double.parseDouble(debit);
                                totalDebit += debitAmt;
//                            Log.d("TAG", "onResponse: "+debit);
                            }
                            if (walletHistoryData1.get_$WalletType204().equals("credit")){
                                String debit = walletHistoryData1.get_$WalletAmount169();
                                creditAmt = Double.parseDouble(debit);
                                totalCredit+= creditAmt;
                            }
                        }
                        walletAmt = totalCredit-totalDebit;
                        Log.d("TAG", "onResponse: totalDebit"+totalCredit+"\n"+walletAmt);
                        if (walletAmt>0){
                            txtWallet.setText(getResources().getString(R.string.txtWal)+" : "+from_currency+String.valueOf(Utility.convertCuurency(walletAmt,getContext())));
                        }   else {
                            txtWallet.setText(getResources().getString(R.string.txtWal)+" : "+from_currency+String.valueOf("0.00"));
                        }
                    } else {
                        //  Toast.makeText(UserDashActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), ""+ getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

                //   Utility.message(getApplicationContext(), getResources().getString(R.string.check_internet));
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.ll_booking:
                //  startActivity(new Intent(getContext(), MyBookingActivity.class));
                MyBookingsFragment myBookingsFragment=new MyBookingsFragment();
                MainActivity.toolbar.setTitle(getResources().getString(R.string.mybooking));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.subview_container,myBookingsFragment).addToBackStack(null).commit();
                break;
            case R.id.ll_acccountdetails:
                // startActivity(new Intent(getActivity(),AccountDetailsActivity.class));
                break;
            case R.id.ll_change_password:
                startActivity(new Intent(getActivity(),ChangePasswordActivity.class));
                break;
            case R.id.ll_profile:
                startActivity(new Intent(getContext(), UserDashActivity.class));
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        from_currency = tinyDB.getString("from_currency");
        getPoint();
        MainActivity.toolbar.setTitle(getResources().getString(R.string.account));

    }

    private List<PointHistoryData> pointHistoryData = new ArrayList<>();

    private double creditAmt, debitAmt,walletAmt, totalDebit, totalCredit,totalPoint,totalDebitPoint, totalCreditPoint,
            creditPoint, debitPoint;

    private void getPoint(){
        if (pointHistoryData!=null){
            pointHistoryData.clear();
        }
//        debitPoint = 0;
        totalPoint=0;
        totalDebitPoint=0;
        totalCreditPoint=0;
        creditPoint=0;
        debitPoint=0;

        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        final Call<ApiResponse> walList = fitApis.pointHistory(user_id);

        Log.d("TAG", "getPoint: "+walList.request().url());

        walList.enqueue(new Callback<ApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response!=null){
                    if (response.body().status){
                        pointHistoryData = response.body().response.points;
                        Log.d("TAG", "onResponse: "+gson.toJson(pointHistoryData));
                        for (PointHistoryData walletHistoryData1 : pointHistoryData){

                            if (walletHistoryData1.get_$BookingPointType184().equals("debit")){
                                String debit = walletHistoryData1.get_$BookingPoint18();
                                debitPoint = Double.parseDouble(debit);
                                totalDebitPoint += debitPoint;
//                            Log.d("TAG", "onResponse: "+debit);
                            }
                            if (walletHistoryData1.get_$BookingPointType184().equals("credit")){
                                String debit = walletHistoryData1.get_$BookingPoint18();
                                creditPoint = Double.parseDouble(debit);
                                totalCreditPoint+= creditPoint;
                            }
                        }
                       /* if (totalCreditPoint>totalDebitPoint){
                            totalPoint = totalCreditPoint-totalDebitPoint;
                        } else {
                           totalPoint = totalDebitPoint-totalCreditPoint;
                       }
*/
                        totalPoint = totalCreditPoint/*-totalDebitPoint*/;

                        txtPoint.setText(getResources().getString(R.string.points)+" : "+String.format("%.2f", Float.parseFloat(String.valueOf(totalPoint))));

                       /* Log.d("TAG", "onResponse: totalDebit"+totalCreditPoint+"\n"+totalPoint);
                        txtCreditPt.setText(getResources().getString(R.string.txtCredit)+" : "+ String.valueOf(totalCreditPoint));
                        txtdebitPt.setText(getResources().getString(R.string.txtDebit)+" : "+ String.valueOf(debitPoint));*/
                    } else {
                        //  Toast.makeText(UserDashActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.message(getContext(), getResources().getString(R.string.check_internet));
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }


}
