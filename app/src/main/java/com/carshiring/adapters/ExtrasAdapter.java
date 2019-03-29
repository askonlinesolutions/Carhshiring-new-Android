package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.models.ExtraAdded;
import com.carshiring.models.ExtraBean;
import com.carshiring.utilities.Utility;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class ExtrasAdapter extends RecyclerView.Adapter<ExtrasAdapter.ViewHolder> {
   public List<ExtraAdded> extraData;

    Context context;
    private static ArrayList<ExtraBean> beanArrayList;
    private ArrayList<ExtraBean> selectedExtraList;

    public ExtrasAdapter(Context context, ArrayList<ExtraBean> beanArrayList) {
        this.context = context;
        this.beanArrayList = beanArrayList;
        extraData = new ArrayList<>();
        selectedExtraList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.extraslist,parent,false);
        return new ViewHolder(row);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       holder.txt_extrasname.setText(beanArrayList.get(position).getName());
       if (extraData!=null){
           extraData.clear();
       }
       TinyDB tinyDB = new TinyDB(context);
       String curen = tinyDB.getString("from_currency");
       if (beanArrayList.get(position).isChecked){
            holder.checkBox.setChecked(true);
       }

       if (beanArrayList.get(position).getPrice()!=null&& !beanArrayList.get(position).getPrice().contains("null"))
           holder.txt_price.setText(curen+
               Utility.convertCuurency(Double.parseDouble(beanArrayList.get(position).getPrice()), context));
       else  holder.txt_price.setText(curen+0.00);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if (b){
                  beanArrayList.get(position).setChecked(true);
                  beanArrayList.get(position).setQty("1");
                  ExtraAdded extraAdded = new ExtraAdded();
                  extraAdded.setName(holder.txt_extrasname.getText().toString().trim());
                  extraAdded.setPrice(beanArrayList.get(position).getPrice());
                  extraAdded.setCurrency(beanArrayList.get(position).getCurrency());
                  extraAdded.setQty("1");
                  extraAdded.setId(beanArrayList.get(position).getType());
                  extraData.add(extraAdded);
              } else {
                  beanArrayList.get(position).setChecked(false);
//                  extraData.remove(position);
              }
          }
      });

    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
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

    public ArrayList<ExtraBean> getExtra(){
        if (beanArrayList!=null){
            for (int i =0; i<beanArrayList.size();i++){
                if (beanArrayList.get(i).isChecked){
                    selectedExtraList.add(beanArrayList.get(i));
                }
            }
        }
        return selectedExtraList;
    }

    public double getTotal(){
        double sum = 0;
        for (int i =0; i<selectedExtraList.size();i++){
            sum = sum+ Double.parseDouble(selectedExtraList.get(i).getPrice());
        }
        return sum;
    }
}
