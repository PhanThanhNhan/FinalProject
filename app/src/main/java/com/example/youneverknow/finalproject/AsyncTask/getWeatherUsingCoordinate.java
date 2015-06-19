package com.example.youneverknow.finalproject.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.youneverknow.finalproject.AutoDetect.AutoDetectActivity;
import com.example.youneverknow.finalproject.AutoDetect.getLocation;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.DataClass.dataFor10daysNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5days;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysDayNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysTimeNode;
import com.example.youneverknow.finalproject.MainActivity;
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
    private boolean isHere;
    public static final String spToday = "spToday";
    public static final String sp5days = "sp5days";
    public static final String sp10days = "sp10days";


    JSONObject jsonDataToday, jsonData5days, jsonData10days;


    public getWeatherUsingCoordinate(Activity context, double latitude, double longitude, boolean isHere){
        this.activity = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isHere = isHere;
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
                    if(jsonData5days.getJSONArray("list").length()%8 != 0){
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
                    } else {
                        for (int j = 0; j < 8; j++){
                            dataFor5days.data[i].time[j] = new dataFor5daysTimeNode();
                            dataFor5days.data[i].time[j].temperature = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("temp"));
                            dataFor5days.data[i].time[j].icon = jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("icon");
                            dataFor5days.data[i].time[j].description = jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("description");
                            dataFor5days.data[i].time[j].pressure = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("pressure"));
                            dataFor5days.data[i].time[j].humidity = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("humidity"));
                            dataFor5days.data[i].time[j].wind = Double.parseDouble(jsonData5days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("wind").getString("speed"));
                            ++tempJsonPos;
                        }
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

        // This happens when user touches AutoDetect button
        if(isHere){
            SharedPreferences preToday = activity.getApplicationContext().getSharedPreferences(spToday, Context.MODE_PRIVATE);
            SharedPreferences.Editor edtToday = preToday.edit();
            edtToday.putString("json", jsonDataToday.toString());
            edtToday.commit();

            SharedPreferences pre5days = activity.getApplicationContext().getSharedPreferences(sp5days, Context.MODE_PRIVATE);
            SharedPreferences.Editor edt5days = pre5days.edit();
            edt5days.putString("json", jsonData5days.toString());
            edt5days.commit();

            SharedPreferences pre10days = activity.getApplicationContext().getSharedPreferences(sp10days, Context.MODE_PRIVATE);
            SharedPreferences.Editor edt10days = pre10days.edit();
            edt10days.putString("json", jsonData10days.toString());
            edt10days.commit();
        }

        // Modifying weather description
        modifyDescription();

        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        CircularProgressButton btnMainAutoDetect = (CircularProgressButton) activity.findViewById(R.id.btnMainAutoDetect);
        btnMainAutoDetect.setProgress(0);
        btnMainAutoDetect.setIdleText("Auto-Detect");

        CircularProgressButton btnMainChoosOnMap = (CircularProgressButton) activity.findViewById(R.id.btnMainChooseOnMap);
        btnMainChoosOnMap.setProgress(0);

        CircularProgressButton btnMainEnterLocation = (CircularProgressButton) activity.findViewById(R.id.btnMainEnterLocation);
        btnMainEnterLocation.setProgress(0);

        Intent iGo = new Intent(activity, AutoDetectActivity.class);
        activity.startActivity(iGo);
    }

    public static String convertTime(String time){
        long dv = Long.valueOf(time) * 1000;
        Date df = new Date(dv);
        String vv = new SimpleDateFormat("hh:mma").format(df);
        return vv;
    }


    public void modifyDescription(){
        // 10 days description
        for (int i = 0; i < 10; i++){
            dataFor10days.data[i].description = chooseDescription(dataFor10days.data[i].description);
        }
        // 5 days description
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 8; j++){
                dataFor5days.data[i].time[j].description = chooseDescription(dataFor5days.data[i].time[j].description);
            }
        }
    }

    public String chooseDescription(String description){
        switch (description){
            case "thunderstorm with light rain":
                return activity.getString(R.string.thunderStormWithLightRain);
            case "thunderstorm with rain":
                return activity.getString(R.string.thunderStormWithRain);
            case "thunderstorm with heavy rain":
                return activity.getString(R.string.thunderStormWithHeavyRain);
            case "light thunderstorm":
                return activity.getString(R.string.lightThunderStorm);
            case "thunderstorm":
                return activity.getString(R.string.thunderStorm);
            case "heavy thunderstorm":
                return activity.getString(R.string.heavyThunderStorm);
            case "ragged thunderstorm":
                return activity.getString(R.string.raggedThunderStorm);
            case "thunderstorm with light drizzle":
                return activity.getString(R.string.thunderStormWithLightDrizzle);
            case "thunderstorm with drizzle":
                return activity.getString(R.string.thunderStormWithDrizzle);
            case "thunderstorm with heavy drizzle":
                return activity.getString(R.string.thunderStormWithHeavyDrizzle);

            case "light intensity drizzle":
                return activity.getString(R.string.lightIntensityDrizzle);
            case "drizzle":
                return activity.getString(R.string.drizzle);
            case "heavy intensity drizzle":
                return activity.getString(R.string.heavyIntensityDrizzle);
            case "light intensity drizzle rain":
                return activity.getString(R.string.lightIntensityDrizzleRain);
            case "drizzle rain":
                return activity.getString(R.string.drizzleRain);
            case "heavy intensity drizzle rain":
                return activity.getString(R.string.heavyIntensityDrizzleRain);
            case "shower rain and drizzle":
                return activity.getString(R.string.showerRainAndDrizzle);
            case "heavy shower rain and drizzle":
                return activity.getString(R.string.heavyShowerRainAndDrizzle);
            case "shower drizzle":
                return activity.getString(R.string.showerDrizzle);

            case "light rain":
                return activity.getString(R.string.lightRain);
            case "moderate rain":
                return activity.getString(R.string.moderateRain);
            case "heavy intensity rain":
                return activity.getString(R.string.heavyIntensityRain);
            case "very heavy rain":
                return activity.getString(R.string.veryHeavyRain);
            case "extreme rain":
                return activity.getString(R.string.extremeRain);
            case "freezing rain":
                return activity.getString(R.string.freezingRain);
            case "light intensity shower rain":
                return activity.getString(R.string.lightIntensityShowerRain);
            case "shower rain":
                return activity.getString(R.string.showerRain);
            case "heavy intensity shower rain":
                return activity.getString(R.string.heavyIntensityShowerRain);
            case "ragged shower rain":
                return activity.getString(R.string.raggedShowerRain);

            case "light snow":
                return activity.getString(R.string.lightSnow);
            case "snow":
                return activity.getString(R.string.snow);
            case "heavy snow":
                return activity.getString(R.string.heavySnow);
            case "sleet":
                return activity.getString(R.string.sleet);
            case "shower sleet":
                return activity.getString(R.string.showerSleet);
            case "light rain and snow":
                return activity.getString(R.string.lightRainAndSnow);
            case "rain and snow":
                return activity.getString(R.string.rainAndSnow);
            case "light shower snow":
                return activity.getString(R.string.lightShowerSnow);
            case "shower snow":
                return activity.getString(R.string.showerSnow);
            case "heavy shower snow":
                return activity.getString(R.string.heavyShowerSnow);

            case "mist":
                return activity.getString(R.string.mist);
            case "smoke":
                return activity.getString(R.string.smoke);
            case "haze":
                return activity.getString(R.string.haze);
            case "sand, dust whirls":
                return activity.getString(R.string.sandDustWhirls);
            case "fog":
                return activity.getString(R.string.fog);
            case "sand":
                return activity.getString(R.string.sand);
            case "dust":
                return activity.getString(R.string.dust);
            case "volcanic ash":
                return activity.getString(R.string.volcanicAsh);
            case "squalls":
                return activity.getString(R.string.squalls);
            case "tornado":
                return activity.getString(R.string.tornado);

            case "clear sky":
                return activity.getString(R.string.clearSky);
            case "few clouds":
                return activity.getString(R.string.fewClouds);
            case "scattered clouds":
                return activity.getString(R.string.scatteredClouds);
            case "broken clouds":
                return activity.getString(R.string.brokenClouds);
            case "overcast clouds":
                return activity.getString(R.string.overcastClouds);
            case "sky is clear":
                return activity.getString(R.string.skyIsClear);
        }
        return description;
    }

}
