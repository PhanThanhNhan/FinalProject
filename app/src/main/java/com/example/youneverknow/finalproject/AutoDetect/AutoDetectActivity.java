package com.example.youneverknow.finalproject.AutoDetect;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.youneverknow.finalproject.AsyncTask.getWeatherUsingCoordinate;
import com.example.youneverknow.finalproject.AutoDetect.Adapter.MyPagerAdapter;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.MainActivity;
import com.example.youneverknow.finalproject.R;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class AutoDetectActivity extends FragmentActivity{

    public static LineChartView chart_top, chart_bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autodetect);

        setTabLayout();
        TextView tvAutoDetectCityName = (TextView) findViewById(R.id.tvAutoDetectCityname);
        tvAutoDetectCityName.setText(dataFor10days.cityName);
    }



    public void setTabLayout(){
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

}
