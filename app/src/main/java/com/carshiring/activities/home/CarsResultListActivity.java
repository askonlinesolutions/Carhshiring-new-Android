package com.carshiring.activities.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.carshiring.R;
import com.carshiring.activities.mainsetup.SignUpActivity;
import com.carshiring.adapters.CarListCategory;
import com.carshiring.adapters.CarResultsListAdapter;
import com.carshiring.fragments.SearchCarFragment;
import com.carshiring.models.FilterDefaultMultipleListModel;
import com.carshiring.models.CatRequest;
import com.carshiring.models.Category;
import com.carshiring.models.PassData;
import com.carshiring.models.SearchData;
import com.carshiring.models.TestData;
import com.carshiring.models.UserDetails;
import com.carshiring.splash.SplashActivity;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.AppGlobal;
import com.carshiring.utilities.CustomLayoutManager;
import com.carshiring.utilities.Utility;
import com.carshiring.webservices.ApiResponse;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mukesh.tinydb.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.carshiring.activities.home.MainActivity.getKeyFromValue;

public class CarsResultListActivity extends AppBaseActivity implements CarResultsListAdapter.OnItemClickListener {
    private Gson gson = new Gson();
    private List<SearchData> listCarResult1 = new ArrayList<>();
    private List<TestData> catBeanList = new ArrayList<>();
    private List<SearchData> listCarResult = new ArrayList<>();
    public static List<String> supplierList = new ArrayList<>();
    public static List<String> featuresList = new ArrayList<>();
    private List<Integer> cateList = new ArrayList<>();
    private TinyDB tinyDB;
    public boolean isClicked;
    private LinearLayout allView;
    public static String id_context, refertype, type, day, time;
    private RecyclerView recycler_search_cars;
    private CatRequest cateRequest = new CatRequest();
    private CarListCategory adapter;
    private ImageView cat_image_all;
    private RecyclerView recyclerView_carlist_category;
    private List<Category.ResponseBean.CatBean> catListdata = new ArrayList<>();
    public static int row_index = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_result_list2);
        InitUI();

        init();
    }

    private void InitUI() {

        TextView tvFromDate = findViewById(R.id.tvFromDT);
        TextView tvPickDate = findViewById(R.id.txtPlaceName);
        TextView tvTodate = findViewById(R.id.tvToDT);
        TextView txtPlaceDrop = findViewById(R.id.txtPlaceName_drop);
        allView = findViewById(R.id.all_view);
        tvFromDate.setText(SearchCarFragment.pick_date + "\n" + SearchCarFragment.pickTime);
        tvPickDate.setText(SearchCarFragment.pickName);
        tvTodate.setText(SearchCarFragment.drop_date + "\n" + SearchCarFragment.dropTime);
        txtPlaceDrop.setText(SearchCarFragment.dropName);
        cat_image_all = findViewById(R.id.car_cat_image_all);
        recycler_search_cars = findViewById(R.id.recycler_search_cars);
        recyclerView_carlist_category = findViewById(R.id.recycler_carlist_by_category);

    }

    private void init() {
        supl = null;
        feat = null;
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        tinyDB = new TinyDB(getApplicationContext());

        if (listCarResult1 != null) {
            listCarResult1.clear();
        }
        if (listCarResult!=null)
            listCarResult.clear();

        assert listCarResult1 != null;
        listCarResult1.addAll(SearchCarFragment.searchData);
        listCarResult.addAll(SearchCarFragment.searchData);

        Log.d(TAG, "init: " + listCarResult.size());
        Glide.with(getApplicationContext())
                .load("https://carshiring.com/site/images/car.png")
                .into(cat_image_all);

//        get supplier
        if (supplierList != null) {
            supplierList.clear();
        }
        for (SearchData searchData : listCarResult) {
            supplierList.add(searchData.getSupplier());
        }
        if (featuresList!=null)
            featuresList.clear();
        for (SearchData searchData : listCarResult) {
            if (searchData.getFeature()!=null ){
                if (searchData.getFeature().getAircondition().equals("true")||searchData
                        .getFeature().getAircondition().equals("1"))
                    featuresList.add("Air Condition");
              /*  else if (searchData.getFeature().getAircondition().equals("false")||searchData
                        .getFeature().getAircondition().equals("0")){
                    featuresList.add("No Air Condition");
                }*/
                if (searchData.getFeature().getFueltype()!= null
                        && !searchData.getFeature().getFueltype().equals("null") )
                featuresList.add(searchData.getFeature().getFueltype());
                if (searchData.getFeature().getTransmission()!=null)
                featuresList.add(searchData.getFeature().getTransmission());
            }
        }

        assert featuresList != null;
        Set<String> stringSet = new HashSet<>(featuresList);
        featuresList.clear();
        featuresList.addAll(stringSet);

        allView.setBackgroundColor(Color.parseColor("#079607"));

        allView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = -1;
                adapter.notifyDataSetChanged();
                cat_All();
            }
        });

        Set<String> hs = new HashSet<>(supplierList);
        supplierList.clear();
        supplierList.addAll(hs);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(CarsResultListActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_carlist_category.setLayoutManager(horizontalLayoutManagaer);
        // Category List
        catBeanList = SearchCarFragment.catBeanList;
        for (TestData testData : catBeanList) {
            cateList.add(Integer.parseInt(testData.getCat_id()));
        }

        getCat(cateList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recycler_search_cars.setLayoutManager(linearLayoutManager);
        Log.d(TAG, "init: "+listCarResult.size());
        listdispaly(listCarResult);

    }

    private JSONObject obj;

    private void getCat(List<Integer> list) {
        if (catListdata != null) {
            catListdata.clear();
        }
        cateRequest.setCode(list);
        Utility.showloadingPopup(CarsResultListActivity.this);
        String cat = gson.toJson(cateRequest);
        Log.d(TAG, "getCat: " + cat);
        String url = RetrofitApiBuilder.CarHires_BASE_URL + "category_list_byid";
        try {
            obj = new JSONObject(cat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                url, obj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utility.hidepopup();
                Category category = new Category();
                category = gson.fromJson(response.toString(), Category.class);
                if (category != null && category.getResponse() != null) {
                    if (category.getResponse().getCat() != null) {
                        catListdata.addAll(category.getResponse().getCat());

                        adapter = new CarListCategory(CarsResultListActivity.this, listCarResult, catListdata, catBeanList,
                                new CarListCategory.OnItemClickListenerCategory() {
                                    @Override
                                    public void onItemClickCategory(int position) {
                                        allView.setBackgroundResource(R.drawable.buttoncurve_caategory);
                                        isClicked = true;
                                        catgory_clicked(position);
                                    }
                                });
                        recyclerView_carlist_category.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hidepopup();

                Log.d(TAG, "onErrorResponse: " + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void catgory_clicked(int position) {
        List<SearchData> listCarResultCatFilter = new ArrayList<>();
        listCarResultCatFilter.clear();
        for (int i = 0; i < listCarResult.size(); i++) {
            ArrayList<String> integers = new ArrayList<>(catBeanList.get(position).getCat_code());
            for (int j = 0; j < integers.size(); j++) {
                String k = integers.get(j) + "";
                if (String.valueOf(k).equals(listCarResult.get(i).getCategory())) {
                    listCarResultCatFilter.add(listCarResult.get(i));
                }
            }
        }

        if (supl != null && feat != null) {
            if (supl.equals("NoSuppliers") && feat.equals("NoFeatures")) {
                listdispaly(listCarResult);
                Toast.makeText(getApplicationContext(), "No Filter Applied", Toast.LENGTH_SHORT).show();
            } else {
                filter(supl, feat, listCarResultCatFilter);
            }
        } else {
            listdispaly(listCarResultCatFilter);
        }
    }

    public void cat_All() {
        allView.setBackgroundColor(Color.parseColor("#079607"));
        if (supl != null && feat != null) {
            if (supl.equals("NoSuppliers") && feat.equals("NoFeatures")) {
                listdispaly(listCarResult1);
                Toast.makeText(getApplicationContext(), "No Filter Applied", Toast.LENGTH_SHORT).show();
            } else {
                filter(supl, feat, listCarResult1);
            }
        } else {
            listdispaly(listCarResult1);
        }

    }

    private String oneway, driverSur;
    private CarResultsListAdapter listAdapter;
    private void listdispaly(List<SearchData> listCarResult) {
        listAdapter = new CarResultsListAdapter(this, listCarResult, catBeanList, this);
        recycler_search_cars.setAdapter(listAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.car_results));

        if (isApplyFiltered) {
            if (filteredtList.size() > 0)
                listdispaly(filteredtList);
            else
                listdispaly(filteredtList);
        }
        isApplyFiltered = false;
    }

    public void openSelectionSortedBy(View view) {
        Intent intent = new Intent(CarsResultListActivity.this, SearchbyMapActivity.class);
        startActivityForResult(intent, 2200);
    }

    private static String supl, feat;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Filter
        if (requestCode == FILTER_REQUEST) {
            if (resultCode == SelectFilterActivity.FILTER_RESPONSE_CODE) {
                Log.d("VKK", "Total no of car list = " + listCarResult.size());
                FilterDefaultMultipleListModel multipleListModel = (FilterDefaultMultipleListModel) data
                        .getSerializableExtra(SelectFilterActivity.FILTER_RESPONSE);
                Log.d("VKK", "Total Filtered List = " + gson.toJson(multipleListModel));
                supl = multipleListModel.getSupplier();
                feat = multipleListModel.getFeatures();
                Log.d("VKK", "Total Suppliers Filtered = " + supl);
                Log.d("VKK", "Total Features Filtered = " + feat);
                if (supl.equals("NoSuppliers") && feat.equals("NoFeatures")) {
                    listdispaly(listCarResult1);
                    Toast.makeText(getApplicationContext(), "No Filter Applied", Toast.LENGTH_SHORT).show();
                    supl = null;
                    feat = null;
                } else {
                    filter(supl, feat, listCarResult);
                }
            }
        } else if (requestCode == 2200) {
            if (data != null) {
                String str = data.getExtras().getString("myname");
                supl = str;
                feat = "NoFeatures";
                filter(str, feat, listCarResult);
            }
        } else {

        }
    }

    private boolean isApplyFiltered = false, isFeatureFound = false, isSupplierFound = false;
    private ArrayList<SearchData> filteredtList, filteredtList1, filteredtList2;

    private void filter(String supl, String feat, List<SearchData> searchDataList) {

        String[] suplier = supl.split(",");
        String[] features = feat.split(",");

        filteredtList = new ArrayList<>();
        filteredtList1 = new ArrayList<>();
        filteredtList2 = new ArrayList<>();

        outerloop:

       /* if (isClicked){
            searchDataList = listCarResultCatFilter;
        } else {
            searchDataList = listCarResult;
        }*/
        for (int i = 0; i < searchDataList.size(); i++) {

            isFeatureFound = false;
            isSupplierFound = false;

            SearchData data = searchDataList.get(i);

            for (int z = 0; z < suplier.length; z++) {
                String str = suplier[z];
                if (str.equals(data.getSupplier())) {
                    filteredtList1.add(data);
                    isSupplierFound = true;
                } else {
                    isSupplierFound = false;
                }
            }

            if (!feat.equals("NoFeatures")){
                for (int z = 0; z < features.length; z++) {
                    String str = features[z];
                    SearchData.FeatureBean data1;

                    data1 = data.getFeature();
                    String f = gson.toJson(data1);

                    try {
                        JSONObject job = new JSONObject(f);

                        String aircondition = job.getString("aircondition");
                        String bag = job.getString("bag");
                        String door = job.getString("door");
                        String fueltype = job.getString("fueltype");
                        String transmission = job.getString("transmission");

                        if (str.equals("Air Condition")) {
                            if (aircondition.equals("true")) {
                                isFeatureFound = true;
                            } else {
                                isFeatureFound = false;
                                break;
                            }
                        } else if (str.equals("Automatic")) {
                            if (transmission.equals("Automatic") ) {
                                isFeatureFound = true;
//                        filteredtList.add(data);
                            } else {
                                isFeatureFound = false;
                                break;
                            }
                        } else if (str.equals("Manual")){
                            if (transmission.equals("Manual") ) {
                                isFeatureFound = true;
//                        filteredtList.add(data);
                            } else {
                                isFeatureFound = false;
                                break;
                            }
                        }
                        else if (str.equals("Petrol")) {
                            if (fueltype.equals("Petrol")) {
                                isFeatureFound = true;
//                        filteredtList.add(data);
                            } else {
                                isFeatureFound = false;
                                break;
                            }
                        } else if(fueltype.equals("Diesel")){
                            if (fueltype.equals("Diesel")) {
                                isFeatureFound = true;
//                        filteredtList.add(data);
                            } else {
                                isFeatureFound = false;
                                break;
                            }
                        }
                        else if (str.equals("4+ Doors")) {
                            if (door!=null && Integer.parseInt(door)>=4) {
                                isFeatureFound = true;
//                        filteredtList.add(data);
                            } else {
                                isFeatureFound = false;
                                break;
                            }
                        } else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            if (isFeatureFound) {
                filteredtList2.add(data);
            }
        }

        if (filteredtList1.size() != 0) {
            for (int f1 = 0; f1 < filteredtList1.size(); f1++) {
                SearchData data1;
                data1 = filteredtList1.get(f1);
                Log.d(TAG, "RK filter: "+filteredtList1.size());

                if (filteredtList2.size() != 0) {
                    for (int f2 = 0; f2 < filteredtList2.size(); f2++) {
                        SearchData data2 = new SearchData();
                        data2 = filteredtList2.get(f2);
                        if (data1.getId_context().equals(data2.getId_context())) {
                            filteredtList.add(data1);
                        } else {
                        }
                    }
                } else {
                    if (features[0].equals("NoFeatures")) {
                        // No Features selected
                        filteredtList.addAll(filteredtList1);
                    } else {
                        // Features Selected but FilterLis2 Size is 0
                        filteredtList.clear();
                    }
                    break;
                }
            }
        } else {
            filteredtList.addAll(filteredtList2);
        }
        if ((filteredtList != null && filteredtList1.size() == 0) && (filteredtList2 != null && filteredtList2.size() == 0) &&
                (filteredtList != null && filteredtList.size() == 0)) {
            Toast.makeText(CarsResultListActivity.this, "No Record found", Toast.LENGTH_SHORT).show();
        }

        Log.d("VKK", filteredtList1.size() + " ");
        Log.d("VKK", filteredtList2.size() + " ");
        Log.d("VKK", filteredtList.size() + " ");

        isApplyFiltered = true;

        if (isClicked) {
            listdispaly(filteredtList);
        }

    }

    public static int FILTER_REQUEST = 201;

    public void openSelectionFilter(View view) {
        Intent intent = new Intent(CarsResultListActivity.this, FilterListActivity.class);
        startActivityForResult(intent, FILTER_REQUEST);
    }

    @Override
    protected void onStop() {
        super.onStop();

        tinyDB.remove("listSup");
        tinyDB.remove("listFet");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        tinyDB.remove("listSup");
//        tinyDB.remove("listFet");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        row_index = -1;
    }

    public static String driverSurCharge, oneWayFee;

    @Override
    public void onItemClick(SearchData carDetail) {
        id_context = carDetail.getId_context();
        type = carDetail.getType();
        refertype = carDetail.getRefer_type();
        final String curren = tinyDB.getString("from_currency");
        for (SearchData.CoveragesBean bean : carDetail.getCoverages()) {
            if (bean.getCode().equalsIgnoreCase("412")) {
                oneway = bean.getName() + " : " + curren
                        + Utility.convertCuurency(Double.parseDouble(bean.getAmount2()), getApplicationContext());
                oneWayFee = bean.getAmount2();
            } else if (bean.getCode().equalsIgnoreCase("410")) {
                driverSurCharge = bean.getAmount2();
                driverSur = bean.getName() + " : " + curren
                        + Utility.convertCuurency(Double.parseDouble(bean.getAmount2()), getApplicationContext());
            }
        }
        Intent intent = new Intent(CarsResultListActivity.this, CarDetailActivity.class);
        intent.putExtra("id_context", id_context);
        intent.putExtra("type", type);
        intent.putExtra("car_type", carDetail.getCar_type());
        double pricea = Double.parseDouble(carDetail.getPrice());
        double pointpercent = Double.parseDouble(SearchCarFragment.pointper);
        double markUp = Double.parseDouble(SearchCarFragment.markup);
        double d = pricea;
        double priceNew = d + (d * markUp) / 100;
        double calPrice = (priceNew * pointpercent) / 100;
        double calPoint = (int) (calPrice / 0.02);
        day = carDetail.getTime();
        time = carDetail.getTime_unit();
        intent.putExtra("day", carDetail.getTime());
        intent.putExtra("refer_type", refertype);
        intent.putExtra("point_earn", String.valueOf(calPoint));
        intent.putExtra("one_way_fee", oneway);
        intent.putExtra("driverSur", driverSur);
        startActivity(intent);
    }
}