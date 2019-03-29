package com.carshiring.activities.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carshiring.R;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.MapData;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class SearchbyMapActivity extends AppBaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private List<MapData> mapDataList = new ArrayList<>();
    private TinyDB tinyDB;
    private String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchby_map);
        tinyDB = new TinyDB(getApplicationContext());
        currency = tinyDB.getString("from_currency");

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        mapDataList.clear();
        mapDataList = SearchCarFragment.mapDataList;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.search_by_map));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showByList(View view) {
        finish();
    }

    public void openSelectionFilter(View view) {
       /* Intent intent = new Intent(CarsResultListActivity.this, FilterListActivity.class);
        startActivityForResult(intent, 201);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if(mapDataList!=null && mapDataList.size()!=0) {
            for (int i = 0; i < mapDataList.size(); i++) {
                Log.d("TAG", "onMapReady: sdfs" + i);
                if (mapDataList.get(i).getLat() != null || mapDataList.get(i).getLng() != null) {
                    double lat = Double.parseDouble(mapDataList.get(i).getLat());
                    double lang = Double.parseDouble(mapDataList.get(i).getLng());
                    LatLng la = new LatLng(lat, lang);
                    mMap.addMarker(new MarkerOptions().position(la)
                            .title(i + "").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_marker)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(la));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(la, 12.0f));
                }
            }
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public View getInfoContents(Marker marker) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.marker_view,null);
                String s = marker.getTitle();
                TextView txt_price, txt_sname, txt_add;
                ImageView imgLogo;

                txt_add = view.findViewById(R.id.marker_view_supplier_address);
                txt_sname = view.findViewById(R.id.marker_view_supplier_name);
                txt_price = view.findViewById(R.id.marker_view_supplier_price);
                imgLogo = view.findViewById(R.id.marker_view_img);

                if (mapDataList!=null && mapDataList.size()>0){
                    txt_add.setText(mapDataList.get(Integer.parseInt(s)).getSupp_detail());
                    String price =mapDataList.get(Integer.parseInt(s)).getSupplier_price();
                   double markUp = Double.parseDouble(SearchCarFragment.markup);
                    double d = Double.parseDouble(price);
                    double priceNew  = d+(d*markUp)/100;

                    txt_price.setText(currency+" "+ Utility.convertCuurency(priceNew,getApplicationContext()));
                    txt_sname.setText(mapDataList.get(Integer.parseInt(s)).getSupplier_name_ar());
                    if (mapDataList.get(Integer.parseInt(s)).getSupplier_logo()!=null){
                        Glide.with(getApplicationContext())
                                .load(mapDataList.get(Integer.parseInt(s)).getSupplier_logo())
                                .apply(RequestOptions.placeholderOf(R.drawable.placeholder_car).error(R.drawable.placeholder_car))
                                .into(imgLogo);
                    }
                }
                return view;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

               String s= marker.getTitle();
                Intent i = new Intent();
                i.putExtra("myname", mapDataList.get(Integer.parseInt(s)).getSupplier_name_ar());
                setResult(2200, i);
                finish();
            }
        });

    }

}
