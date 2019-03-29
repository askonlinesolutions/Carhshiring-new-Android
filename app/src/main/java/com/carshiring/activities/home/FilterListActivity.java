package com.carshiring.activities.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.carshiring.R;
import com.carshiring.adapters.FilterValRecyclerAdapter;
import com.carshiring.models.FilterDefaultMultipleListModel;
import com.carshiring.utilities.AppBaseActivity;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;

public class FilterListActivity extends AppBaseActivity implements View.OnClickListener {
    public static String FILTER_RESPONSE = "filter_response";
    public static int FILTER_RESPONSE_CODE = 220;
    private static final String SEPARATOR = ",";
    private FilterValRecyclerAdapter filterValAdapterSupl,filterValAdapterpackFeature;
    Button reset,applyfilter;
    RecyclerView rec_supplier,recy_carfeatures;
    private ArrayList<String> supplier = new ArrayList<>();
    private ArrayList<String> features = new ArrayList<>();
    TinyDB tinyDB ;
    public static ArrayList<FilterDefaultMultipleListModel> supplierMultipleListModelsSelected = new ArrayList<>();
    public static ArrayList<FilterDefaultMultipleListModel> featuresMultipleListModelsSelected  = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> supplierMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> featuresMultipleListModels = new ArrayList<>();
    private ArrayList<String> SelectedSupplier = new ArrayList<String>();
    private ArrayList<String> SelectedFeatures = new ArrayList<String>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);

//        find id

        rec_supplier=findViewById(R.id.rec_supplier);
        recy_carfeatures=findViewById(R.id.recy_carfeatures);

        ArrayList<String> getlist= (ArrayList<String>) CarsResultListActivity.supplierList;
        supplier .addAll(getlist);

        features = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.filter_features)));
        features.addAll(CarsResultListActivity.featuresList);
        tinyDB = new TinyDB(getApplicationContext());
        FilterDefaultMultipleListModel modelsup,modelfeat;

        if (supplierMultipleListModelsSelected!=null){
            for(FilterDefaultMultipleListModel model:supplierMultipleListModelsSelected)
                model.setChecked(true);
        }
        if (featuresMultipleListModelsSelected!=null){
            for(FilterDefaultMultipleListModel model:featuresMultipleListModelsSelected)
                model.setChecked(true);
        }

        for (String sup:supplier)
        {
            modelsup = new FilterDefaultMultipleListModel();
            modelsup.setName(sup);
            supplierMultipleListModels.add(modelsup);
            supplierMultipleListModelsSelected.addAll(supplierMultipleListModels);
        }

        for(String feat:features)
        {
            modelfeat = new FilterDefaultMultipleListModel();
            modelfeat.setName(feat);
            featuresMultipleListModels.add(modelfeat);
            featuresMultipleListModelsSelected.addAll(featuresMultipleListModels);
        }

        filterValAdapterSupl=new FilterValRecyclerAdapter(this, R.layout.filter_list_val_item_layout,
                supplierMultipleListModels, SelectedSupplier);

        //suppliers
        rec_supplier.setAdapter(filterValAdapterSupl);
        rec_supplier.setLayoutManager(new LinearLayoutManager(this));
        rec_supplier.setHasFixedSize(true);

        filterValAdapterpackFeature=new FilterValRecyclerAdapter(this, R.layout.filter_list_val_item_layout,
                featuresMultipleListModels,SelectedFeatures);

        // features
        recy_carfeatures.setAdapter(filterValAdapterpackFeature);
        recy_carfeatures.setLayoutManager(new LinearLayoutManager(this));
        recy_carfeatures.setHasFixedSize(true);

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

        if (tinyDB.contains("listFet")){
            ArrayList<String> fet = new ArrayList<>();
            fet.addAll(tinyDB.getListString("listFet"));
            Log.d("TAG", "onCreate:fet " + fet);

            for (int i = 0; i < fet.size(); i++) {

                for (int j = 0; j < features.size(); j++) {

                    if (fet.get(i).equals(features.get(j))) {
                        selectfeature(j);
                    }
                }
            }
        } if (tinyDB.contains("listSup")) {
            ArrayList<String> supp = new ArrayList<>();
            supp.addAll(tinyDB.getListString("listSup"));
            Log.d("TAG", "onCreate:sdd " + supp);
            for (int i = 0; i < supp.size(); i++) {

                for (int j = 0; j < supplier.size(); j++) {

                    if (supp.get(i).equals(supplier.get(j))) {
                        selectedSupplier(j);
                    }
                }
            }
        }

        tinyDB.remove("listSup");
        tinyDB.remove("listFet");

        setuptoolbar();

        //Buttons
        reset= findViewById(R.id.reset);
        applyfilter=  findViewById(R.id.apply_filter);

        reset.setOnClickListener(this);
        applyfilter.setOnClickListener(this);


     //   createMyDialog();

    }

    private void selectfeature(int i) {
        filterValAdapterpackFeature.setitemselected(i);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        /*
        outState.putString("myselectedsuppliers", s);
        outState.putString("myselectedfeatures", s1);*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getSerializable("myselectedsuppliers");
        Log.d("SelectedStuffs", "onRestoreInstanceState: "
                +savedInstanceState.getString("myselectedsuppliers") +"\n"+ savedInstanceState.getString("myselectedfeatures"));
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
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.reset:
                for(FilterDefaultMultipleListModel model:supplierMultipleListModels)
                {
                    model.setChecked(false);
                }
                for(FilterDefaultMultipleListModel model:featuresMultipleListModels)
                {
                    model.setChecked(false);
                }
                filterValAdapterSupl.notifyDataSetChanged();
                filterValAdapterpackFeature.notifyDataSetChanged();
                break;
            case R.id.apply_filter:
              /*  if(SelectedFeatures!=null || SelectedSupplier!=null)
                {
                    SelectedSupplier.clear();
                    SelectedFeatures.clear();
                }*/


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
                    listModel.setSupplier("NoSuppliers");
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
                    listModel.setFeatures("NoFeatures");
                }
                tinyDB.putListString("listSup",SelectedSupplier);
                tinyDB.putListString("listFet",SelectedFeatures);

//                tinyDB.remove("listSup");
//                tinyDB.remove("listFet");

                Intent filrestintent=new Intent();
                filrestintent.putExtra(FILTER_RESPONSE,listModel);
                setResult(FILTER_RESPONSE_CODE,filrestintent);
//                Log.d("VKK", gson.toJson(listModel));
                finish();
//                moveTaskToBack(true);
                break;
        }

    }

    private void createMyDialog(){

        dialog = new Dialog(FilterListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter_back);
        dialog.setCancelable(false);
    }

    public void dialog_filter_back_exit(View view){
        FilterDefaultMultipleListModel listModel=new FilterDefaultMultipleListModel();
        listModel.setSupplier("NoSuppliers");
        listModel.setFeatures("NoFeatures");
        String s = new Gson().toJson(SelectedSupplier);
        String s1 = new Gson().toJson(SelectedFeatures);

        tinyDB.putListString("listSup",SelectedSupplier);
        tinyDB.putListString("listFet",SelectedFeatures);

        Intent filrestintent=new Intent();
        filrestintent.putExtra(FILTER_RESPONSE,listModel);
        setResult(FILTER_RESPONSE_CODE,filrestintent);
        finish();

    }

    public void dialog_filter_back_apply_filter(View view){

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
            listModel.setSupplier("NoSuppliers");
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
            listModel.setFeatures("NoFeatures");
        }

//                tinyDB.clear();

        tinyDB.putListString("listSup",SelectedSupplier);
        tinyDB.putListString("listFet",SelectedFeatures);

//                tinyDB.remove("listSup");
//                tinyDB.remove("listFet");

        Intent filrestintent=new Intent();
        filrestintent.putExtra(FILTER_RESPONSE,listModel);
        setResult(FILTER_RESPONSE_CODE,filrestintent);
       // Log.d("VKK", gson.toJson(listModel));
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       // dialog.show();

    }
}
