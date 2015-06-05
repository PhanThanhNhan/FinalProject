package com.example.youneverknow.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.youneverknow.finalproject.AsyncTask.getWeatherUsingCoordinate;
import com.example.youneverknow.finalproject.AutoDetect.AutoDetectActivity;
import com.example.youneverknow.finalproject.AutoDetect.getLocation;
import com.example.youneverknow.finalproject.ChooseOnMap.ChooseOnMapActivity;
import com.example.youneverknow.finalproject.DataClass.dataFor10daysNode;
import com.example.youneverknow.finalproject.EnterLocation.EnterLocationActivity;
import com.example.youneverknow.finalproject.Settings.SettingActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends FragmentActivity {

    private Button btnMainAutoDetect, btnMainEnterLocation, btnMainChooseOnMap, btnMainSettings;
    public static double curLatitude, curLongitude;
    private final int CHOOSE_ON_MAP_RESULT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getControl();
        setButtonClickEvents();

    }

    public void getControl(){
        btnMainAutoDetect = (Button) findViewById(R.id.btnMainAutoDetect);
        btnMainEnterLocation = (Button) findViewById(R.id.btnMainEnterLocation);
        btnMainChooseOnMap = (Button) findViewById(R.id.btnMainChooseOnMap);
        btnMainSettings = (Button) findViewById(R.id.btnMainSettings);
    }

    public void setButtonClickEvents(){
        btnMainAutoDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), AutoDetectActivity.class);
                startActivity(iGo);
            }
        });

        btnMainEnterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWeatherUsingCoordinate.loadSaveData(MainActivity.this);

                Intent iGo = new Intent(v.getContext(), EnterLocationActivity.class);
                startActivity(iGo);
            }
        });

        btnMainChooseOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), ChooseOnMapActivity.class);
                startActivityForResult(iGo, CHOOSE_ON_MAP_RESULT_CODE);
            }
        });

        btnMainSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), SettingActivity.class);
                startActivity(iGo);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_ON_MAP_RESULT_CODE){
            if(resultCode == RESULT_OK){

                getWeatherUsingCoordinate d = new getWeatherUsingCoordinate(MainActivity.this, curLatitude, curLongitude, true);
                d.execute();

                Toast.makeText(getApplicationContext(), "Latitude: " + curLatitude + "\nLongtitude: " + curLongitude, Toast.LENGTH_LONG).show();

            }
        }
    }
}
