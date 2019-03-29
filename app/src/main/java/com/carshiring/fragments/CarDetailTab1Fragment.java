package com.carshiring.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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

import com.carshiring.activities.home.BookCarActivity;
import com.carshiring.activities.home.CarDetailActivity;
import com.carshiring.activities.home.ExcessProtectionActivity;
import com.carshiring.activities.home.Extras;
import com.carshiring.activities.home.TermsActivity;
import com.carshiring.activities.mainsetup.LoginActivity;
import com.carshiring.models.CarDetailBean;
import com.carshiring.models.ExtraBean;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carshiring.activities.home.CarDetailActivity.carImage;
import static com.carshiring.activities.home.CarDetailActivity.carSpecificationList;
import static com.carshiring.activities.home.CarDetailActivity.car_type;
import static com.carshiring.activities.home.CarDetailActivity.day;
import static com.carshiring.activities.home.CarDetailActivity.fullAmtValue;
import static com.carshiring.activities.home.CarDetailActivity.fullprotectionammount;
import static com.carshiring.activities.home.CarDetailActivity.fullprotectioncurrency;
import static com.carshiring.activities.home.CarDetailActivity.point;
import static com.carshiring.activities.home.CarDetailActivity.refer_type;
import static com.carshiring.activities.home.CarDetailActivity.termsurl;
import static com.carshiring.activities.home.CarDetailActivity.time;
import static com.carshiring.activities.home.CarDetailActivity.type;
import static com.carshiring.activities.home.Extras.extraData;
import static com.carshiring.splash.SplashActivity.TAG;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class CarDetailTab1Fragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout ll_extra,ll_protection;
    private TextView terms,quotes,carname,carprice, txtPoint;
    ImageView carImg,imglogo;
    ProgressBar bar,bar1;
    LinearLayout ll,gl;
    UserDetails userDetails  = new UserDetails();
    Gson gson = new Gson();
    String userId,language, carnect_id,car_model,carnect_type,pick_city,pick_houre,pick_minute,
            pick_datetyme,drop_city,pick_date,drop_date,drop_houre,drop_minute,drop_datetyme,age,image,booking_price;
    TinyDB tinyDB ;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cardetail_tab1,container,false);
        ll_extra = view.findViewById(R.id.ll_extra);
        ll_protection = view.findViewById(R.id.ll_protection);
        terms = view.findViewById(R.id.txt_terms);
        quotes = view.findViewById(R.id.txt_savequote);
        carname = view.findViewById(R.id.txt_modelname);
        carprice = view.findViewById(R.id.txt_carPrice);
        carImg = view.findViewById(R.id.img_car);
        imglogo = view.findViewById(R.id.img_carlogo);
        bar = view.findViewById(R.id.progressbar);
        bar1 = view.findViewById(R.id.progressbar1);
        txtPoint = view.findViewById(R.id.point_get);
        TextView txtOneway = view.findViewById(R.id.oneway);
        tinyDB = new TinyDB(getContext());

        String s= tinyDB.getString("login_data");
        userDetails = gson.fromJson(s, UserDetails.class);
        if (userDetails!=null&&userDetails.getUser_id()!=null){
            userId = userDetails.getUser_id();
        }
        else {
            quotes.setVisibility(View.VISIBLE);
        }
        language = tinyDB.getString("language_code");
        carnect_id = CarDetailActivity.id_context;
        car_model = CarDetailActivity.modelname;

        TextView tvFromDate = view.findViewById(R.id.tvFromDT);
        TextView tvPickDate = view.findViewById(R.id.txtPlaceName);
        TextView tvTodate = view.findViewById(R.id.tvToDT);
        TextView txtPlaceDrop = view.findViewById(R.id.txtPlaceName_drop);
        TextView txtDriverSur = view.findViewById(R.id.driverSurCharge);
        ll = view.findViewById(R.id.ll_otherspec);
        gl =  view.findViewById(R.id.gr_Spec);
        tvFromDate.setText(SearchCarFragment.pick_date+"\n"+ SearchCarFragment.pickTime);
        tvPickDate.setText(SearchCarFragment.pickName);
        tvTodate.setText(SearchCarFragment.drop_date+"\n"+SearchCarFragment.dropTime);
        txtPlaceDrop.setText(SearchCarFragment.dropName);

        if (CarDetailActivity.oneway!=null){
            txtOneway.setText(CarDetailActivity.oneway);
            txtOneway.setVisibility(View.VISIBLE);
        } else {
            txtOneway.setVisibility(View.GONE);
        }
        if (CarDetailActivity.driverSur!=null){
            txtDriverSur.setText(CarDetailActivity.driverSur);
            txtDriverSur.setVisibility(View.VISIBLE);
        } else {
            txtDriverSur.setVisibility(View.GONE);
        }
        if (CarDetailActivity.extralist!=null&&CarDetailActivity.extralist.size()>0){
            for (ExtraBean extraBean: CarDetailActivity.extralist){
                if (extraBean.getName()!=null&&!extraBean.getName().equalsIgnoreCase("null")){
                    ll_extra.setVisibility(View.VISIBLE);
                } else {
                    ll_extra.setVisibility(View.GONE);
                }
            }
        } else {
            ll_extra.setVisibility(View.GONE);
        }

        if (CarDetailActivity.fullprotectionammount == null || CarDetailActivity.fullprotectionammount.equals("0")){
            ll_protection.setVisibility(View.GONE);
        } else ll_protection.setVisibility(View.GONE);//should be visible

        carnect_type = CarDetailActivity.type;
        pick_city = SearchCarFragment.pickName;
        pick_houre = SearchCarFragment.pick_hour;
        pick_minute = SearchCarFragment.pick_minute;
        pick_date = SearchCarFragment.pick_date;
        pick_datetyme=pick_date +" "+SearchCarFragment.pickTime;
        drop_city = SearchCarFragment.dropName;
        drop_date = SearchCarFragment.drop_date;
        drop_houre = SearchCarFragment.drop_hour;
        drop_minute = SearchCarFragment.drop_minute;
        drop_datetyme= drop_date +" "+SearchCarFragment.dropTime;
        booking_price = CarDetailActivity.currency  + Utility.convertCuurency(Double.parseDouble(CarDetailActivity.carPrice), getContext());
        image = CarDetailActivity.carImage;
        if (userDetails!=null&&userDetails.getUser_age()!=null){
            age = userDetails.getUser_age();
        }

        ll_extra.setOnClickListener(this);
        ll_protection.setOnClickListener(this);
        terms.setOnClickListener(this);
        quotes.setOnClickListener(this);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bar.setVisibility(View.VISIBLE);
        bar1.setVisibility(View.VISIBLE);
       /* Glide.with(getContext()).load(carImage).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                bar.setVisibility(View.GONE);
                return false;
            }
            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                bar.setVisibility(View.GONE);
                return false;
            }
        }).into(carImg);*/
        if (carImage!=null){
            new AsyncCaller().execute();
        }

        Glide.with(getContext()).load(CarDetailActivity.logo).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                bar1.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                bar1.setVisibility(View.GONE);
                return false;
            }
        }).into(imglogo);
        carname.setText(CarDetailActivity.modelname );
        txtPoint.setText(getResources().getString(R.string.points_collected )+String.valueOf( CarDetailActivity.point));
        carprice.setText(CarDetailActivity.currency + Utility.convertCuurency(Double.parseDouble(CarDetailActivity.carPrice),getContext())+ "/ "
                + time + " "+ CarDetailActivity.day);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(carSpecificationList!=null) {
            for(int i=0;i<carSpecificationList.size();i++) {

                CarDetailBean.FeatureBean carSpecification = carSpecificationList.get(i);

                    if(carSpecification.passenger!=null)
                    {
                        TextView tt1 = new TextView(getContext());
                        tt1.setLayoutParams(lparams);
                        tt1.setText(carSpecification.getPassenger() + " " + getResources().getString(R.string.passanger));
                        tt1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_seat,0,0);
                        tt1.setCompoundDrawablePadding(5);
                        tt1.setTextSize(10);
                        tt1.setPadding(0,0,8,0);
                        tt1.setTypeface(Typeface.DEFAULT);
                        gl.addView(tt1);
                        gl.setOrientation(LinearLayout.HORIZONTAL);
                    }

                    if(!carSpecification.door.isEmpty()){
                        TextView tt1 = new TextView(getContext());
                        tt1.setLayoutParams(lparams);
                        tt1.setText(carSpecification.getDoor()+" "+ getResources().getString(R.string.doors));
                        tt1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_car_door,0,0);
                        tt1.setCompoundDrawablePadding(5);
                        tt1.setTextSize(10);
                        tt1.setPadding(5,0,8,0);
                        tt1.setTypeface(Typeface.DEFAULT);
                        gl.addView(tt1);
                        gl.setOrientation(LinearLayout.HORIZONTAL);
                    }

                    if(!carSpecification.transmission.isEmpty()){
                        TextView tt1 = new TextView(getContext());
                        tt1.setLayoutParams(lparams);
                        tt1.setText(carSpecification.getTransmission());
                        tt1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.manual,0,0);
                        tt1.setCompoundDrawablePadding(5);
                        tt1.setTextSize(10);
                        tt1.setPadding(5,0,8,0);
                        tt1.setTypeface(Typeface.DEFAULT);
                        gl.addView(tt1);
                        gl.setOrientation(LinearLayout.HORIZONTAL);
                    }

                    if (carSpecification.aircondition!=null &&
                            !carSpecification.aircondition.equalsIgnoreCase("false")) {
                        View viw = getActivity().getLayoutInflater().inflate(R.layout.gridcustomstyle, null);
                        // TextView tt = (TextView) viw.findViewById(R.id.txt_spec);
                        TextView tt1 = new TextView(getContext());
                        tt1.setLayoutParams(lparams);
                        tt1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_ac,0,0);
                        tt1.setCompoundDrawablePadding(5);
                        tt1.setTextSize(10);
                        tt1.setTypeface(Typeface.DEFAULT);
                        tt1.setText(getResources().getString(R.string.air_condition));
                        gl.addView(tt1);
                        tt1.setPadding(5,0,8,0);
                        gl.setOrientation(LinearLayout.HORIZONTAL);
                    }

                    if(!carSpecification.fueltype.isEmpty()) {
                        TextView tt1 = new TextView(getContext());
                        tt1.setLayoutParams(lparams);
                        tt1.setText(carSpecification.getFueltype());
//                        tt1.setText(getResources().getString(R.string.txtFuel));
                        tt1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_fuel,0,0);
                        tt1.setCompoundDrawablePadding(5);
                        tt1.setTextSize(10);
                        tt1.setPadding(5,0,8,0);
                        tt1.setTypeface(Typeface.DEFAULT);
                        gl.addView(tt1);
                        gl.setOrientation(LinearLayout.HORIZONTAL);
                    }
                    if(!carSpecification.bag.isEmpty())
                    {
                        TextView tt1 = new TextView(getContext());
                        tt1.setLayoutParams(lparams);
                        tt1.setText(carSpecification.getBag() + " " + getResources().getString(R.string.bags));
                        tt1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_bag,0,0);
                        tt1.setCompoundDrawablePadding(5);
                        tt1.setPadding(5,0,8,0);
                        tt1.setTextSize(10);
                        tt1.setTypeface(Typeface.DEFAULT);
                        gl.addView(tt1);
                        gl.setOrientation(LinearLayout.HORIZONTAL);
                    }

            }
        }
    }

    private class AsyncCaller extends AsyncTask<Integer, Void, Integer> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Integer doInBackground(Integer... params) {
            URL url = null;
            int s = 0;
            try {
                url = new URL(carImage);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection httpConn = null;
            try {
                httpConn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                httpConn.setInstanceFollowRedirects(false);
                httpConn.setRequestMethod("HEAD");
                httpConn.connect();
                s = httpConn.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return s;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d("TAG", "onPostExecute: "+result);

            //this method will be running on UI thread
            if (pdLoading!=null){
                pdLoading.dismiss();
            }

            if (result==404){
                Glide.with(getActivity()).load("").listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        bar.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        bar.setVisibility(View.GONE);
                        return false;
                    }
                }).apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                  .into(carImg);
            } else {
                if(getActivity() != null) {
                    Glide.with(getActivity()).load(carImage)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    bar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    bar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                            .into(carImg);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Uri url = Uri.parse(termsurl);
        Intent it=new Intent(getActivity(), ExcessProtectionActivity.class);
        switch (v.getId()) {
            case R.id.ll_extra:
                startActivity(new Intent(getActivity(),Extras.class));
                break;
            case R.id.ll_protection:
                it.putExtra("get","ForProtec");
                startActivity(it);
                break;
            case R.id.txt_terms:
                if (termsurl!=null){
                    try {
                        Intent intent = new Intent(getContext(), TermsActivity.class);
                        intent.putExtra("data", termsurl);
                        intent.putExtra("car_type", car_type);
                        startActivity(intent);
                       /* Intent intent=new Intent(Intent.ACTION_VIEW,url);
                        startActivity(intent);*/
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "No application can handle this request."
                                + " Please install a web browser",  Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.txt_savequote:
              /*  it.putExtra("get","Forquotes");
                startActivity(it);*/
              if (userId != null)
                savelater(userId);
              else {
                  startActivity(new Intent(getActivity(), LoginActivity.class));
                  Utility.message(getContext(),"Login Required ");
              }
              break;
        }
    }

    private void savelater(String userId) {
        if (BookCarActivity.extraData!=null){
            BookCarActivity.extraData.clear();
        }

        Utility.showloadingPopup(getActivity());
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();

        final Call<ApiResponse> bookingDataCall = fitApis.savelater(language,carnect_id,car_model,carnect_type,
                userId, pick_city, pick_date, pick_houre, pick_minute, pick_datetyme,drop_city,
                drop_date,drop_houre, drop_minute, drop_datetyme, age, image, booking_price,time,
                refer_type,type,point, SearchCarFragment.pick_city_id,SearchCarFragment.drop_city_id,car_type);

        bookingDataCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();
                Log.d(TAG, "onResponse: savelater"+gson.toJson(response.body()));

                if (response.body()!=null){
                    if (response.body().msg!=null)
                    Toast.makeText(getContext(), ""+response.body().msg, Toast.LENGTH_SHORT).show();
                } else {
                    if(response.body()!=null && response.body().msg!=null)
                    Utility.message(getContext(), response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                Utility.hidepopup();
            }
        });
    }

}
