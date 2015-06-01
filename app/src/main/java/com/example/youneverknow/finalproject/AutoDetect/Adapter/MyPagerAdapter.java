package com.example.youneverknow.finalproject.AutoDetect.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class MyPagerAdapter  extends FragmentPagerAdapter{

    private final String[] TITLES = {"WEATHER TODAY", "NEXT 6 DAYS"};

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SuperAwesomeCardFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
