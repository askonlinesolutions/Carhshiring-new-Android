package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class CarListCategory extends RecyclerView.Adapter<CarListCategory.MyViewHolder> {
    private TinyDB tinyDB;

    OnItemClickListenerCategory listener;
    double markUp;
    //private List<String> horizontalList;
    public Activity context;
    private List<TestData>catBeanList = new ArrayList<>();
    private List<SearchData>listCarResult = new ArrayList<>();

    public interface  OnItemClickListenerCategory {
        void onItemClickCategory(int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        LinearLayout catLayout;
        public TextView txtCode;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.car_cat_name);
            txtCode = (TextView) view.findViewById(R.id.car_cat_code);
            catLayout = view.findViewById(R.id.cat_layout);
            image = (ImageView) view.findViewById(R.id.car_cat_image);
         /*  CarsResultListActivity. allView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index=-1;
                    notifyDataSetChanged();
                    CarsResultListActivity. allView.setBackgroundColor(Color.parseColor("#079607"));
                }
            });*/
        }
        void bindListener(final int position, final OnItemClickListenerCategory listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CarsResultListActivity.row_index=position;
                    notifyDataSetChanged();
                    listener.onItemClickCategory(position);
                }
            });


        }
    }

    private List<Category.ResponseBean.CatBean> catListdata= new ArrayList<>();

    public CarListCategory(Activity context, List<SearchData> listCarResult,
                           List<Category.ResponseBean.CatBean> catListdata,
                           List<TestData> catBeanList,
                           OnItemClickListenerCategory listener) {
        this.listener = listener;
        this.context = context;
        this.catBeanList = catBeanList;
        this.catListdata = catListdata;
        this.listCarResult = listCarResult;
        tinyDB = new TinyDB(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_category, parent, false);

        return new MyViewHolder(itemView);
    }
    private static DecimalFormat df2 = new DecimalFormat(".##");
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtName.setText(catBeanList.get(position).getCat_name());
        if (catBeanList.get(position).getCat_id().equals(catListdata.get(position).getCategory_id())){
            Glide.with(context)
                    .load(RetrofitApiBuilder.IMG_BASE_URL + catListdata.get(position).getCategory_image())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                    .into(holder.image);
        }

        markUp = Double.parseDouble(SearchCarFragment.markup);
        if (catBeanList.get(position).getCat_min_price()!=null){
            String p = catBeanList.get(position).getCat_min_price().replace(",","");
            double price = Double.parseDouble(p);
            double priceNew  = price+(price*markUp)/100;
            String curen = tinyDB.getString("from_currency");
            holder.txtCode.setText(curen+  Utility.convertCuurency(priceNew, context));
        }


        holder.bindListener(position, listener);
        if(CarsResultListActivity.row_index==position){
            holder.catLayout.setBackgroundColor(Color.parseColor("#079607"));
        }
        else
        {
           holder.catLayout.setBackgroundResource(R.drawable.buttoncurve_caategory);
        }
    }

    @Override
    public int getItemCount() {
        return catBeanList.size();
    }
}