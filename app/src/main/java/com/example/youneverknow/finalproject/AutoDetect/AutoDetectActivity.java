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
                .setContentTitle(dataFor10days.data[0].description)
                .setContentText(round2decimal(round2decimal(String.valueOf((dataFor10days.data[0].temperature - 273)))) + (char) 0x00B0 + "C" ).setSmallIcon(getIcon(curDescription))
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

    int getIcon(String description){
        if(description.equals(getString(R.string.thunderStormWithLightRain)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithRain)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithHeavyRain)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.lightThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.heavyThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.raggedThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithLightDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithHeavyDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithRain)))
            return R.drawable.tstorm3;

        else if(description.equals(getString(R.string.lightIntensityDrizzle)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.drizzle)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.heavyIntensityDrizzle)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.lightIntensityDrizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.drizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.heavyIntensityDrizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.showerRainAndDrizzle)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.heavyShowerRainAndDrizzle)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.showerDrizzle)))
            return R.drawable.shower3;

        else if(description.equals(getString(R.string.lightRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.moderateRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.heavyIntensityRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.veryHeavyRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.extremeRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.freezingRain)))
            return R.drawable.hail;
        else if(description.equals(getString(R.string.lightIntensityShowerRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.showerRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.heavyIntensityShowerRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.raggedShowerRain)))
            return R.drawable.shower3;

        else if(description.equals(getString(R.string.lightSnow)))
            return R.drawable.snow4;
        else if(description.equals(getString(R.string.snow)))
            return R.drawable.snow4;
        else if(description.equals(getString(R.string.heavySnow)))
            return R.drawable.snow5;
        else if(description.equals(getString(R.string.sleet)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.showerSleet)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.lightRainAndSnow)))
            return R.drawable.snow4;
        else if(description.equals(getString(R.string.rainAndSnow)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.lightShowerSnow)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.showerSnow)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.heavyShowerSnow)))
            return R.drawable.sleet;

        else if(description.equals(getString(R.string.mist)))
            return R.drawable.mist;
        else if(description.equals(getString(R.string.smoke)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.haze)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.sandDustWhirls)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.fog)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.sand)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.dust)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.volcanicAsh)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.squalls)))
            return R.drawable.squalls;
        else if(description.equals(getString(R.string.tornado)))
            return R.drawable.tornado;

        else if(description.equals(getString(R.string.clearSky)))
            return R.drawable.sunny;
        else if(description.equals(getString(R.string.fewClouds)))
            return R.drawable.cloudy1;
        else if(description.equals(getString(R.string.scatteredClouds)))
            return R.drawable.cloudy2;
        else if(description.equals(getString(R.string.brokenClouds)))
            return R.drawable.cloudy5;
        else if(description.equals(getString(R.string.overcastClouds)))
            return R.drawable.overcast;
        else if(description.equals(getString(R.string.skyIsClear)))
            return R.drawable.sunny;
        else
            return R.drawable.dunno;
    }
}
