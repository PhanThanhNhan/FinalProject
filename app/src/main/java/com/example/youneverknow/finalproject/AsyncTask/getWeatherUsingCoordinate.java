package com.example.youneverknow.finalproject.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youneverknow.finalproject.AutoDetect.AutoDetectActivity;
import com.example.youneverknow.finalproject.AutoDetect.getLocation;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.DataClass.dataFor10daysNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5days;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysDayNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysTimeNode;
import com.example.youneverknow.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class getWeatherUsingCoordinate extends AsyncTask<Void, Void, Void>{

    private final Activity activity;
    private double latitude, longitude;
    JSONObject jsonDataToday, jsonData5days, jsonData10days;


    public getWeatherUsingCoordinate(Activity context, double latitude, double longitude){
        this.activity = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Void doInBackground(Void... params) {

        /************************************************************** Get data for today ****************************************************************/
        try {
            String strUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude;
            URL url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder data = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null){
                data.append(str);
            }
            in.close();
            jsonDataToday = new JSONObject(data.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /************************************************************** Get data for 10 days **************************************************************/

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
            jsonData10days = new JSONObject(data.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Handle json data */
        dataFor10days.data = new dataFor10daysNode[10];
        try {
            dataFor10days.cityName = jsonData10days.getJSONObject("city").getString("name");
            dataFor10days.sunRise = convertTime(jsonDataToday.getJSONObject("sys").getString("sunrise"));
            dataFor10days.sunSet = convertTime(jsonDataToday.getJSONObject("sys").getString("sunset"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 10; i++){
            try {
                dataFor10days.data[i] = new dataFor10daysNode();
                dataFor10days.data[i].temperature = Double.parseDouble(jsonData10days.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getString("day"));
                dataFor10days.data[i].icon = jsonData10days.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                dataFor10days.data[i].description = jsonData10days.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                dataFor10days.data[i].pressure = Double.parseDouble(jsonData10days.getJSONArray("list").getJSONObject(i).getString("pressure"));
                dataFor10days.data[i].humidity = Double.parseDouble(jsonData10days.getJSONArray("list").getJSONObject(i).getString("humidity"));
                dataFor10days.data[i].wind = Double.parseDouble(jsonData10days.getJSONArray("list").getJSONObject(i).getString("speed"));

                if(jsonData10days.getJSONArray("list").getJSONObject(i).has("rain"))
                    dataFor10days.data[i].rain = Double.parseDouble(jsonData10days.getJSONArray("list").getJSONObject(i).getString("rain"));
                else
                    dataFor10days.data[i].rain = 0;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        /************************************************************** Get data for 5 days **************************************************************/

        try {
            String strUrl = "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude;
            URL url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder data = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null){
                data.append(str);
            }
            in.close();
            jsonData5days = new JSONObject(data.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Handle json data */
        dataFor5days.data = new dataFor5daysDayNode[5];
        int tempJsonPos = 0;
        for (int i = 0; i < 5; i++){
            dataFor5days.data[i] = new dataFor5daysDayNode();
            dataFor5days.data[i].time = new dataFor5daysTimeNode[8];
            if(i == 0){
                try{
                    for(int j = 0; j < (8 - jsonData5days.getJSONArray("list").length()%8); j++){
                        dataFor5days.data[i].time[j] = new dataFor5daysTimeNode();
                        dataFor5days.data[i].time[j].temperature = 273;
                        dataFor5days.data[i].time[j].icon = "";
                        dataFor5days.data[i].time[j].description = "";
                        dataFor5days.data[i].time[j].pressure = 0;
                        dataFor5days.data[i].time[j].humidity = 0;
                        dataFor5days.data[i].time[j].wind = 0;
                    }
                    for (int j = (8 - jsonData5days.getJSONArray("list").length()%8); j < 8; j++){
                        dataFor5days.data[i].time[j] = new dataFor5daysTimeNode();
                        dataFor5days.data[i].time[j].temperature = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("temp"));
                        dataFor5days.data[i].time[j].icon = jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("icon");
                        dataFor5days.data[i].time[j].description = jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("description");
                        dataFor5days.data[i].time[j].pressure = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("pressure"));
                        dataFor5days.data[i].time[j].humidity = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("humidity"));
                        dataFor5days.data[i].time[j].wind = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("wind").getString("speed"));
                        ++tempJsonPos;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                for (int j = 0; j < 8; j++){
                    try{
                        dataFor5days.data[i].time[j] = new dataFor5daysTimeNode();
                        dataFor5days.data[i].time[j].temperature = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("temp"));
                        dataFor5days.data[i].time[j].icon = jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("icon");
                        dataFor5days.data[i].time[j].description = jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("description");
                        dataFor5days.data[i].time[j].pressure = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("pressure"));
                        dataFor5days.data[i].time[j].humidity = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("humidity"));
                        dataFor5days.data[i].time[j].wind = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("wind").getString("speed"));
                        ++tempJsonPos;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
        Intent iGo = new Intent(activity, AutoDetectActivity.class);
        activity.startActivity(iGo);
    }

    public String convertTime(String time){
        long dv = Long.valueOf(time) * 1000;
        Date df = new Date(dv);
        String vv = new SimpleDateFormat("hh:mma").format(df);
        return vv;
    }
}
