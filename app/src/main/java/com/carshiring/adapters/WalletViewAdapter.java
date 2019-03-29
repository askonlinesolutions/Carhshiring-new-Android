package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.carshiring.R;
import com.carshiring.models.Category;
import com.carshiring.models.PointHistoryData;
import com.carshiring.models.SearchData;
import com.carshiring.models.WalletHistoryData;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class WalletViewAdapter extends RecyclerView.Adapter<WalletViewAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_booking_date, tv_booking_type, tv_booking_amount, tv_booking_id, tv_booking_point;

        public MyViewHolder(View view) {
            super(view);

            tv_booking_id = (TextView) view.findViewById(R.id.layout_wallet_view_booking_id);
            tv_booking_amount = (TextView) view.findViewById(R.id.layout_wallet_view_booking_amount);
            tv_booking_date = (TextView) view.findViewById(R.id.layout_wallet_view_booking_date);
            tv_booking_point = (TextView) view.findViewById(R.id.layout_wallet_view_booking_point);
            tv_booking_type = (TextView) view.findViewById(R.id.layout_wallet_view_booking_type);
        }
    }

    private Context context;
    private List<WalletHistoryData> walletHistoryData = new ArrayList<>();

    public WalletViewAdapter(Context context, List<WalletHistoryData> walletHistoryData) {
        this.context = context;
        this.walletHistoryData = walletHistoryData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wallet_view, parent, false);

        //Toast.makeText(context, walletHistoryData.size() + "", Toast.LENGTH_SHORT).show();

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //  Toast.makeText(context, walletHistoryData.size() + "", Toast.LENGTH_SHORT).show();
        TinyDB tinyDB = new TinyDB(context);
        String curen = tinyDB.getString("from_currency");
        holder.tv_booking_id.setText(context.getResources().getString(R.string.booking_id)+" : " +walletHistoryData.get(position).getBooking_id());
        holder.tv_booking_amount.setText(context.getResources().getString(R.string.txtWal)+" : "+curen
                + Utility.convertCuurency(Double.parseDouble(walletHistoryData.get(position).get_$WalletAmount169()), context));
        holder.tv_booking_date.setText(context.getResources().getString(R.string.transaction_date)+" : "+walletHistoryData.get(position).getBooking_date());
        if (walletHistoryData.get(position).get_$WalletType204()!=null){
            if (walletHistoryData.get(position).get_$WalletType204().equalsIgnoreCase("credit")){
                holder.tv_booking_type.setText(walletHistoryData.get(position).get_$WalletType204()+" For Booking");
            } else if (walletHistoryData.get(position).get_$WalletType204().equalsIgnoreCase("debit")){
                holder.tv_booking_type.setText(walletHistoryData.get(position).get_$WalletType204()+" For Cancelation");
            }
        }
        holder.tv_booking_point.setText(context.getResources().getString(R.string.remain_wallet)+" : "+curen+Utility
                .convertCuurency(Double.parseDouble(walletHistoryData.get(position).get_$WalletAffetctedAmount6()),context));
    }

    @Override
    public int getItemCount() {
        return walletHistoryData.size();
    }
}