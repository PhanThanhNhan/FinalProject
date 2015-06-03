package com.example.youneverknow.finalproject.AutoDetect;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.youneverknow.finalproject.AutoDetect.Adapter.MyPagerAdapter;
import com.example.youneverknow.finalproject.R;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class AutoDetectActivity extends FragmentActivity{

    protected double curLatitude, curLongitude;
    public static LineChartView chart_top, chart_bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autodetect);

        setTabLayout();
        tryGettingLocation();

    }
    public void tryGettingLocation(){
        getLocation gps = new getLocation(this);
        if(gps.canGetLocation()){
            curLatitude = gps.getLatitude();
            curLongitude = gps.getLongitude();
            /* Still getting */

                Toast.makeText(getApplicationContext(), "Latitude: " + curLatitude + "\nLongitude: " + curLongitude, Toast.LENGTH_SHORT).show();

        } else{
            gps.showSettingsAlert();
        }
    }

    public void setTabLayout(){
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

}
