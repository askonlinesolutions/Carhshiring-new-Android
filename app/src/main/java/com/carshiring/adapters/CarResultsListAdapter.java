package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carshiring.R;
import com.carshiring.activities.home.TermsActivity;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.Category;
import com.carshiring.models.SearchData;
import com.carshiring.models.TestData;
import com.carshiring.utilities.Utility;
import com.mukesh.tinydb.TinyDB;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.carshiring.splash.SplashActivity.TAG;


/**
 * Created by rakhi on 09/02/2018.
 * Contact Number : +91 9958187463
 */

public class CarResultsListAdapter extends RecyclerView.Adapter<CarResultsListAdapter.MyViewHolder>{
    final OnItemClickListener listener;
    private final Context context;
    List<SearchData> list;
    List<TestData>catBeanList;
    double pointpercent,calPrice, markUp;
    public static int calPoint;
    private TinyDB tinyDB;

    public interface  OnItemClickListener {
        void onItemClick(SearchData carDetail);
    }

    public CarResultsListAdapter(Context context , List<SearchData> list,List<TestData>catBeanList,
                                 OnItemClickListener listener){
        this.context = context ;
        this.list =  list ;
        this.catBeanList = catBeanList;
        this.listener =  listener ;
        tinyDB = new TinyDB(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  =  LayoutInflater.from(parent.getContext()).inflate(R.layout.search_car_result_item,parent,false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SearchData model=list.get(position);

        holder.tvCarModelName.setText(model.getModel());
        holder.txtSupplierNmae.setText(context.getResources().getString(R.string.supplied_by) + model.getSupplier());
        if (SearchCarFragment.location_type.equals("a")){
            holder.txtDropCity.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_drop, 0, 0, 0);
        } else holder.txtDropCity.setCompoundDrawablesWithIntrinsicBounds( R.drawable.skyline, 0, 0, 0);
        holder.txtDropCity.setText(SearchCarFragment.pickName);
        if (model.getFeature()!=null){
            if (model.getFeature().getBag().equals("0")){
                holder.tvBagNo.setVisibility(View.GONE);
            }
            holder.tvBagNo.setText(model.getFeature().getBag()+" " + context.getResources().getString(R.string.large_bag));
            holder.txtDoor.setText(model.getFeature().getDoor()+" "+ context.getResources().getString(R.string.doors));
            if (model.getFeature().getAircondition().equals("true")){
                holder.txtAc.setVisibility(View.VISIBLE);
            }
            if (model.getFeature().getPassengerQuantity()!=null && model.getFeature().getPassengerQuantity().length()>0)
            holder.tvPassanger.setText(model.getFeature().getPassengerQuantity()+ " "+ context.getResources().getString(R.string.passanger));
            else holder.tvPassanger.setVisibility(View.GONE);
            holder.txtTrans.setText(model.getFeature().getTransmission());
            holder.txtFuel.setText(model.getFeature().getFueltype());

        }
        if (SearchCarFragment.markup!=null&& SearchCarFragment.markup.length()>0)

        markUp = Double.parseDouble(SearchCarFragment.markup);
        String price = model.getPrice();
        String curen = tinyDB.getString("from_currency");
        double priceNew = 0;
        if (price!=null){
            double d = Double.parseDouble(price);
            priceNew  = d+(d*markUp)/100;
            Log.d(TAG, "onBindViewHolder: "+Utility.convertCuurency(priceNew, context));
            holder.tvCarPricing.setText(curen
                    + Utility.convertCuurency(priceNew, context)+" /"+ model.getTime()
                    +" "+model.getTime_unit());
        }

       /* holder.tvCarPricing.setText(model.getCurrency()
                +" "+String.valueOf(df2.format(priceNew))+" /"+ model.getTime()
                +" "+model.getTime_unit());*/

        for (TestData catBean: catBeanList){
            if (catBean.getCat_code()!=null){
                for (String s:catBean.getCat_code()){
                    if (s.equals(model.getCategory())){
                        holder.txtClass.setText(context.getResources().getString(R.string.txtClassType)
                                +" : "+catBean.getCat_name());
                    }
                }
            }
        }

        holder.txtDriverSur.setVisibility(View.GONE);
        holder.txtOneway.setVisibility(View.GONE);
        for (SearchData.CoveragesBean bean : list.get(position).getCoverages()){
            if (bean.getCode().equalsIgnoreCase("412")){
                String amt ;
                if (bean.getAmount2()!=null && bean.getAmount2().length()>0 && !bean.getAmount2().equals("null")){
                    amt = bean.getAmount2();
                } else amt="0.00";
                holder.txtOneway.setText(bean.getName() +" : "+ curen
                        +Utility.convertCuurency(Double.parseDouble(amt),context));
                holder.txtOneway.setVisibility(View.VISIBLE);
            } else if (bean.getCode().equalsIgnoreCase("410")){
                String amt;
                if (bean.getAmount2()!=null && !bean.getAmount2().equals("null")&&bean.getAmount2().length()>0){
                    amt = bean.getAmount2();
                } else amt = "0.00";
                holder.txtDriverSur.setText(bean.getName()+" : "+curen
                        + Utility.convertCuurency(Double.parseDouble(amt),context));
                holder.txtDriverSur.setVisibility(View.VISIBLE);
            }
        }

        holder.txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = model.getTc();
                Intent intent = new Intent(context, TermsActivity.class);
                intent.putExtra("data", url);
                intent.putExtra("car_type", model.getCar_type());
//              Intent intent = new Intent(Intent.ACTION_VIEW);
//              intent.setData(Uri.parse(url));
              context.startActivity(intent);
            }
        });

        String urla = model.getSupplier_logo();
        Glide.with(context)
                .load(urla)
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                .into(holder.imgCarAgencyLogo);

        Glide.with(context)
                .load(model.getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                .into(holder.imgCarResult);

//        calculate point
        pointpercent = Double.parseDouble(SearchCarFragment.pointper);
        calPrice = (priceNew*pointpercent)/100;
        calPoint = (int) (calPrice/0.02);
        holder.txtPoint.setText(context.getResources().getString(R.string.points_collected) + String.valueOf(calPoint));
        holder.bindListener(model,listener);
    }

    public static DecimalFormat df2 = new DecimalFormat("#0.00");

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCarModelName,tvCarPricing, txtAc,txtClass,txtSupplierNmae, tvBagNo, tvPassanger,
                txtDropCity,txtDoor,txtTrans, txtTerms,txtDriverSur,txtOneway,txtFuel,txtPoint;
        LinearLayout spec1Container;
        private View itemView;
        ImageView imgCarResult,imgCarAgencyLogo ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvPassanger = itemView.findViewById(R.id.tvPassanger);
            tvCarModelName = itemView.findViewById(R.id.tvCarModelName);
            tvCarPricing = itemView.findViewById(R.id.tvCarPricing);

            tvBagNo = itemView.findViewById(R.id.tvBagSp);
            txtClass = itemView.findViewById(R.id.txtclass);
            txtSupplierNmae = itemView.findViewById(R.id.txtSupplierName);
            txtDoor = itemView.findViewById(R.id.tvDoor);
            txtDropCity = itemView.findViewById(R.id.dropCity);
            txtAc = itemView.findViewById(R.id.txtac);
            txtTrans = itemView.findViewById(R.id.txttrans);
            txtTerms = itemView.findViewById(R.id.txtTermsCond);
            txtFuel = itemView.findViewById(R.id.txtFuel);
            txtPoint = itemView.findViewById(R.id.txtpoint);
            txtDriverSur = itemView.findViewById(R.id.txtDriverSurCharge);
            txtOneway = itemView.findViewById(R.id.txtOneway);
            spec1Container= itemView.findViewById(R.id.spec1Container);
            imgCarResult=  itemView.findViewById(R.id.imgCarResult);
            imgCarAgencyLogo= itemView.findViewById(R.id.imgCarAgencyLogo);
            this.itemView = itemView ;

        }

        void bindListener(final SearchData carDetail, final OnItemClickListener listener){
            itemView.setOnClickListener(v -> listener.onItemClick(carDetail));
        }

    }

}
