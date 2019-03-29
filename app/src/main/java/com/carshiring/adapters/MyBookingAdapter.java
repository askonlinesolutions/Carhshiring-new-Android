package com.carshiring.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carshiring.R;
import com.carshiring.activities.home.TermsActivity;
import com.carshiring.models.BookingHistory;
import com.carshiring.models.UserDetails;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carshiring.splash.SplashActivity.TAG;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyViewHolder> {

    private Dialog dialog;
    private String type,bookingid;
    private MyBookingIF myBookingIF;

    public MyBookingAdapter(Activity context, List<BookingHistory> bookinglist, String type, MyBookingIF myBookingIF) {
        this.context = context;
        this.bookinglist = bookinglist;
        this.type = type;
        tinyDB = new TinyDB(context);
        this.myBookingIF = myBookingIF;
    }

    public interface MyBookingIF{
        void viewExtra(int pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView refnumb, date_time, rate, txtjourneyToDate, txtStatus, txtPickUp, txtDropUp,
                txtCancel, txtCarName, txtPricedetails, txtFullProt, txtPaymentBy, txtCreditValue,
                txtWallet, txtPoint, txtCoupon;
        ImageView imgLogo, car_image;
        LinearLayout  bookingLayout;
        Button btnBookingCharge, btnCancelationCharge, btnExtraView, btnViewVoucher;

        MyViewHolder(View view) {
            super(view);
            btnExtraView = itemView.findViewById(R.id.bookingExtraDetails);
            btnViewVoucher = itemView.findViewById(R.id.bookingVoucher);
            bookingLayout = itemView.findViewById(R.id.bookingDetailLayout);
            refnumb =  itemView.findViewById(R.id.txt_bookingInvoicenumber);
            date_time = itemView.findViewById(R.id.txt_date_time);
            rate = itemView.findViewById(R.id.txt_rate);
            txtStatus = itemView.findViewById(R.id.previous_booking_txtStatus);
            txtPickUp = itemView.findViewById(R.id.previous_booking_pickUp);
            txtDropUp = itemView.findViewById(R.id.previous_booking_dropUp);
            txtPaymentBy = itemView.findViewById(R.id.previous_booking_txtCarName);
            txtFullProt = itemView.findViewById(R.id.txtFullProtection);
            txtCarName = itemView.findViewById(R.id.previous_booking_txtPaymentBy);
            txtPricedetails = itemView.findViewById(R.id.previous_booking_txtPricedetails);
            imgLogo = itemView.findViewById(R.id.previous_booking_imgCarLogo);
            car_image = itemView.findViewById(R.id.my_booking_image);
            txtCancel = itemView.findViewById(R.id.previous_booking_txtcancel);
            txtjourneyToDate = itemView.findViewById(R.id.txt_date_totime);
            txtCoupon = itemView.findViewById(R.id.txtCuoponValue);
            txtCreditValue = itemView.findViewById(R.id.txtCreditValueAmt);
            txtWallet = itemView.findViewById(R.id.txtWalletValueAmt);
            txtPoint = itemView.findViewById(R.id.txtPointValueAmt);
            btnBookingCharge = itemView.findViewById(R.id.btnBookingCharge);
            btnCancelationCharge = itemView.findViewById(R.id.btn_cancelCharge);
            txtPricedetails.setOnClickListener(view1 -> setBookingPop(getAdapterPosition()));


            from_currency = tinyDB.getString("from_currency");
            btnCancelationCharge.setOnClickListener(view12 -> setCancelationPop(getAdapterPosition()));

            if (type.equals("p")) {
                txtCancel.setVisibility(View.GONE);
            }

            btnExtraView.setOnClickListener(view13 -> myBookingIF.viewExtra(getAdapterPosition()));

            txtCancel.setOnClickListener(view14 -> setCancelation(getAdapterPosition(),txtCancel));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private TinyDB tinyDB;

    private String accountType="1",language, user_id;// 1=wallet, 2 account
    @SuppressLint("SetTextI18n")
    private void setCancelation(int position, final TextView txtCancel1){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.account_popup_view);
        language = tinyDB.getString("language_code");

        bookingid = bookinglist.get(position).getBooking_id();
        TextView txtSelect = dialog.findViewById(R.id.tctSelect);
        TextView txtCancelCharge = dialog.findViewById(R.id.txtCancelChagre);
        if (bookinglist.get(position)
                .getBooking_canceldetail().getBooking_cancel_cancel_charge()!=null && bookinglist.get(position)
                .getBooking_canceldetail().getBooking_cancel_cancel_charge().length()>0)
        txtCancelCharge.setText(context.getResources().getString(R.string.canCharge)+": "+from_currency
                + Utility.convertCuurency(Double
                .parseDouble(bookinglist.get(position)
                        .getBooking_canceldetail().getBooking_cancel_cancel_charge()),context));

        else txtCancelCharge.setText( context.getResources().getString(R.string.canCharge)+" : "+from_currency +"0.00");
        String payfortValue;
        if (bookinglist.get(position).getBooking_payfort_value()!=null&&
                bookinglist.get(position).getBooking_payfort_value().equals("0.00")) {
            payfortValue = bookinglist.get(position).getBooking_payfort_value();
        } else {
            payfortValue = bookinglist.get(position).getBooking_payfort_value();
        }
        String walltevalue;
        if (bookinglist.get(position).getBooking_wallet_value()!=null &&
                bookinglist.get(position).getBooking_wallet_value().equals("0.00")) {
            walltevalue = bookinglist.get(position).getBooking_wallet_value();
        } else {
            walltevalue = bookinglist.get(position).getBooking_wallet_value();
        }
        String pointvalue;
        if (bookinglist.get(position).getBooking_pont_value()!=null&&
                bookinglist.get(position).getBooking_pont_value().equals("0.00")) {
            pointvalue = bookinglist.get(position).getBooking_pont_value();
        } else {
            pointvalue = bookinglist.get(position).getBooking_pont_value();
        }
        final RadioButton walletRadioButton= dialog.findViewById(R.id.radio_wallet);
        final RadioButton accountRadioButton= dialog.findViewById(R.id.radio_account);
        final TextView txtSubmit= dialog.findViewById(R.id.account_btnsubit);
        TextView txtCancel = dialog.findViewById(R.id.account_btnCancel);
        dialog.show();
        if (payfortValue !=null && walltevalue !=null && pointvalue !=null){
            if (payfortValue.equals("0.00") && walltevalue.equals("0.00") && pointvalue.equals("0.00")) {
                accountRadioButton.setVisibility(View.GONE);
                walletRadioButton.setVisibility(View.GONE);
                txtSelect.setText(context.getResources().getString(R.string.txtCancelBooking));
            } else {
                if (payfortValue.equals("0.00")){
                    accountRadioButton.setVisibility(View.GONE);
                }
            }
        }

        txtSubmit.setOnClickListener(view -> {
            if (accountType!=null&&accountType.length()>0){
                String login = tinyDB.getString("login_data");
                UserDetails userDetails = new Gson().fromJson(login, UserDetails.class);
                user_id = userDetails.getUser_id();
                cancelBooking(accountType, bookingid,txtCancel1);
                dialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(view -> dialog.dismiss());

        accountRadioButton.setOnClickListener(view -> {
            boolean checked = ((RadioButton) view).isChecked();
            if (checked){
                accountType = "2";
            }
        });

        walletRadioButton.setOnClickListener(view -> {
            boolean checked = ((RadioButton) view).isChecked();
            if (checked){
                accountType="1";
            }
        });
    }

    private Gson gson = new Gson();

    private void cancelBooking(String type, String bookingid, final TextView txtCancel1){
        Utility.showloadingPopup(context);
        RetroFitApis fitApis= RetrofitApiBuilder.getCargHiresapis();
        String time_zone = TimeZone.getDefault().getID()+"";
        final Call<ApiResponse> bookingCancel = fitApis.cancelBooking(language,type,bookingid,time_zone,user_id);

        bookingCancel.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                Utility.hidepopup();
                Log.d(TAG, "onResponse: cancel"+gson.toJson(response.body()));
                if (response!=null){
                    if (response.body().status){
                        txtCancel1.setVisibility(View.GONE);
                       // notifyDataSetChanged();
                        Toast.makeText(context, ""+response.body().msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, ""+context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.hidepopup();
                dialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(context, ""+context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TextView txtCoupon, txtCreditValue, txtWallet, txtPriceTitle, txtPoint, txtBookingDate, rate, txtFullProt;
    private Button btnCancel;
    private String from_currency;

    @SuppressLint("SetTextI18n")
    private void setBookingPop(int position) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.price_details_view);
        txtCoupon = dialog.findViewById(R.id.txtCuoponValue);
        txtCreditValue = dialog.findViewById(R.id.txtCreditValueAmt);
        txtWallet = dialog.findViewById(R.id.txtWalletValueAmt);
        txtPoint = dialog.findViewById(R.id.txtPointValueAmt);
        txtFullProt = dialog.findViewById(R.id.txtFullProtection);
        btnCancel = dialog.findViewById(R.id.txtCancel);
        txtBookingDate = dialog.findViewById(R.id.txt_bookingdate);
        txtPriceTitle = dialog.findViewById(R.id.price_detail_title);
        txtPriceTitle.setText(context.getResources().getString(R.string.txtBookingCharge));
        rate = dialog.findViewById(R.id.txt_rate);
        if (bookinglist.get(position).getBooking_payfort_value() != null &&
                !bookinglist.get(position).getBooking_payfort_value().equals("0.00")) {
            txtCreditValue.setVisibility(View.VISIBLE);
            txtCreditValue.setText(context.getResources().getString(R.string.txtPaid)+" : "+from_currency
                    + Utility.convertCuurency(Double.parseDouble(bookinglist.get(position).getBooking_payfort_value()), context));
        }
        txtBookingDate.setText(context.getResources().getString(R.string.txtTransactionDate) + " : "
                + /*Utility.convertSimpleDate(bookinglist.get(position).getBokking_date())*/
                bookinglist.get(position).getBokking_date());
        rate.setText(context.getResources().getString(R.string.txtCarPrice) + " : "
                + from_currency
               + Utility.convertCuurency(Double.parseDouble(bookinglist.get(position).getBooking_actual_price()),context));

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        if (bookinglist.get(position).getBooking_coupon_value() != null && !bookinglist.get(position).getBooking_coupon_value().equals("0.00")) {
            txtCoupon.setVisibility(View.VISIBLE);
            txtCoupon.setText(context.getResources().getString(R.string.txtDiscount) + " : "+from_currency + Utility.convertCuurency(Double
                    .parseDouble(bookinglist.get(position).getBooking_coupon_value()),context));
        }
        if (bookinglist.get(position).getBooking_wallet_value() != null && !bookinglist.get(position).getBooking_wallet_value().equals("0.00")) {
            txtWallet.setVisibility(View.VISIBLE);
            txtWallet.setText(context.getResources().getString(R.string.txtWal) + " : " +from_currency+ Utility
                    .convertCuurency(Double.parseDouble(bookinglist.get(position).getBooking_wallet_value()), context));
        }
        if (bookinglist.get(position).getBooking_pont_value() != null && !bookinglist.get(position).getBooking_pont_value().equals("0.00")) {
            txtPoint.setVisibility(View.VISIBLE);
            txtPoint.setText(context.getResources().getString(R.string.points) + " : "+from_currency + Utility.convertCuurency(Double
                    .parseDouble(bookinglist.get(position).getBooking_pont_value()),context));
        }
        if (bookinglist.get(position).getBooking_fullprotection_value() != null
                && !bookinglist.get(position).getBooking_fullprotection_value().equalsIgnoreCase("0.00")) {
            txtFullProt.setVisibility(View.VISIBLE);
            txtFullProt.setText(context.getResources().getString(R.string.txtFullPro) + " : "+from_currency +
                    Utility.convertCuurency(Double.parseDouble(bookinglist.get(position).getBooking_fullprotection_value()),context));
        }
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void setCancelationPop(int position) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.price_details_view);
        txtCoupon = dialog.findViewById(R.id.txtCuoponValue);
        txtCreditValue = dialog.findViewById(R.id.txtCreditValueAmt);
        txtWallet = dialog.findViewById(R.id.txtWalletValueAmt);
        txtPoint = dialog.findViewById(R.id.txtPointValueAmt);
        txtFullProt = dialog.findViewById(R.id.txtFullProtection);
        btnCancel = dialog.findViewById(R.id.txtCancel);
        txtBookingDate = dialog.findViewById(R.id.txt_bookingdate);
        rate = dialog.findViewById(R.id.txt_rate);
        txtPriceTitle = dialog.findViewById(R.id.price_detail_title);
        txtPriceTitle.setText(context.getResources().getString(R.string.txtBookingCancel));
        txtBookingDate.setVisibility(View.GONE);
        if (bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_booking_id()
                .equalsIgnoreCase(bookinglist.get(position).getBooking_id())) {
            if (bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_booking_amount() != null &&
                    !bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_booking_amount().equals("0.00")) {
                rate.setVisibility(View.VISIBLE);
                rate.setText(context.getResources().getString(R.string.txtBookingAmt)+" : " +from_currency+
                        Utility.convertCuurency(Double
                                .parseDouble(bookinglist.get(position).getBooking_canceldetail()
                                        .getBooking_cancel_booking_amount()),context));
            }

            txtFullProt.setText(context.getResources().getString(R.string.txtCancelCharge)+" : "+from_currency + Utility
                    .convertCuurency(Double.parseDouble(bookinglist.get(position)
                    .getBooking_canceldetail().getBooking_cancel_cancel_charge()),context));
            txtCoupon.setVisibility(View.VISIBLE);
            txtCoupon.setText(context.getResources().getString(R.string.txtRefun)+" : "+from_currency + Utility.convertCuurency(Double
                    .parseDouble(bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_refundable_amount()),context));
            txtFullProt.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(view -> dialog.dismiss());

            if (bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_wallet_amount()
                    != null && !bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_wallet_amount().equalsIgnoreCase("0.00")) {
                txtWallet.setVisibility(View.VISIBLE);
                txtWallet.setText(context.getResources().getString(R.string.txtWal)+" : "+from_currency + Utility.convertCuurency(Double.parseDouble(bookinglist.get(position)
                        .getBooking_canceldetail().getBooking_cancel_wallet_amount()),context));
            } else {
                txtWallet.setVisibility(View.GONE);
            }
            if (bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_credit_amount() != null &&
                    !bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_credit_amount().equalsIgnoreCase("0.00")) {
                txtCreditValue.setVisibility(View.VISIBLE);
                txtCreditValue.setText(context.getResources().getString(R.string.txtRefundBank)
                        +" : "+from_currency + Utility.convertCuurency(Double.parseDouble(bookinglist
                        .get(position).getBooking_canceldetail().getBooking_cancel_credit_amount()),context));
            }
            if (bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_point_amount() != null
                    && !bookinglist.get(position).getBooking_canceldetail().getBooking_cancel_point_amount().equalsIgnoreCase("0.00")) {
                txtPoint.setVisibility(View.VISIBLE);
                txtPoint.setText(context.getResources().getString(R.string.txtRefundPoint)+" : "+from_currency + Utility.convertCuurency(Double.parseDouble(String.valueOf(bookinglist.get(position)
                        .getBooking_canceldetail().getBooking_cancel_point_amount())),context));
            } else {
                txtPoint.setVisibility(View.GONE);
            }
        }
        dialog.show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previous_booking_row, parent, false);

        return new MyViewHolder(itemView);
    }
  Activity context;
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.refnumb.setText(bookinglist.get(position).getBooking_id());
        holder.date_time.setText(Utility.convertdate(bookinglist.get(position).getBooking_from_date()));
        holder.txtjourneyToDate.setText(Utility.convertdate(bookinglist.get(position).getBooking_to_date()));

        holder.btnViewVoucher.setOnClickListener(view -> {
            if (bookinglist.get(position).getBokking_invoice()!=null && bookinglist.get(position)
                    .getBokking_invoice().length()>0){
                String url = RetrofitApiBuilder.INVICE_BASE_URL + bookinglist.get(position).getBokking_invoice();
                Log.d(TAG, "onClick: "+url);
                Intent intent = new Intent(context, TermsActivity.class);
                intent.putExtra("voucher", "View Voucher");
                intent.putExtra("data", url);
                context.startActivity(intent);
            } else Toast.makeText(context, "Voucher not available", Toast.LENGTH_SHORT).show();
        });


        if (bookinglist.get(position).getBooking_status().equals("0")) {
            holder.txtCancel.setVisibility(View.GONE);
            holder.txtStatus.setText(context.getResources().getString(R.string.failed));
        } else if (bookinglist.get(position).getBooking_status().equals(/*"1"*/"3")) {
           /* if (type.equals("c")){
                holder.bookingLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.txtStatus.setText("Success");
            } else{
                holder.bookingLayout.setBackgroundColor(context.getResources().getColor(R.color.redStrong));
                holder.txtStatus.setText(context.getResources().getString(R.string.completed));
            }*/
            holder.txtCancel.setVisibility(View.GONE);
            holder.txtStatus.setText(context.getResources().getString(R.string.failed)); 
        } else if (bookinglist.get(position).getBooking_status().equals("2")) {
            holder.txtCancel.setVisibility(View.GONE);
            holder.txtStatus.setText(context.getResources().getString(R.string.canceled));
        } else {
           /* holder.txtCancel.setVisibility(View.GONE);
            holder.txtStatus.setText(context.getResources().getString(R.string.failed));*/

            if (type.equals("c")){
                holder.bookingLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.txtStatus.setText(context.getResources().getString(R.string.Success));
            } else{
                holder.bookingLayout.setBackgroundColor(context.getResources().getColor(R.color.redStrong));
                holder.txtStatus.setText(context.getResources().getString(R.string.completed));
            }
        }
        holder.txtDropUp.setText(bookinglist.get(position).getBooking_to_location());
        holder.txtPickUp.setText(bookinglist.get(position).getBooking_from_location());
        holder.txtCarName.setText(bookinglist.get(position).getBooking_car_model());
        holder.txtPaymentBy.setText(bookinglist.get(position).getBooking_company_name());
        if (bookinglist.get(position).getBooking_extra()!=null && bookinglist.get(position).getBooking_extra().size()>0){
            holder.btnExtraView.setVisibility(View.VISIBLE);
        } else holder.btnExtraView.setVisibility(View.GONE);
        Glide.with(context).load(bookinglist.get(position).getBooking_supllier_log())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                .into(holder.imgLogo);
        Glide.with(context).load(bookinglist.get(position).getBooking_car_image())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                .into(holder.car_image);

        holder.btnBookingCharge.setText(context.getResources().getString(R.string.txtBookingCharge)+
                ": "+from_currency + Utility.convertCuurency(Double.parseDouble(bookinglist.get(position).getBooking_total_price()),
                context));
        if (bookinglist.get(position).getBooking_status().equals("2")) {
            holder.btnCancelationCharge.setVisibility(View.VISIBLE);
            holder.btnCancelationCharge.setText(context.getResources().getString(R.string.txtCancelCharge)+" : "+from_currency
                    + Utility.convertCuurency(Double.parseDouble(bookinglist.get(position)
                    .getBooking_canceldetail().getBooking_cancel_cancel_charge()),context));
        } else {
            holder.btnCancelationCharge.setVisibility(View.GONE);
        }
        holder.btnBookingCharge.setText(context.getResources().getString(R.string.txtBookingCharge)+
                " : "+from_currency + Utility.convertCuurency(Double.parseDouble(bookinglist.get(position)
                .getBooking_total_price()),context));
        if (bookinglist.get(position).getBooking_status().equals("2")) {
            holder.btnCancelationCharge.setVisibility(View.VISIBLE);
            holder.btnCancelationCharge.setText( context.getResources().getString(R.string.txtCancelCharge)+" : "+from_currency
                    + Utility.convertCuurency(Double.parseDouble(bookinglist.get(position)
                    .getBooking_canceldetail().getBooking_cancel_cancel_charge()),context));
        } else {
            holder.btnCancelationCharge.setVisibility(View.GONE);
        }
    }

    List<BookingHistory>  bookinglist;
    @Override
    public int getItemCount() {
        return bookinglist.size();
    }

}