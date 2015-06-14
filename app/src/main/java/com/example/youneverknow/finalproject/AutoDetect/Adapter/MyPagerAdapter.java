package com.example.youneverknow.finalproject.AutoDetect.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.youneverknow.finalproject.R;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class MyPagerAdapter  extends FragmentPagerAdapter{

    public String[] TITLES;

    public MyPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        String toDay = context.getResources().getString(R.string.weatherToday);
        String _5days = context.getResources().getString(R.string.weatherIn5Days);
        String _10days = context.getResources().getString(R.string.weatherIn10Days);
        TITLES = new String[]{toDay, _5days, _10days};
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
