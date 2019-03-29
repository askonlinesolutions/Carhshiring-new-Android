package com.carshiring.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carshiring.R;
import com.carshiring.models.CardListModel;

import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.PaymentList> {
    List<CardListModel> list;
    Context mContext;
    private Clicklistner clicklistner;
    public interface Clicklistner
    {
        void itemclick(View v, int post);
    }
    public PaymentListAdapter(Context mContext, List<CardListModel> list) {
        this.mContext=mContext;
        this.list=list;
    }

    @Override
    public PaymentList onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlistrow,parent,false);
        return new PaymentList(row);
    }

    @Override
    public void onBindViewHolder(PaymentList holder, int position) {
        CardListModel model=list.get(position);
        holder.txt_CardType.setText(model.getCard_type());
        holder.txt_Name.setText(model.getCard_name());
        String number=model.getCard_number();
        String CreditCardFormat="XXXX XXXX XXXX "+number.substring(number.length()-4);
        holder.txt_Cardno.setText(CreditCardFormat);
        holder.txt_CardExpiry.setText(model.getCard_date());
    }

    public void setclick(Clicklistner clicklistner1)
{
    clicklistner=clicklistner1;
}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentList extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_Name,txt_Cardno,txt_CardExpiry,txt_CardType;
        public PaymentList(View itemView) {
            super(itemView);
            txt_Name= (TextView) itemView.findViewById(R.id.txt_Cardname);
            txt_Cardno= (TextView) itemView.findViewById(R.id.txt_Cardno);
            txt_CardExpiry= (TextView) itemView.findViewById(R.id.txt_CardExpiry);
            txt_CardType= (TextView) itemView.findViewById(R.id.txt_Cardtype);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        if(clicklistner!=null)
        {
            clicklistner.itemclick(itemView,getPosition());
        }
        }
    }
}



