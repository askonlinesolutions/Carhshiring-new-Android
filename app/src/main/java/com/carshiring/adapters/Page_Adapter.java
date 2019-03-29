package com.carshiring.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by sony on 01-05-2017.
 */

public class Page_Adapter extends FragmentPagerAdapter {
    int mNumTabs;
    Fragment tab1,tab2,tab3;
    public Page_Adapter(FragmentManager fm, int mNumTabs, Fragment tabA, Fragment tabB, Fragment tabC)
    {
        super(fm);
        this.mNumTabs=mNumTabs;
         tab1=tabA;
        tab2=tabB;
        tab3=tabC;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
            //    CurrentBookingFragment tab1=new CurrentBookingFragment();
                return tab1;
            case 1:
          //   PreviousBookingFragment tab2=new PreviousBookingFragment();
                return tab2;
            case 2:
              //  QuotesFragment tab3=new QuotesFragment();
                return tab3;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return mNumTabs;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
