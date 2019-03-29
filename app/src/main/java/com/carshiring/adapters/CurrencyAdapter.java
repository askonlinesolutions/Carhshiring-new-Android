package com.carshiring.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carshiring.R;
import com.carshiring.models.CardListModel;
import com.carshiring.models.CurrencyData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rakhi.
 * Contact Number : +91 9958187463
 * Date: 05/09/2018
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.PaymentList> {
    Context mContext;
    private Clicklistner clicklistner;
    private List<CurrencyData>currencyList;
    private String from_currency;
    public interface Clicklistner
    {
        void itemclick(int post);
    }

    public CurrencyAdapter(Context mContext, Clicklistner clicklistner, List<CurrencyData> currencyList,
                           String  from_currency) {
        this.mContext = mContext;
        this.clicklistner = clicklistner;
        this.currencyList = currencyList;
        this.from_currency = from_currency;
    }

    @Override
    public PaymentList onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency_row,parent,false);
        return new PaymentList(row);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(PaymentList holder, int position) {
        holder.txt_Name.setText(currencyList.get(position).getName());
        if (from_currency.equals(currencyList.get(position).getCurrency())){
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_select_filter));
        }

    }


    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class PaymentList extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_Name;
        ImageView imageView;
        public PaymentList(View itemView) {
            super(itemView);
            txt_Name= (TextView) itemView.findViewById(R.id.txt_currency);
            imageView = itemView.findViewById(R.id.item_currency_img_select);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        if(clicklistner!=null)
        {
            clicklistner.itemclick(getAdapterPosition());
        }
        }
    }
}



