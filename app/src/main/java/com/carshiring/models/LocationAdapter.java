package com.carshiring.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.carshiring.R;
import com.carshiring.webservices.Location;

import java.util.List;
import java.util.Locale;

import static com.carshiring.fragments.SearchCarFragment.languagecode;

/**
 * Created by Rakhi on 09/02/2018.
 * Contact Number : +91 9958187463
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {


    private final OnItemClickListener listener;
    List<Location> list ;

    public interface OnItemClickListener {
        void OnItemClick(Location location) ;
    }

    public LocationAdapter(List<Location> list, OnItemClickListener listener) {
        this.list  = list ;
        this.listener =  listener ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_item_view,parent,false) ;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Location location = list.get(position);
        String cityname="";
        if (location.getCountry_code()!=null && location.getCountry_code().length()>0){
            Locale loc = new Locale(languagecode,location.getCountry_code());
            String s = loc.getDisplayCountry();
            cityname = location.getCity_name();
            cityname = cityname+" , "+ s;
        } else  {
            cityname = location.getCity_name();
        }

        holder.view.setText(cityname);
        holder.bindListener(location,listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView view ;
        private  View itenView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itenView = itemView ;
            view = (TextView) itemView.findViewById(R.id.tvName) ;
        }

        public void bindListener(final Location location, final OnItemClickListener listener) {
            itenView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(location);
                }
            });
        }
    }
}


