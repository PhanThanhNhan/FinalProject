package com.example.youneverknow.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dd.CircularProgressButton;
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

    private CircularProgressButton btnMainAutoDetect, btnMainEnterLocation, btnMainChooseOnMap, btnMainSettings;;

    public static double curLatitude, curLongitude;
    private final int CHOOSE_ON_MAP_RESULT_CODE = 1;
    private final int ENTER_LOCATION_RESULT_CODE = 2;
    public boolean isLocationGotten = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getControl();
        setButtonClickEvents();
    }

    public void getControl(){
        btnMainAutoDetect = (CircularProgressButton) findViewById(R.id.btnMainAutoDetect);
        btnMainEnterLocation = (CircularProgressButton) findViewById(R.id.btnMainEnterLocation);
        btnMainChooseOnMap = (CircularProgressButton) findViewById(R.id.btnMainChooseOnMap);
        btnMainSettings = (CircularProgressButton) findViewById(R.id.btnMainSettings);
    }

    public void setButtonClickEvents(){
        btnMainAutoDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocationGotten = false;
                btnMainAutoDetect.setIndeterminateProgressMode(true);
                btnMainAutoDetect.setProgress(5);
                tryGettingLocation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(isLocationGotten){
                            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            if (!mWifi.isConnected()){
                                Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
                                btnMainAutoDetect.setProgress(0);
                                btnMainAutoDetect.setIdleText("Retry");
                                return;
                            }
                            btnMainAutoDetect.setProgress(100);
                            getWeatherUsingCoordinate weatherAsyncTask = new getWeatherUsingCoordinate(MainActivity.this, curLatitude, curLongitude, true);
                            weatherAsyncTask.execute();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "GPS location not found", Toast.LENGTH_LONG).show();
                            btnMainAutoDetect.setProgress(-1);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    btnMainAutoDetect.setProgress(0);
                                    btnMainAutoDetect.setIdleText("Retry");
                                }
                            }, 1000);

                            return;
                        }
                    }
                }, 3000);
            }
        });

        btnMainEnterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), EnterLocationActivity.class);
                startActivityForResult(iGo, ENTER_LOCATION_RESULT_CODE);
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
            if(curLatitude == 0 && curLatitude == 0)
                isLocationGotten = false;
            else isLocationGotten = true;
        } else{
            isLocationGotten = false;
            gps.showSettingsAlert();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_ON_MAP_RESULT_CODE){
            if(resultCode == RESULT_OK){

                btnMainChooseOnMap.setIndeterminateProgressMode(true);
                btnMainChooseOnMap.setProgress(5);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getWeatherUsingCoordinate weatherAsyncTask = new getWeatherUsingCoordinate(MainActivity.this, curLatitude, curLongitude, false);
                        weatherAsyncTask.execute();
                    }
                }, 1000);
            }
        } else if(requestCode == ENTER_LOCATION_RESULT_CODE){
            if(resultCode == RESULT_OK){
                btnMainEnterLocation.setIndeterminateProgressMode(true);
                btnMainEnterLocation.setProgress(5);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getWeatherUsingCoordinate weatherAsyncTask = new getWeatherUsingCoordinate(MainActivity.this, curLatitude, curLongitude, false);
                        weatherAsyncTask.execute();
                    }
                }, 1000);
            }
        }
    }
}
