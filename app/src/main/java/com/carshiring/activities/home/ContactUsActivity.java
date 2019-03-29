package com.carshiring.activities.home;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.models.ContactUs;
import com.carshiring.models.SearchData;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ContactUsActivity extends AppBaseActivity {

    private TinyDB sharedpref;
    private String language_code;

    private TextView tv_supplier, tv_pvt, tv_gen_query, tv_title, phone, address;
    private LinearLayout layout_supplier, layout_pvt, layout_gen_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        sharedpref = new TinyDB(getApplicationContext());
        language_code = sharedpref.getString("language_code");
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            actionBar.setTitle(getResources().getString(R.string.action_contact_us));
        }

        getData();
    }


    public ContactUs res = new ContactUs();
    private void getData(){
        Utility.showLoading(this,getResources().getString(R.string.loading));
        RetroFitApis retroFitApis = RetrofitApiBuilder.getCargHiresapis() ;

        Call<ApiResponse> responseCall = retroFitApis.contact_us(language_code);
        final Gson gson = new Gson();

        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.hidepopup();

                if (response.body() != null) {
                    if (response.body().status) {
                        res = response.body().response.contact_us;
                        Log.d(TAG, "onResponse: contact"+gson.toJson(res));
                        address = (TextView) findViewById(R.id.contact_us_address);
                        phone = (TextView) findViewById(R.id.contact_us_phone);
                        TextView email = (TextView) findViewById(R.id.contact_us_email);
                        address.setText(res.getAddrees());
                        phone.setText(res.getInquiry());
                        email.setText(res.getReservation_cancellation_issues_contact());


                        SpannableString spanString = new SpannableString(res.getReservation_cancellation_issues_contact());
                        Matcher matcher = Pattern.compile( "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+").matcher(spanString);

                        while (matcher.find())
                        {
                            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")), matcher.start(), matcher.end(), 0); //to highlight word havgin '@'
                            final String tag = matcher.group(0);
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View textView) {
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{res.getReservation_cancellation_issues_contact()
                                            .replace("For reservation & cancellation issues contact us on ","")});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                                    email.putExtra(Intent.EXTRA_TEXT, "message");
                                    email.setType("message/rfc822");
                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                }
                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);

                                }
                            };
                            spanString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        email.setText(spanString);
                        email.setMovementMethod(LinkMovementMethod.getInstance());



                        final SpannableString spanString1 = new SpannableString(res.getInquiry());
                        Matcher matcher1 = Pattern.compile( "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+").matcher(spanString1);

                        while (matcher1.find())
                        {
                            spanString1.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")), matcher1.start(), matcher1.end(), 0); //to highlight word havgin '@'
                            final String tag = matcher1.group(0);
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View textView) {
                                    Log.e("click", "click " + tag);
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{res.getInquiry().replace("Any general inquiry ","")});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                                    email.putExtra(Intent.EXTRA_TEXT, "message");
                                    email.setType("message/rfc822");
                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                }
                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);

                                }
                            };
                            spanString1.setSpan(clickableSpan, matcher1.start(), matcher1.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        phone.setText(spanString1);
                        phone.setMovementMethod(LinkMovementMethod.getInstance());

                    } else {
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                Log.d(TAG, "onFailure: "+t.getMessage());

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

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