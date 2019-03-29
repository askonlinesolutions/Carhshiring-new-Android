package com.carshiring.activities.home;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.carshiring.R;
import com.carshiring.adapters.MyExListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.carshiring.activities.home.CarDetailActivity.coveragelist;

public class FullProtectionActivity extends AppCompatActivity {
    ExpandableListView expListView;
    List<String> listparent;
    HashMap<String,List<String>> listchild;
    MyExListAdapter myExListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_protection);
        preaprelist();
        expListView= (ExpandableListView) findViewById(R.id.list_protect);
        myExListAdapter=new MyExListAdapter(this,listparent,listchild);
        expListView.setAdapter(myExListAdapter);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setTitle(getResources().getString(R.string.full_protection));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    private void preaprelist() {
        listparent=new ArrayList<>();
        listchild=new HashMap<>();
        if(coveragelist.size()>0)
        {
            for (int i=0;i<coveragelist.size();i++)
            {
                List<String> pro=new ArrayList<String>();
                listparent.add(coveragelist.get(i).getName());
                pro.add(coveragelist.get(i).getAmount()+" "+ coveragelist.get(i).getCurrency()+"\n"+coveragelist.get(i).getDesc());
                listchild.put(listparent.get(i),pro);
            }
        }


       /*  listparent.add("Full Protection-Excellent Value,Best Cover,Peace of Mind");
       listparent.add("What does our Full Protection Product cover?");
        listparent.add("What does our Full Protection Product not cover?");
        listparent.add(getResources().getString(R.string.dummy));
        listparent.add(getResources().getString(R.string.dummy));
        listparent.add(getResources().getString(R.string.dummy));
        listparent.add(getResources().getString(R.string.dummy));

        List<String> pro=new ArrayList<String>();
        pro.add(getResources().getString(R.string.dummy));

        List<String> secon=new ArrayList<>();
        secon.add(getResources().getString(R.string.dummy));


        listchild.put(listparent.get(0),pro);
        listchild.put(listparent.get(ab),secon);
        listchild.put(listparent.get(2),secon);
        listchild.put(listparent.get(3),secon);
        listchild.put(listparent.get(4),secon);
        listchild.put(listparent.get(5),secon);
        listchild.put(listparent.get(6),secon);*/
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
}
