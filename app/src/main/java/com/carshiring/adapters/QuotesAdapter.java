package com.carshiring.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carshiring.R;
import com.carshiring.models.QuotesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rakhi on 13-03-2018.
 */

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.Holder> {
    List<QuotesModel> quoteslist;
    Context context;
    QuoteInterface quoteInterface;
    int lastpositon = -1;

    public QuotesAdapter(Context context, List<QuotesModel> quoteslist) {

        this.context = context;
        this.quoteslist = quoteslist;
    }

    public interface QuoteInterface {
        void QuoteInterfaceMethod(int position, String tag);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotes_row, parent, false);
        return (new Holder(v));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        QuotesModel model = quoteslist.get(position);
        holder.txtPickUp.setText(model.getSavelater_pick_city());
        holder.txtDrop.setText(model.getSavelater_drop_city());
        holder.txtDropTime.setText((String)model.getSavelater_drop_date());
        holder.txtPickTime.setText((String)model.getSavelater_pick_date());
        holder.txtModelName.setText(model.getSavelater_carnect_model());
        holder.txtPoint.setText(model.getSavelater_carnect_model());

        Glide.with(context)
                .load(model.getSavelater_carnect_image())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                .into(holder.imgCar);
    }

    @Override
    public int getItemCount() {
        return quoteslist.size();
    }

    public void QuoteAdapterMethod(QuoteInterface quoteInterface) {
        this.quoteInterface = quoteInterface;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Reference, Rate,txtModelName,txtPoint, txtPickUp, txtDrop, txtPickTime, txtDropTime;
        ImageView imgCar;
        Button btnBook, btnCancel;
        public Holder(View view) {
            super(view);
            Reference = (TextView) view.findViewById(R.id.txt_Quote_refnumb);
            Rate = (TextView) view.findViewById(R.id.txt_quoterate);
            txtPickUp = view.findViewById(R.id.txt_quoteaddress_pick);
            txtDrop = view.findViewById(R.id.txt_quoteaddress_drop);
            imgCar = view.findViewById(R.id.imgCar_quote);
            txtPickTime = view.findViewById(R.id.txtPicktimeQuote);
            txtDropTime = view.findViewById(R.id.txtdroptimeQuote);
            txtModelName = view.findViewById(R.id.txtModelNameQuote);
            txtPoint = view.findViewById(R.id.txtPointQuote);
            btnBook = view.findViewById(R.id.quotes_btn_book_now);
            btnCancel = view.findViewById(R.id.quotes_cancel);
            btnBook.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.quotes_cancel:
                    if (quoteInterface != null) {
                        quoteInterface.QuoteInterfaceMethod(getAdapterPosition(),"delete");
                    }
                    break;
                case R.id.quotes_btn_book_now:
                    if (quoteInterface != null) {
                        quoteInterface.QuoteInterfaceMethod(getAdapterPosition(),"book");
                    }
                    break;
            }
        }

    }
}

