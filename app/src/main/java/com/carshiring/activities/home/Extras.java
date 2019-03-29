package com.carshiring.activities.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.carshiring.R;
import com.carshiring.adapters.ExtrasAdapter;
import com.carshiring.models.ExtraAdded;
import com.carshiring.models.ExtraBean;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.Utility;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

import static com.carshiring.activities.home.CarDetailActivity.carImage;
import static com.carshiring.activities.home.CarDetailActivity.carPrice;
import static com.carshiring.activities.home.CarDetailActivity.currency;
import static com.carshiring.activities.home.CarDetailActivity.extralist;
import static com.carshiring.activities.home.CarDetailActivity.modelname;

public class Extras extends AppBaseActivity {
    private ImageView car_img;
    private TextView carname;
    private RecyclerView extra;
    private ExtrasAdapter extrasAdapter;
    private TinyDB tinyDB;
    public static double totalExtra;
    private Gson gson = new Gson();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);
        extraData = new ArrayList<>();
        tinyDB = new TinyDB(getApplicationContext());
        car_img=findViewById(R.id.img_car);
        carname=findViewById(R.id.txt_carname);
        carname.setText(modelname + " " + getResources().getString(R.string.or_similar)+ "\n \n" + currency
                +Utility.convertCuurency(Double.parseDouble(carPrice),getApplicationContext()));
        Glide.with(this).load(carImage).into(car_img);
        extra=findViewById(R.id.recy_extra);
        extrasAdapter=new ExtrasAdapter(Extras.this,extralist);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        extra.setLayoutManager(layoutManager);
        extra.setAdapter(extrasAdapter);
        setupactionbar();
        setUpToolbar();

    }

    private void setupactionbar() {
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.select_extras));
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    public static List<ExtraBean> extraData;

    private void setUpToolbar() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.bottomToolBar);
        TextView textView= (TextView) toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.add_extra));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extraData = extrasAdapter.getExtra();
                totalExtra = extrasAdapter.getTotal();
                String extra = gson.toJson(extraData);
                tinyDB.putString("extra_added", extra);
                if (extraData!=null && extraData.size()>0)
                Utility.message(getApplication(), getResources().getString(R.string.extra_added));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
