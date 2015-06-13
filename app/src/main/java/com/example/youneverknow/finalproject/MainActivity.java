package com.example.youneverknow.finalproject;

import android.content.Context;
import android.content.Intent;
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
import com.example.youneverknow.finalproject.EnterLocation.EnterLocationActivity;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;


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
                                Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
                                btnMainAutoDetect.setProgress(0);
                                btnMainAutoDetect.setIdleText("Retry");
                                return;
                            }
                            getWeatherUsingCoordinate weatherAsyncTask = new getWeatherUsingCoordinate(MainActivity.this, curLatitude, curLongitude, true);
                            weatherAsyncTask.execute();
                            isButtonPressed = true;
                        } else {
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
                                if(getWeatherUsingCoordinate.loadSaveData(MainActivity.this)){
                                    Intent iGo = new Intent(MainActivity.this, AutoDetectActivity.class);
                                    startActivity(iGo);
                                } else Toast.makeText(getApplicationContext(), "There's no data", Toast.LENGTH_LONG).show();
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
    private final String HELP_DIALOG = "2";
    private final String SETTING_DIALOG = "3";

    public void showAboutDialog(String type){
        String message = "";
        String title = "Let's weather";
        switch (type){
            case ABOUT_DIALOG:
                message = "Let's weather is a weather forecast application.";
                break;
            case HELP_DIALOG:
                message = "Please look at the tutorial.";
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
        String title = "Auto Detect";
        String message = "Click this button will automatically detect your location. Then search for weather info";
        ShowcaseView sv = setupShowcase(target, title, message);
        sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {
                ViewTarget target = new ViewTarget(R.id.btnMainEnterLocation, MainActivity.this);
                String title = "Enter Location";
                String message = "There 's a listview and edittext. Long tap on list item to search or enter city name on TextField ";
                ShowcaseView sv = setupShowcase(target, title, message);
                sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        ViewTarget target = new ViewTarget(R.id.btnMainChooseOnMap, MainActivity.this);
                        String title = "Choose on map";
                        String message = "Choose a location on our map to see its weather";
                        ShowcaseView sv = setupShowcase(target, title, message);
                        sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                            @Override
                            public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                ViewTarget target = new ViewTarget(R.id.btnMainSettings, MainActivity.this);
                                String title = "Settings";
                                String message = "Setting to make app more easier";
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
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {}

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {}
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
}
