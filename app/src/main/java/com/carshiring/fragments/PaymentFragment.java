package com.carshiring.fragments;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.adapters.PaymentListAdapter;
import com.carshiring.models.CardListModel;
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

public class PaymentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PaymentListAdapter.Clicklistner {
    AppGlobal appGlobal= AppGlobal.getInstancess();
    View view;
    RecyclerView recyclerView;
    PaymentListAdapter pAdapter;
    List<CardListModel> paylist=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    TinyDB sharedpref;
    String token,userid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_payment_list,container,false);
        sharedpref=new TinyDB(getContext());
        appGlobal.context=getContext();
        userid=sharedpref.getString("userid");
        token=sharedpref.getString("access_token");
        recyclerView= (RecyclerView) view.findViewById(R.id.rec_card_list);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipepayment);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.redStrong));
        swipeRefreshLayout.setOnRefreshListener(this);
        pAdapter=new PaymentListAdapter(getContext(),paylist);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        pAdapter.setclick(this);
        recyclerView.setAdapter(pAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        setupToolBar();
        return view;
    }

    private void setupToolBar() {
        Toolbar toolbar= (Toolbar) view.findViewById(R.id.bottomToolBar);
        ImageView imageView= (ImageView) toolbar.findViewById(R.id.img_bot);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
        TextView textView= (TextView) view.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.add));
      /*  toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddCardActivity.class));
                getActivity().overridePendingTransition(R.anim.up_from_bottom,R.anim.bottom_from_up);
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
//        composedata();
    }


    @Override
    public void onRefresh() {
        Toast.makeText(getContext(),getResources().getString(R.string.loading),Toast.LENGTH_SHORT).show();
//        composedata();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void itemclick(View v, int post) {
     /*   String number=paylist.get(post).getCard_number();
        String name=paylist.get(post).getCard_name();
        String expiry=paylist.get(post).getCard_date();
        String cvv=paylist.get(post).getCard_cvv();
        String country=paylist.get(post).getCard_country();
        String zipcode=paylist.get(post).getCard_zipcode();
        String cardId=paylist.get(post).getCard_id();
        Intent it=new Intent(getActivity(),AddCardActivity.class);
        it.putExtra("cardId",cardId);
        it.putExtra("name",name);
        it.putExtra("number",number);
        it.putExtra("expiry",expiry);
        it.putExtra("cvv",cvv);
        it.putExtra("country",country);
        it.putExtra("zipcode",zipcode);
        startActivity(it);*/
    }
}
