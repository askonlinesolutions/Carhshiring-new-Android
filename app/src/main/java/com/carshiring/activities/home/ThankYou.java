package com.carshiring.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carshiring.R;


public class ThankYou extends AppCompatActivity implements View.OnClickListener {
    Button bt_viewquotes;
    TextView txtBookingid;
    String bookingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        bt_viewquotes= (Button) findViewById(R.id.bt_viewquotes);
        bt_viewquotes.setOnClickListener(this);
        ActionBar actionBar=getSupportActionBar();

        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            actionBar.setTitle(getResources().getString(R.string.thankyou));
        }
        txtBookingid = findViewById(R.id.thank_bookingid);
        if (getIntent().hasExtra("bookingid")){
            bookingId = getIntent().getStringExtra("bookingid");
            txtBookingid.setText(getResources().getString(R.string.dummy)+ " : "+ bookingId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(ThankYou.this, MainActivity.class);
        intent.putExtra("From Payment","Payment");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        finish();
    }
}
