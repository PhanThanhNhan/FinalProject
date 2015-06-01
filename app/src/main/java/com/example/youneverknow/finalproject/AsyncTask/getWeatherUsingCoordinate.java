package com.example.youneverknow.finalproject.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.youneverknow.finalproject.AutoDetect.getLocation;
import com.example.youneverknow.finalproject.R;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class getWeatherUsingCoordinate extends AsyncTask<Void, Void, Void>{

    private final Activity activity;
    private double latitude, longitude;

    public getWeatherUsingCoordinate(Activity context){
        this.activity = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        TextView textView = (TextView) activity.findViewById(R.id.textView);
    }
}
