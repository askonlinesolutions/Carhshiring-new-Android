package com.carshiring.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.carshiring.R;
import com.carshiring.activities.home.CarDetailActivity;
import com.carshiring.adapters.QuotesAdapter;
import com.carshiring.models.QuotesModel;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carshiring.splash.SplashActivity.TAG;

/**
 * Created by rakhi on 13-03-2018.
 */

public class QuotesFragment extends Fragment implements QuotesAdapter.QuoteInterface ,SwipeRefreshLayout.OnRefreshListener{
    View view;
    LinearLayout ll_quote;
    RecyclerView recyclerView;
    QuotesAdapter quotesAdapter;
    String userid;
    Gson gson = new Gson();
    UserDetails userDetails = new UserDetails();
    TinyDB tinyDB;
    List<QuotesModel>quotesModelList;
    LinearLayout linearLayout;
    Button bt_search;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_quotes,container,false);
        tinyDB = new TinyDB(getContext());
        String s = tinyDB.getString("login_data");
        userDetails = gson.fromJson(s, UserDetails.class);
        userid = userDetails.getUser_id();
        linearLayout = (LinearLayout) view.findViewById(R.id.rec_prev_booki_no_data);
        bt_search= (Button) view.findViewById(R.id.bt_previousbooking);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.quote_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.redStrong));
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView= (RecyclerView) view.findViewById(R.id.recy_quotes);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.subview_container,
                        new SearchCarFragment())
                        .commit();
            }
        });


        return view;
    }

    private void setMyAdapter(List<QuotesModel> quotesModelList){
        quotesAdapter=new QuotesAdapter(getContext(),quotesModelList);
        quotesAdapter.QuoteAdapterMethod(this);
        if (quotesModelList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            recyclerView.setAdapter(quotesAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void QuoteInterfaceMethod(int position, String tag) {
        if (tag.equals("book")){
            Intent intent=new Intent(getActivity(),CarDetailActivity.class);
            intent.putExtra("day", (String) quotesModelList.get(position).getSavelater_day());
            intent.putExtra("refer_type", (String) quotesModelList.get(position).getSavelater_refer_type());
            intent.putExtra("point_earn", (String) quotesModelList.get(position).getSavelater_bookingpoint());
            intent.putExtra("id_context",quotesModelList.get(position).getSavelater_carnect_id());
            intent.putExtra("type", (String) quotesModelList.get(position).getSavelater_type());
            startActivity(intent);
        } else if (tag.equals("delete")){
            deleteCar(position);
        } else { }

    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

     //   savelater();
    }

    private void savelater() {
        quotesModelList=new ArrayList<>();

        Utility.showloadingPopup(getActivity());
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();

        final Call<ApiResponse> bookingDataCall = fitApis.getQuotes(userid);

        bookingDataCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();
                Log.d(TAG, "onResponse: savelater"+gson.toJson(response.body()));
                if (response.body()!=null){
                    if (response.body().status){
                        if (quotesModelList!=null){
                            quotesModelList.clear();
                        }
                        quotesModelList.addAll(response.body().response.save_later_list);
                        if (quotesModelList.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.GONE);
                            setMyAdapter(quotesModelList);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        quotesAdapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    Utility.message(getContext(), response.body().message);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                Utility.hidepopup();
            }
        });
    }


    private void deleteCar(final int pos){
        Utility.showloadingPopup(getActivity());
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();

        final Call<ApiResponse> deleteCarCall = fitApis.deleteQuotes(quotesModelList.get(pos).getSavelater_id());
        deleteCarCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();
                if (response.body()!=null){
                    if (response.body().status){
                        quotesModelList.remove(pos);
                        quotesAdapter.notifyDataSetChanged();
                      }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }

    @Override
    public void onRefresh() {
        savelater();
//        setMyAdapter(quotesModelList);
        swipeRefreshLayout.setRefreshing(false);
    }
}
