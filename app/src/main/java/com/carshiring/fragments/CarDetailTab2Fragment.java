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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.carshiring.R;
import com.carshiring.activities.home.ExcessProtectionActivity;
import com.carshiring.activities.home.TermsActivity;

import static com.carshiring.activities.home.CarDetailActivity.car_type;
import static com.carshiring.activities.home.CarDetailActivity.logo;
import static com.carshiring.activities.home.CarDetailActivity.suppliercity;
import static com.carshiring.activities.home.CarDetailActivity.suppliername;
import static com.carshiring.activities.home.CarDetailActivity.termsurl;


/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class CarDetailTab2Fragment extends Fragment implements View.OnClickListener {
    View view;
    TextView terms,quotes,txt_supdetail,txt_suploc;
    ImageView img_sup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.cardetail_tab2,container,false);
        img_sup=view.findViewById(R.id.img_supl);
        Glide.with(getContext()).load(logo).into(img_sup);
        terms= (TextView) view.findViewById(R.id.txt_terms);
        quotes=(TextView) view.findViewById(R.id.txt_savequote);
        txt_supdetail=(TextView) view.findViewById(R.id.txt_supdetail);
        txt_suploc=view.findViewById(R.id.txt_suploc);
        txt_supdetail.setText(getResources().getString(R.string.your_supplier_is)+" " + suppliername);
        txt_suploc.setText(suppliercity);
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
                Intent it=new Intent(getActivity(), ExcessProtectionActivity.class);
                it.putExtra("get","Forquotes");
                startActivity(it);
                break;
        }
    }
}