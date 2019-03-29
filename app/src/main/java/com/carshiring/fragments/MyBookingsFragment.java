package com.carshiring.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carshiring.R;
import com.carshiring.adapters.Page_Adapter;


/**
 * Created by rakhi on  13/3/2018.
 */

public class MyBookingsFragment extends Fragment {
    View view,v;
    ViewPager pager;
    String s;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_my_bookings,container,false);
       /* Toolbar toolbar =   ((MainActivity)getActivity()).toolbar ;
        AnimatioonUtils.animateToolbarDroppingDown(toolbar);*/

        TabLayout tabLayout= (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.current)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.previous)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.quotes)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
         pager= (ViewPager) view.findViewById(R.id.viewpager_view);
        CurrentBookingFragment tab1=new CurrentBookingFragment();
        PreviousBookingFragment tab2=new PreviousBookingFragment();
        QuotesFragment tab3=new QuotesFragment();
        Page_Adapter bookingPageAdapter=new Page_Adapter(getChildFragmentManager(),tabLayout.getTabCount(),tab1,tab2,tab3);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(bookingPageAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            s= String.valueOf(bundle.getSerializable("Check"));
            if(s.equalsIgnoreCase("Quotes")) {
                pager.setCurrentItem(3);
            }
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
