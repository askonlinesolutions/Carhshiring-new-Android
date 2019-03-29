package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.carshiring.R;
import com.carshiring.models.ExtraAdded;
import com.carshiring.models.ExtraBean;
import com.carshiring.utilities.Utility;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

import static com.carshiring.activities.home.CarDetailActivity.currency;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class ExtrasDetailAdapter extends RecyclerView.Adapter<ExtrasDetailAdapter.ViewHolder> {
    private List<ExtraBean> extraData ;
   private Context context;
   private RemoveExtra removeExtra;

    public ExtrasDetailAdapter(List<ExtraBean> extraData, Context context, RemoveExtra removeExtra) {
        this.extraData = extraData;
        this.context = context;
        this.removeExtra = removeExtra;
    }

    public interface RemoveExtra{
       void removeExtra(int pos);
   }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_global_extra_view,parent,false);
        return new ViewHolder(row);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtGlobal.setText(extraData.get(position).getName()+" : "+1);
        String price = extraData.get(position).getPrice();
        holder.txtPrice.setText(context.getString(R.string.price) + currency
                + Utility.convertCuurency(Double.parseDouble(price), context));
        double d = Double.parseDouble(price);
        double total = d*1;
        holder.txtTotal.setText(context.getString(R.string.sub_total) +
                currency + Utility.convertCuurency(total, context));
    }

    @Override
    public int getItemCount() {
        return extraData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPrice,txtGlobal,txtTotal;
        public ViewHolder(View itemView) {
            super(itemView);
            txtGlobal = itemView.findViewById(R.id.txtGlobal);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTotal = itemView.findViewById(R.id.txtSubtotal);

            ImageView buttonRemove = itemView.findViewById(R.id.imgCross);
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeExtra.removeExtra(getAdapterPosition());
                }
            });
        }
    }

}
