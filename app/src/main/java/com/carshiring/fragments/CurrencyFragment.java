package com.carshiring.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carshiring.R;
import com.carshiring.activities.home.MainActivity;
import com.carshiring.activities.home.Pay1Activity;
import com.carshiring.adapters.CurrencyAdapter;
import com.carshiring.models.CurrencyData;
import com.carshiring.models.CurrencyResponse;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carshiring.fragments.SearchCarFragment.driver_age;
import static com.carshiring.fragments.SearchCarFragment.languagecode;

public class CurrencyFragment extends Fragment implements CurrencyAdapter.Clicklistner{
    private RecyclerView recyclerView;
    private List<CurrencyData> currencyList;
    private TinyDB tinyDB ;
    private String from_currency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updateRes(languagecode);
        return inflater.inflate(R.layout.fragment_currency, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
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



    private void init(){
        tinyDB = new TinyDB(getContext());
        from_currency = tinyDB.getString("from_currency");
        recyclerView = getView().findViewById(R.id.fragment_currency_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        currencyList = new ArrayList<>();
        currencyList.add(new CurrencyData("British Pound (GBP)", "GBP"));
        currencyList.add(new CurrencyData("EURO (EUR)", "EUR"));
        currencyList.add(new CurrencyData("Saudi Arabian Riyal (SAR)", "SAR"));
        currencyList.add(new CurrencyData("Unites Arab Emirates Dirham (AED)", "AED"));
        currencyList.add(new CurrencyData("US Dollar (USD)","USD"));

        setAdapter();
    }

    @Override
    public void itemclick(int post) {
        String to_currency = currencyList.get(post).getCurrency();
        convertCurrency("SAR", to_currency, "1");
    }


    private void setAdapter(){
        from_currency = tinyDB.getString("from_currency");
        CurrencyAdapter currencyAdapter = new CurrencyAdapter(getContext(), this, currencyList, from_currency);
        recyclerView.setAdapter(currencyAdapter);
    }

    private ProgressDialog progressDialog;
    private void convertCurrency(String from_Currency, final String to_Currency, String amount){
        progressDialog = new ProgressDialog(getContext());
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
                        CurrencyResponse currencyResponse;
                        currencyResponse = response.body().response.currency;
                        Log.d("TAG", "onResponse: "+currencyResponse.getConverted_amount()
                                +"\n"+currencyResponse.getCurrency() +"\n"+currencyResponse.getConverted_currency());
                        tinyDB.putString("from_currency", to_Currency);
                        if (!currencyResponse.getConverted_amount().equals("0.00"))
                        tinyDB.putString("rate", currencyResponse.getConverted_amount());
                        else {
                            tinyDB.putString("rate","1");
                            tinyDB.putString("from_currency", "SAR");
                            errorDialog();
                        }

                        setAdapter();
                      //  getFragmentManager().popBackStackImmediate();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressDialog.dismiss();
                Utility.message(getContext(), getResources().getString(R.string.error));
            }
        });
    }

    private void errorDialog(){
        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_age_error);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        TextView txtOk = dialog.findViewById(R.id.dialog_age_error_ok);
        TextView txtDetail = dialog.findViewById(R.id.dialog_age_error_txtInfo);
        txtDetail.setText("You can't convert currency right now, Please try after sometime.");

        txtOk.setOnClickListener(view -> dialog.dismiss());
    }


}
