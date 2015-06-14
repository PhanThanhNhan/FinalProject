package com.example.youneverknow.finalproject.AutoDetect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.youneverknow.finalproject.AsyncTask.getWeatherUsingCoordinate;
import com.example.youneverknow.finalproject.AutoDetect.Adapter.MyPagerAdapter;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.MainActivity;
import com.example.youneverknow.finalproject.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class AutoDetectActivity extends FragmentActivity{

    public static boolean isCalculated = false;
    public static int curTemperature;
    public static String curDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autodetect);

        setTabLayout();
        TextView tvAutoDetectCityName = (TextView) findViewById(R.id.tvAutoDetectCityname);
        tvAutoDetectCityName.setText(dataFor10days.cityName);
        if (MainActivity.isButtonPressed){
            isCalculated = true;
            curTemperature = (int) dataFor10days.data[0].temperature;
            curDescription = dataFor10days.data[0].description;
            setNotification();
        }

    }

    public void setTabLayout(){
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

    public void setNotification(){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = new Notification.Builder(this)
                .setContentTitle(formalString(dataFor10days.data[0].description))
                .setContentText(round2decimal(round2decimal(String.valueOf((dataFor10days.data[0].temperature - 273)))) + (char) 0x00B0 + "C" ).setSmallIcon(getIcon(curDescription, curTemperature))
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(0, noti);
    }

    String round2decimal(String number){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number.length(); i++){
            if(number.charAt(i) == '.'){
                stringBuilder.append(number.charAt(i));
                stringBuilder.append(number.charAt(i + 1));
                if(i + 2 >= number.length())
                    stringBuilder.append("0");
                else
                    stringBuilder.append(number.charAt(i + 2));

                break;
            }
            stringBuilder.append(number.charAt(i));
        }
        return stringBuilder.toString();
    }

    String formalString(String str){
        if(str == "")
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((char)(str.charAt(0) - 32));
        for (int i = 1; i < str.length(); i++){
            if(str.charAt(i) == '-'){
                stringBuilder.append(" ");
                continue;
            }
            stringBuilder.append(str.charAt(i));
        }
        return stringBuilder.toString();
    }

    int getIcon(String description, double temperature){
        if(description.toLowerCase().contains(getString(R.string.clear)))
        {
            if(temperature < 10)
                return R.drawable.snow4;
            if (temperature < 30)
                return R.drawable.fog;
            else
                return R.drawable.sunny;
        }

        if(description.toLowerCase().contains(getString(R.string.sunny)))
            return R.drawable.cloudy1;
        if(description.toLowerCase().contains(getString(R.string.cloud)))
            return R.drawable.cloudy5;
        if(description.toLowerCase().contains(getString(R.string.rain)))
            return R.drawable.light_rain;
        if(description.toLowerCase().contains(getString(R.string.snow)))
            return R.drawable.snow4;
        return R.drawable.dunno;
    }
}
