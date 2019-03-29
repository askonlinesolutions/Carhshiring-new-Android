package com.carshiring.activities.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carshiring.R;
import com.carshiring.models.AboutUs;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.Data;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.mukesh.tinydb.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends AppBaseActivity {


    private TinyDB sharedpref;
    private String language_code ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        sharedpref=new TinyDB(getApplicationContext());
        language_code = sharedpref.getString("language_code");

        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.action_about_us));
        getAboutUsDetails();
    }

    private void getAboutUsDetails() {
        RetroFitApis retroFitApis= RetrofitApiBuilder.getCargHiresapis();
        Call<ApiResponse> responseCall=retroFitApis.about_us(language_code);
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.body()!=null){
                    if(response.body().status)
                    {
                        Data data  = response.body().response ;
                        if(data!=null){
                            AboutUs about_us = data.about_us ;
                            String banner  = about_us.cms_banner ;
                            String content  =about_us.cms_language_content ;
                            String title  =about_us.cms_language_title ;
                            final ImageView ivBannerAboutUs = (ImageView)findViewById(R.id.ivBannerAboutUs);
                            if(banner!=null && !banner.isEmpty()){
                                String imagePath  = RetrofitApiBuilder.IMG_BASE_URL + banner ;
                                Log.d("Image URL",RetrofitApiBuilder.IMG_BASE_URL + banner);
                                Glide.with(getApplicationContext())
                                        .load(imagePath)
                                        .into(ivBannerAboutUs);
                            }
                            final TextView tvContentAboutUs = (TextView)findViewById(R.id.tvContentAboutUs);
                            actionBar.setTitle(title);
                            content = content.replaceAll("<(.*?)>","");//Removes all items in brackets
                            content = content.replaceAll("&nbsp;"," ");
                            content = content.replaceAll("&amp;"," ");
                            content = content.replaceAll("&#39;","'");
                            content = content.replaceAll("&ndash;","-");
                            tvContentAboutUs.setText(content) ;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
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
