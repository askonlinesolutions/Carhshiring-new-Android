package com.carshiring.activities.home;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.utilities.AppBaseActivity;

public class TermsActivity extends AppBaseActivity {
    private WebView mWebview;
    private ActionBar actionBar;
    private String voucher, car_type;

    @SuppressLint("SetJavaScriptEnabled")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Create progressBar dynamically...
        final ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mWebview = new WebView(this);
        String url = getIntent().getStringExtra("data");
        if (getIntent().hasExtra("voucher")) {
            voucher = getIntent().getStringExtra("voucher");
        }
        if (getIntent().hasExtra("car_type"))
            car_type = getIntent().getStringExtra("car_type");

     /*   mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);*/
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        final Activity activity = this;
        /*mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);*/

        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle("Loading...");
                progressBar.setVisibility(View.VISIBLE);
                activity.setProgress(progress * 100);
                if (progress == 100){
                    activity.setTitle(R.string.app_name);
                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }
        });

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        if (voucher != null) {
            actionBar.setTitle(voucher);
            mWebview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
        } else if (car_type!=null && !car_type.equals("carnect")){
            mWebview.loadUrl(url);
            actionBar.setTitle(getResources().getString(R.string.action_terms));
        } else {
            mWebview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            actionBar.setTitle(getResources().getString(R.string.action_terms));
        }

        setContentView(mWebview);

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

}
