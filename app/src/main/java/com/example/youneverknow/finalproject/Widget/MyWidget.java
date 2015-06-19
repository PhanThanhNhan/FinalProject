package com.example.youneverknow.finalproject.Widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.youneverknow.finalproject.AsyncTask.getWeatherUsingCoordinate;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.DataClass.dataFor10daysNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5days;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysDayNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysTimeNode;
import com.example.youneverknow.finalproject.MainActivity;
import com.example.youneverknow.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by YouNeverKnow on 6/5/2015.
 */
public class MyWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                MyWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            // create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.my_widget);
            Log.w("WidgetExample", String.valueOf(number));

            JSONObject tempJson10days = null;
            SharedPreferences pre10days = context.getApplicationContext().getSharedPreferences(sp10days, Context.MODE_PRIVATE);
            try {
                 tempJson10days = new JSONObject(pre10days.getString("json", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(tempJson10days == null || tempJson10days.toString().equals("")){
                remoteViews.setTextViewText(R.id.tvWidgetTemperature, context.getString(R.string.notFound));
                remoteViews.setTextViewText(R.id.tvWidgetDescription, context.getString(R.string.notFound));
                remoteViews.setTextViewText(R.id.tvWidgetHumidity, context.getString(R.string.notFound));
                remoteViews.setTextViewText(R.id.tvWidgetPressure, context.getString(R.string.notFound));
            } else {
                try {
                    curTemperature = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(0).getJSONObject("temp").getString("day"));
                    curDescription = tempJson10days.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
                    curPressure = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(0).getString("pressure"));
                    curHumidity = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(0).getString("humidity"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Settext
                remoteViews.setTextViewText(R.id.tvWidgetTemperature, round2decimal(String.valueOf(curTemperature - 273)) + (char) 0x00B0 + "C");
                remoteViews.setTextViewText(R.id.tvWidgetDescription, chooseDescription(context, curDescription));
                remoteViews.setTextViewText(R.id.tvWidgetHumidity, String.valueOf(curHumidity) + "%");
                remoteViews.setTextViewText(R.id.tvWidgetPressure, String.valueOf(curPressure) + "hPa");
                remoteViews.setImageViewResource(R.id.ivWidgetWeatherIcon, getIcon(curDescription, curTemperature - 273));
            }
            // Register an onClickListener
            Intent intent = new Intent(context, MyWidget.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btnWidgetRefresh, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    public static final String spToday = "spToday";
    public static final String sp5days = "sp5days";
    public static final String sp10days = "sp10days";
    private static double curTemperature, curHumidity, curPressure;
    private static String curDescription;

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

    int getIcon(String description, double temperature){
        if(description.toLowerCase().contains("clear"))
        {
            if(temperature < 10)
                return R.drawable.snow4;
            if (temperature < 30)
                return R.drawable.fog;
            else
                return R.drawable.sunny;
        }

        if(description.toLowerCase().contains("sunny"))
            return R.drawable.cloudy1;
        if(description.toLowerCase().contains("cloud"))
            return R.drawable.cloudy5;
        if(description.toLowerCase().contains("rain"))
            return R.drawable.light_rain;
        if(description.toLowerCase().contains("snow"))
            return R.drawable.snow4;
        return R.drawable.dunno;
    }

    public String chooseDescription(Context context, String description){
        switch (description){
            case "thunderstorm with light rain":
                return context.getString(R.string.thunderStormWithLightRain);
            case "thunderstorm with rain":
                return context.getString(R.string.thunderStormWithRain);
            case "thunderstorm with heavy rain":
                return context.getString(R.string.thunderStormWithHeavyRain);
            case "light thunderstorm":
                return context.getString(R.string.lightThunderStorm);
            case "thunderstorm":
                return context.getString(R.string.thunderStorm);
            case "heavy thunderstorm":
                return context.getString(R.string.heavyThunderStorm);
            case "ragged thunderstorm":
                return context.getString(R.string.raggedThunderStorm);
            case "thunderstorm with light drizzle":
                return context.getString(R.string.thunderStormWithLightDrizzle);
            case "thunderstorm with drizzle":
                return context.getString(R.string.thunderStormWithDrizzle);
            case "thunderstorm with heavy drizzle":
                return context.getString(R.string.thunderStormWithHeavyDrizzle);

            case "light intensity drizzle":
                return context.getString(R.string.lightIntensityDrizzle);
            case "drizzle":
                return context.getString(R.string.drizzle);
            case "heavy intensity drizzle":
                return context.getString(R.string.heavyIntensityDrizzle);
            case "light intensity drizzle rain":
                return context.getString(R.string.lightIntensityDrizzleRain);
            case "drizzle rain":
                return context.getString(R.string.drizzleRain);
            case "heavy intensity drizzle rain":
                return context.getString(R.string.heavyIntensityDrizzleRain);
            case "shower rain and drizzle":
                return context.getString(R.string.showerRainAndDrizzle);
            case "heavy shower rain and drizzle":
                return context.getString(R.string.heavyShowerRainAndDrizzle);
            case "shower drizzle":
                return context.getString(R.string.showerDrizzle);

            case "light rain":
                return context.getString(R.string.lightRain);
            case "moderate rain":
                return context.getString(R.string.moderateRain);
            case "heavy intensity rain":
                return context.getString(R.string.heavyIntensityRain);
            case "very heavy rain":
                return context.getString(R.string.veryHeavyRain);
            case "extreme rain":
                return context.getString(R.string.extremeRain);
            case "freezing rain":
                return context.getString(R.string.freezingRain);
            case "light intensity shower rain":
                return context.getString(R.string.lightIntensityShowerRain);
            case "shower rain":
                return context.getString(R.string.showerRain);
            case "heavy intensity shower rain":
                return context.getString(R.string.heavyIntensityShowerRain);
            case "ragged shower rain":
                return context.getString(R.string.raggedShowerRain);

            case "light snow":
                return context.getString(R.string.lightSnow);
            case "snow":
                return context.getString(R.string.snow);
            case "heavy snow":
                return context.getString(R.string.heavySnow);
            case "sleet":
                return context.getString(R.string.sleet);
            case "shower sleet":
                return context.getString(R.string.showerSleet);
            case "light rain and snow":
                return context.getString(R.string.lightRainAndSnow);
            case "rain and snow":
                return context.getString(R.string.rainAndSnow);
            case "light shower snow":
                return context.getString(R.string.lightShowerSnow);
            case "shower snow":
                return context.getString(R.string.showerSnow);
            case "heavy shower snow":
                return context.getString(R.string.heavyShowerSnow);

            case "mist":
                return context.getString(R.string.mist);
            case "smoke":
                return context.getString(R.string.smoke);
            case "haze":
                return context.getString(R.string.haze);
            case "sand, dust whirls":
                return context.getString(R.string.sandDustWhirls);
            case "fog":
                return context.getString(R.string.fog);
            case "sand":
                return context.getString(R.string.sand);
            case "dust":
                return context.getString(R.string.dust);
            case "volcanic ash":
                return context.getString(R.string.volcanicAsh);
            case "squalls":
                return context.getString(R.string.squalls);
            case "tornado":
                return context.getString(R.string.tornado);

            case "clear sky":
                return context.getString(R.string.clearSky);
            case "few clouds":
                return context.getString(R.string.fewClouds);
            case "scattered clouds":
                return context.getString(R.string.scatteredClouds);
            case "broken clouds":
                return context.getString(R.string.brokenClouds);
            case "overcast clouds":
                return context.getString(R.string.overcastClouds);
            case "sky is clear":
                return context.getString(R.string.skyIsClear);
        }
        return description;
    }

}
