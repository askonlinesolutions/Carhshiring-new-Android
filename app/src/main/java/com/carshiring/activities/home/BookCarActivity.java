package com.carshiring.activities.home;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.carshiring.R;
import com.carshiring.adapters.ExtrasDetailAdapter;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.ExtraBean;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.Utility;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

import static com.carshiring.activities.home.CarDetailActivity.carImage;
import static com.carshiring.activities.home.CarDetailActivity.car_type;
import static com.carshiring.activities.home.CarDetailActivity.termsurl;
import static com.carshiring.splash.SplashActivity.TAG;

public class BookCarActivity extends AppBaseActivity implements View.OnClickListener, ExtrasDetailAdapter.RemoveExtra{

    TextView terms,quotes,carname,carprice,txtAddExtra,txtAddExtraChanrge,txtSaveLater, txtFull,txtPoint, txtFullValue;
    ImageView carImg,imglogo;
    LinearLayout extraView,addExtra;
    //  List<CarSpecification> carSpecificationList;
    ProgressBar bar,bar1;
    TinyDB tinyDB;
    UserDetails userDetails = new UserDetails();
    private String number;
    private String curren;
    Gson gson = new Gson();
    public static List<ExtraBean> extraData ;
    private double extraPrice;
    private ExtrasDetailAdapter extrasDetailAdapter;
    private RecyclerView extraRecyclerView;
    EditText edtflight;
    private View viewExtra;
    public static String flight_no,fullProtection ;
    TextView tvFromDate,tvPickDate,txtDob,txtOneway,txtDriverSur,tvTodate,txtPlaceDrop;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_car);
        actionBar = getSupportActionBar() ;
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        setupToolbar();
        extraData = new ArrayList<>();
        tinyDB = new TinyDB(getApplicationContext());
        String logindata= tinyDB.getString("login_data");

        userDetails = gson.fromJson(logindata,UserDetails.class);
        String name;
        if (userDetails!=null&&userDetails.getUser_name()!=null){
            name = userDetails.getUser_name();
        }
        extraRecyclerView = findViewById(R.id.extraRecycler);

        tvFromDate = findViewById(R.id.tvFromDT);
        tvPickDate = findViewById(R.id.txtPlaceName);
        tvTodate = findViewById(R.id.tvToDT);
        viewExtra = findViewById(R.id.viewExtra);
        txtPlaceDrop = findViewById(R.id.txtPlaceName_drop);
        txtOneway = findViewById(R.id.oneway);
        txtAddExtraChanrge = findViewById(R.id.totalExtraCharge);

        tvFromDate.setText(SearchCarFragment.pick_date+"\n"+ SearchCarFragment.pickTime);
        tvPickDate.setText(SearchCarFragment.pickName);
        tvTodate.setText(SearchCarFragment.drop_date+"\n"+SearchCarFragment.dropTime);
        txtPlaceDrop.setText(SearchCarFragment.dropName);
        if (extraData!=null){
            extraData.clear();
        }
        extraData = Extras.extraData;
        terms= findViewById(R.id.txt_terms);
        txtPoint = findViewById(R.id.txtpoint_cal);
        edtflight = findViewById(R.id.edtFlight);
        quotes = findViewById(R.id.txt_savequote);
        carname = findViewById(R.id.txt_modelname);
        carprice = findViewById(R.id.txt_carPrice);
        carImg = findViewById(R.id.img_car);
        imglogo = findViewById(R.id.img_carlogo);
        bar = findViewById(R.id.progressbar);
        bar1= findViewById(R.id.progressbar1);
        addExtra = findViewById(R.id.extraadded_view);
        txtAddExtra = findViewById(R.id.txt_additional_extra);
        txtFull = findViewById(R.id.txt_full_prote);
        txtFullValue = findViewById(R.id.txt_full_prote_value);
        txtSaveLater = findViewById(R.id.activity_booking_txtSaveLater);
        txtDriverSur = findViewById(R.id.driverSurCharge);
        curren = tinyDB.getString("from_currency");
        if (tinyDB.contains("full_prot")){

            if (CarDetailActivity.fullAmtValue>0){
                txtFull.setVisibility(View.GONE);// should be visible
                txtFullValue.setVisibility(View.GONE);
                fullProtection = "yes";
                txtFullValue.setText(getResources().getString(R.string.full_protection_only) +" "+curren
                        + Utility.convertCuurency(CarDetailActivity.fullAmtValue,getApplicationContext()) + " "+getResources()
                        .getString(R.string.full_day));
            } else   fullProtection = "no";

        } else {
            fullProtection = "no";
        }

        if (CarDetailActivity.driverSur!=null){
            txtDriverSur.setText(CarDetailActivity.driverSur);
            txtDriverSur.setVisibility(View.VISIBLE);
        } else {
            txtDriverSur.setVisibility(View.GONE);
        }
        if (CarDetailActivity.oneway!=null){
            txtOneway.setText(CarDetailActivity.oneway);
            txtOneway.setVisibility(View.VISIBLE);
        } else {
            txtOneway.setVisibility(View.GONE);
        }

        if (carImage!=null){

            Glide.with(getApplication()).load(CarDetailActivity.carImage).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                            boolean isFirstResource) {
                    bar.setVisibility(View.GONE);
                    return false;
                }
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                               DataSource dataSource, boolean isFirstResource) {
                    bar.setVisibility(View.GONE);
                    return false;
                }
            }).apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                    .into(carImg);
        }
        txtPoint.setText(getResources().getString(R.string.colletcted_point) + String.valueOf(CarDetailActivity.point));
        terms.setOnClickListener(this);
        quotes.setOnClickListener(this);

        bar.setVisibility(View.VISIBLE);
        bar1.setVisibility(View.VISIBLE);
        Glide.with(getApplication()).load(CarDetailActivity.logo).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                        boolean isFirstResource) {
                bar1.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                bar1.setVisibility(View.GONE);
                return false;
            }
        }).apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
          .into(imglogo);
        carname.setText(CarDetailActivity.modelname );
        carprice.setText(curren + "" + Utility.convertCuurency(Double.parseDouble(CarDetailActivity
                .carPrice), getApplicationContext())+ "/ "
                +CarsResultListActivity.day + " "+ CarsResultListActivity.time);

        if (extraData!=null && extraData.size()>0){
            txtAddExtra.setVisibility(View.VISIBLE);
            viewExtra.setVisibility(View.VISIBLE);

            extraPrice = getTotal(extraData);

            txtAddExtraChanrge.setText("Total : "+curren +Utility.convertCuurency(extraPrice, getApplicationContext()) );
            for (int i=0;i<extraData.size();i++){
                String price = extraData.get(i).getPrice();
                number = extraData.get(i).getQty();
                name = extraData.get(i).getName();
                String currency = extraData.get(i).getCurrency();
                addLayout(name, price,number,curren,i);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        extraRecyclerView.setLayoutManager(linearLayoutManager);
        if (extraData!=null && extraData.size()>0){
            extrasDetailAdapter = new ExtrasDetailAdapter(extraData, getApplicationContext(),this);
            extraRecyclerView.setAdapter(extrasDetailAdapter);
        }
        txtSaveLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public double getTotal(List<ExtraBean> extraData){
        double sum = 0;
        for (int i = 0; i< extraData.size(); i++){
            sum = sum+ Double.parseDouble(extraData.get(i).getPrice());
        }
        return sum;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.car_book));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        tinyDB.remove("extra_added");
//        tinyDB.remove("full_prot");

        if (extraData!=null){
            extraData.clear();
        }

    }


    @SuppressLint("SetTextI18n")
    private void addLayout(String name, String price, final String number, String currency, final int i) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = layoutInflater.inflate(R.layout.book_global_extra_view, null);
        TextView txtGlobal = layout.findViewById(R.id.txtGlobal);
        TextView txtPrice = layout.findViewById(R.id.txtPrice);
        TextView txtTotal = layout.findViewById(R.id.txtSubtotal);
        layout.setTag(i);
        if (name.length()>0 && price.length()>0){
            txtGlobal.setText(name +" : "+ number);
            txtPrice.setText(getResources().getString(R.string.price) + currency
                    + Utility.convertCuurency(Double.parseDouble(price), getApplicationContext()));
            double d = Double.parseDouble(price);
            double total = d*Integer.parseInt(number);
            txtTotal.setText(getResources().getString(R.string.sub_total) +
                    currency + Utility.convertCuurency(total, getApplicationContext()));
        }

        ImageView buttonRemove = layout.findViewById(R.id.imgCross);
        buttonRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((LinearLayout)layout.getParent()).removeView(layout);
                if (extraData != null){
                    extraData.remove((int)layout.getTag());
                    extraPrice = getTotal(extraData);
                    txtAddExtraChanrge.setText("Total : "+curren +Utility.convertCuurency(extraPrice, getApplicationContext()) );
                }
            }});
        addExtra.addView(layout);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.bottomToolBar);
        TextView textView = toolbar.findViewById(R.id.txt_bot);
        textView.setText(getResources().getString(R.string.book_this_car));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flight_no = edtflight.getText().toString().trim();
                Intent it = new Intent(BookCarActivity.this, Pay1Activity.class)
                        .putExtra("activity","BookCar")
                        .putExtra("extraPrice", extraPrice);
                startActivity(it);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Uri url=Uri.parse(termsurl);
        switch (view.getId()){
            case R.id.txt_terms:
                if (url!=null){
                    try {
                        Log.d(TAG, "onClick: "+url);
                        Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                        intent.putExtra("data", termsurl);
                        intent.putExtra("car_type", car_type);
                        startActivity(intent);

                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "No application can handle this request."
                                + " Please install a web browser",  Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void removeExtra(int pos) {
        extraData.remove(pos);
        if (extrasDetailAdapter!=null){
            extraPrice = getTotal(extraData);
            txtAddExtraChanrge.setText("Total : "+curren +Utility.convertCuurency(extraPrice, getApplicationContext()) );
            extrasDetailAdapter.notifyDataSetChanged();
        }
    }
}
