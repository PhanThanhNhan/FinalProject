package com.example.youneverknow.finalproject.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.youneverknow.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by YouNeverKnow on 6/5/2015.
 */
public class MyWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        loadTemperatureSetting(context);
        loadPressureSetting(context);

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
                curDescription = chooseDescription(context, curDescription);
                remoteViews.setTextViewText(R.id.tvWidgetTemperature, temperatureCalculator());
                remoteViews.setTextViewText(R.id.tvWidgetDescription, curDescription);
                remoteViews.setTextViewText(R.id.tvWidgetHumidity, String.valueOf(curHumidity) + "%");
                remoteViews.setTextViewText(R.id.tvWidgetPressure, pressureCalculator());
                remoteViews.setImageViewResource(R.id.ivWidgetWeatherIcon, getIcon(context, curDescription));
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

    int getIcon(Context context, String description){
        if(description.equals(context.getString(R.string.thunderStormWithLightRain)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.thunderStormWithRain)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.thunderStormWithHeavyRain)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.lightThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.thunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.heavyThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.raggedThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.thunderStormWithLightDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.thunderStormWithDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.thunderStormWithHeavyDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(context.getString(R.string.thunderStormWithRain)))
            return R.drawable.tstorm3;

        else if(description.equals(context.getString(R.string.lightIntensityDrizzle)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.drizzle)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.heavyIntensityDrizzle)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.lightIntensityDrizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.drizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.heavyIntensityDrizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.showerRainAndDrizzle)))
            return R.drawable.shower3;
        else if(description.equals(context.getString(R.string.heavyShowerRainAndDrizzle)))
            return R.drawable.shower3;
        else if(description.equals(context.getString(R.string.showerDrizzle)))
            return R.drawable.shower3;

        else if(description.equals(context.getString(R.string.lightRain)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.moderateRain)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.heavyIntensityRain)))
            return R.drawable.shower3;
        else if(description.equals(context.getString(R.string.veryHeavyRain)))
            return R.drawable.shower3;
        else if(description.equals(context.getString(R.string.extremeRain)))
            return R.drawable.shower3;
        else if(description.equals(context.getString(R.string.freezingRain)))
            return R.drawable.hail;
        else if(description.equals(context.getString(R.string.lightIntensityShowerRain)))
            return R.drawable.light_rain;
        else if(description.equals(context.getString(R.string.showerRain)))
            return R.drawable.shower3;
        else if(description.equals(context.getString(R.string.heavyIntensityShowerRain)))
            return R.drawable.shower3;
        else if(description.equals(context.getString(R.string.raggedShowerRain)))
            return R.drawable.shower3;

        else if(description.equals(context.getString(R.string.lightSnow)))
            return R.drawable.snow4;
        else if(description.equals(context.getString(R.string.snow)))
            return R.drawable.snow4;
        else if(description.equals(context.getString(R.string.heavySnow)))
            return R.drawable.snow5;
        else if(description.equals(context.getString(R.string.sleet)))
            return R.drawable.sleet;
        else if(description.equals(context.getString(R.string.showerSleet)))
            return R.drawable.sleet;
        else if(description.equals(context.getString(R.string.lightRainAndSnow)))
            return R.drawable.snow4;
        else if(description.equals(context.getString(R.string.rainAndSnow)))
            return R.drawable.sleet;
        else if(description.equals(context.getString(R.string.lightShowerSnow)))
            return R.drawable.sleet;
        else if(description.equals(context.getString(R.string.showerSnow)))
            return R.drawable.sleet;
        else if(description.equals(context.getString(R.string.heavyShowerSnow)))
            return R.drawable.sleet;

        else if(description.equals(context.getString(R.string.mist)))
            return R.drawable.mist;
        else if(description.equals(context.getString(R.string.smoke)))
            return R.drawable.fog;
        else if(description.equals(context.getString(R.string.haze)))
            return R.drawable.fog;
        else if(description.equals(context.getString(R.string.sandDustWhirls)))
            return R.drawable.fog;
        else if(description.equals(context.getString(R.string.fog)))
            return R.drawable.fog;
        else if(description.equals(context.getString(R.string.sand)))
            return R.drawable.fog;
        else if(description.equals(context.getString(R.string.dust)))
            return R.drawable.fog;
        else if(description.equals(context.getString(R.string.volcanicAsh)))
            return R.drawable.fog;
        else if(description.equals(context.getString(R.string.squalls)))
            return R.drawable.squalls;
        else if(description.equals(context.getString(R.string.tornado)))
            return R.drawable.tornado;

        else if(description.equals(context.getString(R.string.clearSky)))
            return R.drawable.sunny;
        else if(description.equals(context.getString(R.string.fewClouds)))
            return R.drawable.cloudy1;
        else if(description.equals(context.getString(R.string.scatteredClouds)))
            return R.drawable.cloudy2;
        else if(description.equals(context.getString(R.string.brokenClouds)))
            return R.drawable.cloudy5;
        else if(description.equals(context.getString(R.string.overcastClouds)))
            return R.drawable.overcast;
        else if(description.equals(context.getString(R.string.skyIsClear)))
            return R.drawable.sunny;
        else
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

    public static final String tempC = (char) 0x00B0 + "C";
    public static final String tempK = (char) 0x00B0 + "K";
    public static final String tempF = (char) 0x00B0 + "F";

    public static final String pressATM = "atm";
    public static final String pressHPA = "hPa";
    public static final String pressMMHG = "mmHg";


    public static final String spTemperature = "spTemperature";
    public static final String spPressure = "spPressure";
    String temperatureUnit, pressureUnit;

    public void loadTemperatureSetting(Context context){
        SharedPreferences preferences = context.getSharedPreferences(spTemperature, Context.MODE_PRIVATE);
        temperatureUnit = preferences.getString("unit", "");
    }

    public void loadPressureSetting(Context context){
        SharedPreferences preferences = context.getSharedPreferences(spPressure, Context.MODE_PRIVATE);
        pressureUnit = preferences.getString("unit", "");
    }

    String temperatureCalculator(){
        if(temperatureUnit.equals(tempC)){
            return round2decimal(String.valueOf((curTemperature - 273))) + (char) 0x00B0 + "C";
        } else if(temperatureUnit.equals(tempK)){
            return round2decimal(String.valueOf((curTemperature))) + (char) 0x00B0 + "K";
        }
        else if(temperatureUnit.equals(tempF)){
            return round2decimal(String.valueOf((curTemperature - 273) * 1.8 + 32 )) + (char) 0x00B0 + "F";
        } else return round2decimal(String.valueOf((curTemperature - 273))) + (char) 0x00B0 + "C";
    }

    String pressureCalculator(){
        if(pressureUnit.equals(pressATM))
            return round2decimal(String.valueOf(curPressure * 100 * 9.86923267 * 0.000001)) + " atm";
        else if(pressureUnit.equals(pressHPA))
            return String.valueOf(curPressure) + " hPa";
        else if(pressureUnit.equals(pressMMHG))
            return round2decimal(String.valueOf(curPressure * 100 * 0.00750061683)) + " mmHg";
        return String.valueOf(curPressure) + " hPa";
    }

}
