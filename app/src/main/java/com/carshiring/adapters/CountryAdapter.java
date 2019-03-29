package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carshiring.R;
import com.carshiring.activities.home.CarsResultListActivity;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.Category;
import com.carshiring.models.SearchData;
import com.carshiring.models.TestData;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.mukesh.tinydb.TinyDB;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rakhi.
 * Contact Number : +91 9958187463
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    private ArrayList<String>countryList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public CountryAdapter(ArrayList<String> countryList, Context mContext, OnItemClickListener onItemClickListener) {
        this.countryList = countryList;
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickCategory(int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;

        public MyViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickCategory(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_country, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtName.setText(countryList.get(position));
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }
}