package com.carshiring.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.activities.home.TermsActivity;

import static com.carshiring.activities.home.CarDetailActivity.CDW;
import static com.carshiring.activities.home.CarDetailActivity.THP;
import static com.carshiring.activities.home.CarDetailActivity.car_type;
import static com.carshiring.activities.home.CarDetailActivity.driver_maxage;
import static com.carshiring.activities.home.CarDetailActivity.driver_minage;
import static com.carshiring.activities.home.CarDetailActivity.termsurl;


/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class CarDetailTab3Fragment extends Fragment implements View.OnClickListener {

    View view;
    TextView terms,quotes,txt_driverage,txt_cdw,txt_theftpro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.cardetail_tab3,container,false);

        terms= (TextView) view.findViewById(R.id.txt_terms);
        quotes=(TextView) view.findViewById(R.id.txt_savequote);
        txt_driverage=view.findViewById(R.id.txt_driverage);
        txt_cdw=view.findViewById(R.id.txt_cdw);
        txt_theftpro=view.findViewById(R.id.txt_theftpro);
        txt_driverage.setText(getResources().getString(R.string.min_age) +
                driver_minage + "\n" + getResources().getString(R.string.max_age) + driver_maxage);
        txt_cdw.setText(CDW);
        txt_theftpro.setText(THP);
        terms.setOnClickListener(this);
        quotes.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        Uri url=Uri.parse(termsurl);

        switch (v.getId())
        {
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
             /*   Intent it=new Intent(getActivity(), ExcessProtectionActivity.class);
                it.putExtra("get","Forquotes");
                startActivity(it);
                break;*/
        }
    }
}