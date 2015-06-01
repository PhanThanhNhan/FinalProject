package com.example.youneverknow.finalproject.AutoDetect;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;
import com.example.youneverknow.finalproject.AutoDetect.Adapter.MyPagerAdapter;
import com.example.youneverknow.finalproject.R;

/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class AutoDetectActivity extends FragmentActivity{

    protected double curLatitude, curLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autodetect);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(pager);




    }
    public void tryGettingLocation(){
        getLocation gps = new getLocation(this);
        if(gps.canGetLocation()){
            curLatitude = gps.getLatitude();
            curLongitude = gps.getLongitude();
            /* Still getting */
            if(curLatitude == 0 && curLongitude == 0){

            }
        } else{

        }
    }
}
