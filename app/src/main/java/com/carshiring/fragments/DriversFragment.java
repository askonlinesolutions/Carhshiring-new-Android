package com.carshiring.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.adapters.QuotesAdapter;
import com.carshiring.utilities.AppGlobal;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sony on 02-05-2017.
 */

public class DriversFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    AppGlobal appGlobal= AppGlobal.getInstancess();
    View view,v;
    RecyclerView recyclerView;

    RippleDrawable rippleDrawable;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rr;
    TinyDB sharedpref;
    String token,userid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_drivers_selection,container,false);
        sharedpref=new TinyDB(getContext());
        appGlobal.context=getContext();
        userid=sharedpref.getString("userid");
        token=sharedpref.getString("access_token");
        v=getActivity().findViewById(android.R.id.content);
        recyclerView= (RecyclerView) view.findViewById(R.id.rec_driver_list);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipedriver);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.redStrong));
        swipeRefreshLayout.setOnRefreshListener(this);
      /*  DefaultItemAnimator itemAnimator=new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);*/

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity=getActivity();
    }

    @Override
    public void onRefresh()
    {
        Toast.makeText(getContext(),getResources().getString(R.string.loading),Toast.LENGTH_SHORT).show();
        /*driverAdapter=new DriverAdapter(getActivity(),driverlists);
        recyclerView.setAdapter(driverAdapter);
        driverAdapter.setClickListene(this);
        driverAdapter.notifyDataSetChanged();*/
        swipeRefreshLayout.setRefreshing(false);
    }
}
