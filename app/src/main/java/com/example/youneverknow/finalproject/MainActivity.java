package com.example.youneverknow.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.dd.CircularProgressButton;
import com.example.youneverknow.finalproject.AsyncTask.getWeatherUsingCoordinate;
import com.example.youneverknow.finalproject.AutoDetect.AutoDetectActivity;
import com.example.youneverknow.finalproject.AutoDetect.getLocation;
import com.example.youneverknow.finalproject.ChooseOnMap.ChooseOnMapActivity;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.DataClass.dataFor10daysNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5days;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysDayNode;
import com.example.youneverknow.finalproject.DataClass.dataFor5daysTimeNode;
import com.example.youneverknow.finalproject.EnterLocation.EnterLocationActivity;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private CircularProgressButton btnMainAutoDetect, btnMainEnterLocation, btnMainChooseOnMap, btnMainSettings;;
    public static boolean isButtonPressed = false;

    public static double curLatitude, curLongitude;
    private final int CHOOSE_ON_MAP_RESULT_CODE = 1;
    private final int ENTER_LOCATION_RESULT_CODE = 2;
    public boolean isLocationGotten = false;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getControl();
        setButtonClickEvents();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        };
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
                        if (isLocationGotten) {
                            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            if (!mWifi.isConnected()) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();
                                btnMainAutoDetect.setProgress(0);
                                btnMainAutoDetect.setIdleText(getResources().getString(R.string.main_buttonRetry));
                                return;
                            }
                            getWeatherUsingCoordinate weatherAsyncTask = new getWeatherUsingCoordinate(MainActivity.this, curLatitude, curLongitude, true);
                            weatherAsyncTask.execute();
                            isButtonPressed = true;
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.GPSLocationNotFound), Toast.LENGTH_LONG).show();
                            btnMainAutoDetect.setProgress(-1);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    btnMainAutoDetect.setProgress(0);
                                    btnMainAutoDetect.setIdleText(getResources().getString(R.string.main_buttonRetry));
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
                isButtonPressed = false;
            }
        });

        btnMainChooseOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), ChooseOnMapActivity.class);
                startActivityForResult(iGo, CHOOSE_ON_MAP_RESULT_CODE);
                isButtonPressed = false;
            }
        });

        btnMainSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Function uncompleted", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.item_navigation_drawer_lastCheck:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                if(loadSaveData(MainActivity.this)){
                                    Intent iGo = new Intent(MainActivity.this, AutoDetectActivity.class);
                                    startActivity(iGo);
                                } else Toast.makeText(getApplicationContext(), getString(R.string.therenoData), Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.item_navigation_drawer_howtouse:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                tutorial();
                                return true;
                            case R.id.item_navigation_drawer_about:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                showAboutDialog(ABOUT_DIALOG);
                                return true;
                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);;
                                drawerLayout.closeDrawer(GravityCompat.START);
                                showAboutDialog(SETTING_DIALOG);
                                return true;
                            case R.id.item_navigation_drawer_exit:
                                finish();
                                return true;
                        }
                        return true;
                    }
                });
    }

    private final String ABOUT_DIALOG = "1";
    private final String SETTING_DIALOG = "3";

    public void showAboutDialog(String type){
        String message = "";
        String title = "Let's weather";
        switch (type){
            case ABOUT_DIALOG:
                message = getString(R.string.appIntroduction);
                break;
            case SETTING_DIALOG:
                title = "Oops sorry!";
                message = "Function coming soon.";
                break;
        }
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonText("OK")
                .show();
    }

    private void tutorial(){

        ViewTarget target = new ViewTarget(R.id.btnMainAutoDetect, this);
        String title = getString(R.string.main_AutoDetect);
        String message = getString(R.string.autoDetectMessage);
        ShowcaseView sv = setupShowcase(target, title, message);
        sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {
                ViewTarget target = new ViewTarget(R.id.btnMainEnterLocation, MainActivity.this);
                String title = getString(R.string.main_EnterLocation);
                String message = getString(R.string.enterLocationMessage);
                ShowcaseView sv = setupShowcase(target, title, message);
                sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        ViewTarget target = new ViewTarget(R.id.btnMainChooseOnMap, MainActivity.this);
                        String title = getString(R.string.main_ChooseOnMap);
                        String message = getString(R.string.chooseOnMapMessage);
                        ShowcaseView sv = setupShowcase(target, title, message);
                        sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                            @Override
                            public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                ViewTarget target = new ViewTarget(R.id.btnMainSettings, MainActivity.this);
                                String title = getString(R.string.main_Settings);
                                String message = getString(R.string.settingsMessage);
                                setupShowcase(target, title, message);
                            }

                            @Override
                            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                            }

                            @Override
                            public void onShowcaseViewShow(ShowcaseView showcaseView) {

                            }
                        });
                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {
                    }
                });
            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {
            }
        });
    }

    private ShowcaseView setupShowcase(ViewTarget target, String title, String message){
            ShowcaseView sv = new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .hideOnTouchOutside()
                    .build();
        return sv;
    }

    public boolean loadSaveData(Activity activity){
        JSONObject tempJsonToday = null, tempJson5Days  = null, tempJson10days = null;

        SharedPreferences preToday = activity.getApplicationContext().getSharedPreferences(getWeatherUsingCoordinate.spToday, Context.MODE_PRIVATE);
        SharedPreferences pre5days = activity.getApplicationContext().getSharedPreferences(getWeatherUsingCoordinate.sp5days, Context.MODE_PRIVATE);
        SharedPreferences pre10days = activity.getApplicationContext().getSharedPreferences(getWeatherUsingCoordinate.sp10days, Context.MODE_PRIVATE);
        try {
            tempJsonToday = new JSONObject(preToday.getString("json", ""));
            tempJson5Days= new JSONObject(pre5days.getString("json", ""));
            tempJson10days = new JSONObject(pre10days.getString("json", ""));
        } catch (JSONException e) {
            return false;
        }
        /* Handle json data 10 days*/
        dataFor10days.data = new dataFor10daysNode[10];
        try {
            dataFor10days.cityName = tempJson10days.getJSONObject("city").getString("name");
            dataFor10days.sunRise = convertTime(tempJsonToday.getJSONObject("sys").getString("sunrise"));
            dataFor10days.sunSet = convertTime(tempJsonToday.getJSONObject("sys").getString("sunset"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 10; i++){
            try {
                dataFor10days.data[i] = new dataFor10daysNode();
                dataFor10days.data[i].temperature = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getString("day"));
                dataFor10days.data[i].icon = tempJson10days.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                dataFor10days.data[i].description = tempJson10days.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                dataFor10days.data[i].pressure = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(i).getString("pressure"));
                dataFor10days.data[i].humidity = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(i).getString("humidity"));
                dataFor10days.data[i].wind = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(i).getString("speed"));

                if(tempJson10days.getJSONArray("list").getJSONObject(i).has("rain"))
                    dataFor10days.data[i].rain = Double.parseDouble(tempJson10days.getJSONArray("list").getJSONObject(i).getString("rain"));
                else
                    dataFor10days.data[i].rain = 0;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

         /* Handle json data 5 days */
        dataFor5days.data = new dataFor5daysDayNode[5];
        int tempJsonPos = 0;
        for (int i = 0; i < 5; i++){
            dataFor5days.data[i] = new dataFor5daysDayNode();
            dataFor5days.data[i].time = new dataFor5daysTimeNode[8];
            if(i == 0){
                try{
                    if(tempJson5Days.getJSONArray("list").length()%8 != 0){
                        for(int j = 0; j < (8 - tempJson5Days.getJSONArray("list").length()%8); j++){
                            dataFor5days.data[i].time[j] = new dataFor5daysTimeNode();
                            dataFor5days.data[i].time[j].temperature = 273;
                            dataFor5days.data[i].time[j].icon = "";
                            dataFor5days.data[i].time[j].description = "";
                            dataFor5days.data[i].time[j].pressure = 0;
                            dataFor5days.data[i].time[j].humidity = 0;
                            dataFor5days.data[i].time[j].wind = 0;
                        }
                        for (int j = (8 - tempJson5Days.getJSONArray("list").length()%8); j < 8; j++){
                            dataFor5days.data[i].time[j] = new dataFor5daysTimeNode();
                            dataFor5days.data[i].time[j].temperature = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("temp"));
                            dataFor5days.data[i].time[j].icon = tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("icon");
                            dataFor5days.data[i].time[j].description = tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("description");
                            dataFor5days.data[i].time[j].pressure = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("pressure"));
                            dataFor5days.data[i].time[j].humidity = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("humidity"));
                            dataFor5days.data[i].time[j].wind = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("wind").getString("speed"));
                            ++tempJsonPos;
                        }
                    } else {
                        for (int j = 0; j < 8; j++){
                            dataFor5days.data[i].time[j] = new dataFor5daysTimeNode();
                            dataFor5days.data[i].time[j].temperature = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("temp"));
                            dataFor5days.data[i].time[j].icon = tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("icon");
                            dataFor5days.data[i].time[j].description = tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("description");
                            dataFor5days.data[i].time[j].pressure = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("pressure"));
                            dataFor5days.data[i].time[j].humidity = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("humidity"));
                            dataFor5days.data[i].time[j].wind = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("wind").getString("speed"));
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
                        dataFor5days.data[i].time[j].temperature = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("temp"));
                        dataFor5days.data[i].time[j].icon = tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("icon");
                        dataFor5days.data[i].time[j].description = tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONArray("weather").getJSONObject(0).getString("description");
                        dataFor5days.data[i].time[j].pressure = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("pressure"));
                        dataFor5days.data[i].time[j].humidity = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("main").getString("humidity"));
                        dataFor5days.data[i].time[j].wind = Double.parseDouble(tempJson5Days.getJSONArray("list").getJSONObject(tempJsonPos).getJSONObject("wind").getString("speed"));
                        ++tempJsonPos;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        modifyDescription();
        return true;
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
                return getString(R.string.thunderStormWithLightRain);
            case "thunderstorm with rain":
                return getString(R.string.thunderStormWithRain);
            case "thunderstorm with heavy rain":
                return getString(R.string.thunderStormWithHeavyRain);
            case "light thunderstorm":
                return getString(R.string.lightThunderStorm);
            case "thunderstorm":
                return getString(R.string.thunderStorm);
            case "heavy thunderstorm":
                return getString(R.string.heavyThunderStorm);
            case "ragged thunderstorm":
                return getString(R.string.raggedThunderStorm);
            case "thunderstorm with light drizzle":
                return getString(R.string.thunderStormWithLightDrizzle);
            case "thunderstorm with drizzle":
                return getString(R.string.thunderStormWithDrizzle);
            case "thunderstorm with heavy drizzle":
                return getString(R.string.thunderStormWithHeavyDrizzle);

            case "light intensity drizzle":
                return getString(R.string.lightIntensityDrizzle);
            case "drizzle":
                return getString(R.string.drizzle);
            case "heavy intensity drizzle":
                return getString(R.string.heavyIntensityDrizzle);
            case "light intensity drizzle rain":
                return getString(R.string.lightIntensityDrizzleRain);
            case "drizzle rain":
                return getString(R.string.drizzleRain);
            case "heavy intensity drizzle rain":
                return getString(R.string.heavyIntensityDrizzleRain);
            case "shower rain and drizzle":
                return getString(R.string.showerRainAndDrizzle);
            case "heavy shower rain and drizzle":
                return getString(R.string.heavyShowerRainAndDrizzle);
            case "shower drizzle":
                return getString(R.string.showerDrizzle);

            case "light rain":
                return getString(R.string.lightRain);
            case "moderate rain":
                return getString(R.string.moderateRain);
            case "heavy intensity rain":
                return getString(R.string.heavyIntensityRain);
            case "very heavy rain":
                return getString(R.string.veryHeavyRain);
            case "extreme rain":
                return getString(R.string.extremeRain);
            case "freezing rain":
                return getString(R.string.freezingRain);
            case "light intensity shower rain":
                return getString(R.string.lightIntensityShowerRain);
            case "shower rain":
                return getString(R.string.showerRain);
            case "heavy intensity shower rain":
                return getString(R.string.heavyIntensityShowerRain);
            case "ragged shower rain":
                return getString(R.string.raggedShowerRain);

            case "light snow":
                return getString(R.string.lightSnow);
            case "snow":
                return getString(R.string.snow);
            case "heavy snow":
                return getString(R.string.heavySnow);
            case "sleet":
                return getString(R.string.sleet);
            case "shower sleet":
                return getString(R.string.showerSleet);
            case "light rain and snow":
                return getString(R.string.lightRainAndSnow);
            case "rain and snow":
                return getString(R.string.rainAndSnow);
            case "light shower snow":
                return getString(R.string.lightShowerSnow);
            case "shower snow":
                return getString(R.string.showerSnow);
            case "heavy shower snow":
                return getString(R.string.heavyShowerSnow);

            case "mist":
                return getString(R.string.mist);
            case "smoke":
                return getString(R.string.smoke);
            case "haze":
                return getString(R.string.haze);
            case "sand, dust whirls":
                return getString(R.string.sandDustWhirls);
            case "fog":
                return getString(R.string.fog);
            case "sand":
                return getString(R.string.sand);
            case "dust":
                return getString(R.string.dust);
            case "volcanic ash":
                return getString(R.string.volcanicAsh);
            case "squalls":
                return getString(R.string.squalls);
            case "tornado":
                return getString(R.string.tornado);

            case "clear sky":
                return getString(R.string.clearSky);
            case "few clouds":
                return getString(R.string.fewClouds);
            case "scattered clouds":
                return getString(R.string.scatteredClouds);
            case "broken clouds":
                return getString(R.string.brokenClouds);
            case "overcast clouds":
                return getString(R.string.overcastClouds);
            case "sky is clear":
                return getString(R.string.skyIsClear);
        }
        return description;
    }

}
