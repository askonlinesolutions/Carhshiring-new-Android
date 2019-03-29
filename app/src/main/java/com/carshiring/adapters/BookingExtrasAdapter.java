package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.carshiring.R;
import com.carshiring.models.BookingHistory;
import com.carshiring.models.ExtraAdded;
import com.carshiring.models.ExtraBean;
import com.carshiring.utilities.Utility;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rakhi.
 * Contact Number : +91 9958187463
 */
public class BookingExtrasAdapter extends RecyclerView.Adapter<BookingExtrasAdapter.ViewHolder> {

    private List<BookingHistory.BookingExtraBean> booking_extra;
    private Context context;

    public BookingExtrasAdapter(List<BookingHistory.BookingExtraBean> booking_extra, Context context) {
        this.booking_extra = booking_extra;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.extraslist,parent,false);
        return new ViewHolder(row);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       holder.txt_extrasname.setText(booking_extra.get(position).getExtra_name());

        TinyDB tinyDB = new TinyDB(context);
        String curen = tinyDB.getString("from_currency");

        holder.checkBox.setVisibility(View.GONE);
/*
       holder.txt_price.setText(booking_extra.get(position).getExtra_currency()+" "+
               booking_extra.get(position).getExtra_price());
*/

        holder.txt_price.setText(curen+
                Utility.convertCuurency(Double.parseDouble(booking_extra.get(position).getExtra_price())
                        , context));

    }

    @Override
    public int getItemCount() {
        return booking_extra.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_extrasname,txt_price;
        /*Spinner spinner;*/
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_extrasname=itemView.findViewById(R.id.txt_extrasname);
            txt_price=itemView.findViewById(R.id.txt_price);
          //  spinner=itemView.findViewById(R.id.spinner2);
            checkBox = itemView.findViewById(R.id.check1);
        }
    }
}
