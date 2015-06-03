package com.example.youneverknow.finalproject.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youneverknow.finalproject.AutoDetect.getLocation;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.DataClass.dataFor10daysNode;
import com.example.youneverknow.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class getWeatherUsingCoordinate extends AsyncTask<Void, Void, Void>{

    private final Activity activity;
    private double latitude, longitude;
    JSONObject jsonData;


    public getWeatherUsingCoordinate(Activity context, double latitude, double longitude){
        this.activity = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String strUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + latitude + "&lon=" + longitude + "&cnt=10&mode=json";
            URL url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder data = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null){
                data.append(str);
            }
            in.close();
            jsonData = new JSONObject(data.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Create new data */
        dataFor10days.data= new dataFor10daysNode[10];
        for(int i = 0; i < 10; i++){
            try {
                dataFor10days.data[i] = new dataFor10daysNode();
                dataFor10days.data[i].temperature = Double.parseDouble(jsonData.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getString("day"));
                dataFor10days.data[i].icon = jsonData.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                dataFor10days.data[i].description = jsonData.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                dataFor10days.data[i].pressure = Double.parseDouble(jsonData.getJSONArray("list").getJSONObject(i).getString("pressure"));
                dataFor10days.data[i].humidity = Double.parseDouble(jsonData.getJSONArray("list").getJSONObject(i).getString("humidity"));
                dataFor10days.data[i].wind = Double.parseDouble(jsonData.getJSONArray("list").getJSONObject(i).getString("speed"));

                if(jsonData.getJSONArray("list").getJSONObject(i).has("rain"))
                    dataFor10days.data[i].rain = Double.parseDouble(jsonData.getJSONArray("list").getJSONObject(i).getString("rain"));
                else
                    dataFor10days.data[i].rain = 0;

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
    }
}
