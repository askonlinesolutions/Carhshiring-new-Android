package com.carshiring.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;


import com.carshiring.R;
import com.carshiring.utilities.AppBaseActivity;
import com.carshiring.utilities.Utility;
import com.carshiring.adapters.FilterValRecyclerAdapter;
import com.carshiring.models.FilterDefaultMultipleListModel;
import com.carshiring.webservices.RetroFitApis;
import com.carshiring.webservices.RetrofitApiBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Retrofit;

public class SelectFilterActivity extends AppBaseActivity implements View.OnClickListener {

    RadioGroup CategoriesGroup;
    public static String FILTER_RESPONSE = "filter_response";
    public static int FILTER_RESPONSE_CODE = 220;
    private static final String SEPARATOR = ",";
    private FilterValRecyclerAdapter filterValAdapterinsuran,filterValAdapterpack,filterValAdapterSupl,filterValAdapterpackFeature;
    Button reset,applyfilter;
    RecyclerView rec_supplier,recy_package,recy_carfeatures,recy_insurance;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> supplier = new ArrayList<>();
    private ArrayList<String> features = new ArrayList<>();
    private ArrayList<String> packages = new ArrayList<>();
    private ArrayList<String> insurance = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> supplierMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> featuresMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> packageMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> insuranceMultipleListModels = new ArrayList<>();

    private ArrayList<String> SelectedSupplier = new ArrayList<String>();
    private ArrayList<String> SelectedFeatures = new ArrayList<String>();
    private ArrayList<String> SelectedPackages = new ArrayList<String>();
    private ArrayList<String> SelectedInsurances = new ArrayList<String>();
    String sup , feat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_filter);

        CategoriesGroup=findViewById(R.id.categorariesgroup);
        setupcategorygroup();
        rec_supplier=findViewById(R.id.rec_supplier);
        recy_package=findViewById(R.id.recy_package);
        recy_carfeatures=findViewById(R.id.recy_carfeatures);
        if (getIntent().hasExtra("supl")){

        }

        ArrayList<String> getlist= (ArrayList<String>) CarsResultListActivity.supplierList;
        recy_insurance=findViewById(R.id.recy_insurance);
        supplier .addAll(getlist);
        features = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.filter_features)));

        packages = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.filter_package)));
        insurance = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.filter_insurance)));

        FilterDefaultMultipleListModel modelsup,modelfeat,modelpack,modelinsu;

        for (String sup:supplier)
        {
            modelsup = new FilterDefaultMultipleListModel();
            modelsup.setName(sup);
            supplierMultipleListModels.add(modelsup);
        }

        for(String feat:features)
        {
            modelfeat = new FilterDefaultMultipleListModel();
            modelfeat.setName(feat);
            featuresMultipleListModels.add(modelfeat);
        }

        for(String pac:packages)
        {
            modelpack = new FilterDefaultMultipleListModel();
            modelpack.setName(pac);
            packageMultipleListModels.add(modelpack);
        }

        for(String ins:insurance)
        {
            modelinsu = new FilterDefaultMultipleListModel();
            modelinsu.setName(ins);
            insuranceMultipleListModels.add(modelinsu);
        }

        filterValAdapterSupl=new FilterValRecyclerAdapter(this,R.layout.filter_list_val_item_layout,supplierMultipleListModels,SelectedSupplier);
        rec_supplier.setAdapter(filterValAdapterSupl);
        rec_supplier.setLayoutManager(new LinearLayoutManager(this));
        rec_supplier.setHasFixedSize(true);

        filterValAdapterpackFeature=new FilterValRecyclerAdapter(this,R.layout.filter_list_val_item_layout,featuresMultipleListModels,SelectedFeatures);
        recy_carfeatures.setAdapter(filterValAdapterpackFeature);
        recy_carfeatures.setLayoutManager(new LinearLayoutManager(this));
        recy_carfeatures.setHasFixedSize(true);

        filterValAdapterpack=new FilterValRecyclerAdapter(this,R.layout.filter_list_val_item_layout,packageMultipleListModels,SelectedPackages);
        recy_package.setAdapter(filterValAdapterpack);
        recy_package.setLayoutManager(new LinearLayoutManager(this));
        recy_package.setHasFixedSize(true);

        filterValAdapterinsuran=new FilterValRecyclerAdapter(this,R.layout.filter_list_val_item_layout,insuranceMultipleListModels,SelectedInsurances);
        recy_insurance.setAdapter(filterValAdapterinsuran);
        recy_insurance.setLayoutManager(new LinearLayoutManager(this));
        recy_insurance.setHasFixedSize(true);

        filterValAdapterSupl.setonclick(new FilterValRecyclerAdapter.OnClickItem() {
            @Override
            public void itemclick(View v, int i) {
                selectedSupplier(i);
            }
        });
        filterValAdapterpackFeature.setonclick(new FilterValRecyclerAdapter.OnClickItem() {
            @Override
            public void itemclick(View v, int i) {
                selectfeature(i);
            }
        });
        filterValAdapterpack.setonclick(new FilterValRecyclerAdapter.OnClickItem() {
            @Override
            public void itemclick(View v, int i) {
                selectpack(i);
            }
        });
        filterValAdapterinsuran.setonclick(new FilterValRecyclerAdapter.OnClickItem() {
            @Override
            public void itemclick(View v, int i) {
                selectinsurance(i);
            }
        });
        setuptoolbar();

        //Buttons
        reset= findViewById(R.id.reset);
        applyfilter=  findViewById(R.id.apply_filter);

        reset.setOnClickListener(this);
        applyfilter.setOnClickListener(this);

        setSelectedCarTypes();
    }

    private void setupcategorygroup() {
        if(Utility.isNetworkConnected(getApplicationContext()))
        {
            categories.add("1");
            categories.add("33");
            String cat=categories.toString();
            RetroFitApis retroFitApis=RetrofitApiBuilder.getCargHiresapis();

        }
        else
        {

        }
    }

    private void selectinsurance(int i) {
        filterValAdapterinsuran. setitemselected(i);
    }

    private void selectpack(int i) {
        filterValAdapterpack.setitemselected(i);
    }

    private void selectfeature(int i) {
        filterValAdapterpackFeature.setitemselected(i);
    }

    private void selectedSupplier(int i) {
        filterValAdapterSupl.setitemselected(i);
    }

    private void setuptoolbar() {
        actionBar = getSupportActionBar() ;
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    private void setSelectedCarTypes() {
  /*      LinearLayout carTypeContainer =findViewById(R.id.carTypeContainer) ;
        int catTypeCount  =  carTypeContainer.getChildCount() ;
        for (int indexChild = 0; indexChild < catTypeCount; indexChild++) {
            TextView carTypeTV = (TextView) carTypeContainer.getChildAt(indexChild) ;
            if(indexChild==0){
                carTypeTV.setSelected(true);
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.filter));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.reset:
                for(FilterDefaultMultipleListModel model:supplierMultipleListModels)
                {
                    model.setChecked(false);
                }
                for(FilterDefaultMultipleListModel model:packageMultipleListModels)
                {
                    model.setChecked(false);
                }
                for(FilterDefaultMultipleListModel model:insuranceMultipleListModels)
                {
                    model.setChecked(false);
                }
                for(FilterDefaultMultipleListModel model:featuresMultipleListModels)
                {
                    model.setChecked(false);
                }
               /* supplierMultipleListModels.clear();
                packageMultipleListModels.clear();
                insuranceMultipleListModels.clear();
                featuresMultipleListModels.clear();*/

                filterValAdapterpack.notifyDataSetChanged();
                filterValAdapterSupl.notifyDataSetChanged();
                filterValAdapterinsuran.notifyDataSetChanged();
                filterValAdapterpackFeature.notifyDataSetChanged();
                break;
            case R.id.apply_filter:
                if(SelectedFeatures!=null || SelectedSupplier!=null || SelectedPackages!=null || SelectedInsurances!=null)
                {
                    SelectedSupplier.clear();
                    SelectedFeatures.clear();
                    SelectedPackages.clear();
                    SelectedInsurances.clear();
                }
                for(FilterDefaultMultipleListModel model:supplierMultipleListModels)
                {
                    if (model.isChecked())
                    {
                        SelectedSupplier.add(model.getName());
                    }
                }
                for(FilterDefaultMultipleListModel model:featuresMultipleListModels)
                {
                    if (model.isChecked())
                    {
                        SelectedFeatures.add(model.getName());
                    }
                }
                for(FilterDefaultMultipleListModel model:packageMultipleListModels)
                {
                    if (model.isChecked())
                    {
                        SelectedPackages.add(model.getName());
                    }
                }
                for(FilterDefaultMultipleListModel model:insuranceMultipleListModels)
                {
                    if (model.isChecked())
                    {
                        SelectedInsurances.add(model.getName());
                    }
                }
/*
               Utility.message(this,getResources().getString(R.string.selected_packages_are) +
                       SelectedPackages.toString() + "\n" + getResources().getString(R.string.selected_suppliers_are) +
                       SelectedSupplier.toString() + "\n" + getResources().getString(R.string.selected_features_are) +
                       SelectedFeatures.toString() + "\n" + getResources().getString(R.string.selected_insurances_are) +
                       SelectedInsurances.toString());
*/

                FilterDefaultMultipleListModel listModel=new FilterDefaultMultipleListModel();

                if (SelectedSupplier.size()>0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String se : SelectedSupplier) {
                        stringBuilder.append(se);
                        stringBuilder.append(SEPARATOR);
                    }
                    String sel = stringBuilder.toString();
                    sel = sel.substring(0, sel.length() - SEPARATOR.length());
                    listModel.setSupplier(sel);
                }
                else
                {
                    listModel.setSupplier("");
                }

                if (SelectedFeatures.size()>0)
                {
                    StringBuilder stringBuilder2=new StringBuilder();
                    for(String se:SelectedFeatures)
                    {
                        stringBuilder2.append(se);
                        stringBuilder2.append(SEPARATOR);
                    }
                    String sel2=stringBuilder2.toString();
                    sel2=sel2.substring(0,sel2.length()-SEPARATOR.length());
                    listModel.setFeatures(sel2.toString());
                }
                else
                {
                    listModel.setFeatures("");
                }
                if(SelectedPackages.size()>0) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    for (String se : SelectedPackages) {
                        stringBuilder3.append(se);
                        stringBuilder3.append(SEPARATOR);
                    }
                    String sel3 = stringBuilder3.toString();
                    sel3 = sel3.substring(0, sel3.length() - SEPARATOR.length());
                    listModel.setPackages(sel3.toString());
                }
                else
                {
                    listModel.setPackages("");

                }

                if ((SelectedInsurances.size()>0))
                {
                    StringBuilder stringBuilder4=new StringBuilder();
                    for(String se:SelectedInsurances)
                    {
                        stringBuilder4.append(se);
                        stringBuilder4.append(SEPARATOR);
                    }
                    String sel4=stringBuilder4.toString();
                    sel4=sel4.substring(0,sel4.length()-SEPARATOR.length());
                    listModel.setInsurances(sel4.toString());
                }
                else
                {
                    listModel.setInsurances("");
                }
                Intent filrestintent=new Intent();
                filrestintent.putExtra(FILTER_RESPONSE,listModel);
                setResult(FILTER_RESPONSE_CODE,filrestintent);
                finish();
                break;
        }

    }

}
