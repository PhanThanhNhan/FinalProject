package com.example.youneverknow.finalproject.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.youneverknow.finalproject.AutoDetect.getLocation;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class getWeatherUsingCoordinate extends AsyncTask<Void, Void, Void>{

    private final Context context;
    private double latitude, longitude;

    public getWeatherUsingCoordinate(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        getLocation gps = new getLocation(context);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        } else {
            gps.showSettingsAlert();
            return null;
        }


        return null;
    }
}
