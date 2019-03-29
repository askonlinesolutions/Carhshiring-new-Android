package com.carshiring;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.mukesh.tinydb.TinyDB;

import java.util.Locale;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        if ( new TinyDB(this).getString("language_id")!=null){
            String languagecode = new TinyDB(this).getString("language_id");
            setLanguages(languagecode);
        }

    }

    public void setLanguages(String language_code){
        Locale locale = new Locale(language_code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

}
