package com.carshiring.activities.home;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.carshiring.R;
import com.carshiring.adapters.BookingExtrasAdapter;
import com.carshiring.models.BookingHistory;
import com.carshiring.utilities.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mukesh.tinydb.TinyDB;

import java.lang.reflect.Type;
import java.util.List;

import static com.carshiring.activities.home.BookCarActivity.extraData;

public class BookingExraActivity extends AppCompatActivity {
    private List<BookingHistory.BookingExtraBean> booking_extra;
    private RecyclerView recyclerView;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_exra);
        actionBar = getSupportActionBar() ;
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        actionBar.setTitle("My Extra");

        if (getIntent().hasExtra("extra")){
            String data = getIntent().getStringExtra("extra");
            Type listType = new TypeToken<List<BookingHistory.BookingExtraBean>>() {}.getType();
            booking_extra = new Gson().fromJson(data, listType);
        }
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.extraRecycler);
        TextView txtExtraCharge = findViewById(R.id.txtExtraCharge);
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        String curen = tinyDB.getString("from_currency");
        if (booking_extra!=null && booking_extra.size()>0)
        curr = booking_extra.get(0).getExtra_currency();

        txtExtraCharge.setText("Selected Extra's charge : "+curen
                +   Utility.convertCuurency(getTotal()
                , getApplicationContext()));

        BookingExtrasAdapter bookingExtrasAdapter = new BookingExtrasAdapter(booking_extra,this);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookingExtrasAdapter);
    }

    private String curr;
    public double getTotal(){
        double sum = 0;
        for (int i =0; i<booking_extra.size();i++){

            sum = sum+ Double.parseDouble(booking_extra.get(i).getExtra_price());
        }
        return sum;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
