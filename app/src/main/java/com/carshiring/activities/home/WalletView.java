package com.carshiring.activities.home;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.adapters.PointViewAdapter;
import com.carshiring.adapters.WalletViewAdapter;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.WalletHistoryData;
import com.carshiring.utilities.AppBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class WalletView extends AppBaseActivity{

    private RecyclerView recyclerView;
    private String wallet_title;
    private TextView textView_amount, textView_affected, textView_type, textView_title;
    LinearLayout linearLayout;
    Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_view);

        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        init();
    }

    private void init(){
        textView_amount = (TextView) findViewById(R.id.wallet_view_amount);
        textView_affected = (TextView) findViewById(R.id.wallet_view_affected);
        textView_type = (TextView) findViewById(R.id.wallet_view_type);
        textView_title = (TextView) findViewById(R.id.wallet_view_title);

        recyclerView = (RecyclerView) findViewById(R.id.wallet_view_recycler);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(WalletView.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        linearLayout = (LinearLayout) findViewById(R.id.rec_prev_booki_no_data);
        btn_search= (Button) findViewById(R.id.bt_previousbooking);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WalletView.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
//        setMyAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wallet_title = getIntent().getExtras().getString("wallet_title");
        assert wallet_title != null;
        setValues(wallet_title);
        setMyAdapter(wallet_title);
    }

    private void setValues(String wallet_title){
        if(wallet_title.equals("Wallet History Details")){
            textView_title.setText(wallet_title);
            textView_amount.setText(getResources().getString(R.string.wallet_amnt));
            textView_affected.setText(getResources().getString(R.string.wallet_affected_amnt));
            textView_type.setText(getResources().getString(R.string.wallet_type));
            actionBar.setTitle(getResources().getString(R.string.my_wallwt_hist));
        }else if(wallet_title.equals("Point History Details")){
            textView_title.setText(wallet_title);
            textView_amount.setText(getResources().getString(R.string.booking_point));
            textView_affected.setText(getResources().getString(R.string.booking_affect_point));
            textView_type.setText(getResources().getString(R.string.booking_point));
            actionBar.setTitle(getResources().getString(R.string.point_hist));
        } else{}
    }

    private void setMyAdapter(String wallet_title){
//        Toast.makeText(getApplicationContext(), UserDashActivity.walletHistoryData.size()+" " + UserDashActivity.pointHistoryData.size(), Toast.LENGTH_SHORT).show();
        if(wallet_title.equals("Wallet History Details")){
            List<WalletHistoryData> walletHistoryData = new ArrayList<>(UserDashActivity.walletHistoryData);
            WalletViewAdapter adapter = new WalletViewAdapter(getApplicationContext(), walletHistoryData);
            if (walletHistoryData.size()>0){
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        } else if(wallet_title.equals("Point History Details")){
            PointViewAdapter adapter = new PointViewAdapter(getApplicationContext(), UserDashActivity.pointHistoryData);
            if (UserDashActivity.pointHistoryData.size()>0){
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
