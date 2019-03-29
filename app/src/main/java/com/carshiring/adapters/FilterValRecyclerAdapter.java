package com.carshiring.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.carshiring.R;
import com.carshiring.activities.home.FilterListActivity;
import com.carshiring.models.FilterDefaultMultipleListModel;

import java.util.ArrayList;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class FilterValRecyclerAdapter extends RecyclerView.Adapter<FilterValRecyclerAdapter.ViewHolder>{
    Context context;
    int resource;
    ArrayList<FilterDefaultMultipleListModel> filterModels;
    ArrayList<FilterDefaultMultipleListModel> selected=new ArrayList<>();
    OnClickItem clickItem;
    ArrayList<String>selecteds;

    public interface OnClickItem
    {
        void itemclick(View v, int i);
    }

    public FilterValRecyclerAdapter(Context context, int resource,
                                    ArrayList<FilterDefaultMultipleListModel> filterModels, ArrayList<String>selected) {
        this.context = context;
        this.resource = resource;
        this.filterModels = filterModels;
        this.selecteds = selected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(resource,parent,false);
        return new ViewHolder(view);
    }

    String c,s;
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.subCategoryName.setText(filterModels.get(i).getName());
        for (int j=0;j<selecteds.size();j++){
            c = selecteds.get(j);
            for (int k=0;k<filterModels.size();k++){
                s = filterModels.get(k).getName();
                if (c.equalsIgnoreCase(s)){
                    holder.cbSelected.setChecked(true);

                }
            }
        }
        holder.cbSelected.setChecked(filterModels.get(i).isChecked());

    }

    @Override
    public int getItemCount() {
        return filterModels.size();
    }

    public void setonclick(OnClickItem clickItem)
    {
        this.clickItem=clickItem;
    }

    public void setitemselected(int position) {
        if(position!=-1) {
            filterModels.get(position).setChecked(!filterModels.get(position).isChecked());
            notifyDataSetChanged();
        }
    }

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subCategoryName;
        CheckBox cbSelected;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            subCategoryName = (TextView) itemView.findViewById(R.id.txt_item_list_title);
            cbSelected = (CheckBox) itemView.findViewById(R.id.cbSelected);
        }

        @Override
        public void onClick(View v) {
            if(clickItem!=null)
            {
                clickItem.itemclick(v,getAdapterPosition());
            }
        }
    }
}
