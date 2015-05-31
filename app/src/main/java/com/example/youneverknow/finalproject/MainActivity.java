package com.example.youneverknow.finalproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.youneverknow.finalproject.AutoDetect.AutoDetectActivity;
import com.example.youneverknow.finalproject.ChooseOnMap.ChooseOnMapActivity;
import com.example.youneverknow.finalproject.EnterLocation.EnterLocationActivity;
import com.example.youneverknow.finalproject.Settings.SettingActivity;


public class MainActivity extends FragmentActivity {

    private Button btnMainAutoDetect, btnMainEnterLocation, btnMainChooseOnMap, btnMainSettings;
    private int AUTODETECT_RESULT_CODE = 1;
    private int ENTERLOCATION_RESULT_CODE = 2;
    private int CHOOSEONMAP_RESULT_CODE = 3;
    private int SETTINGS_RESULT_CODE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getControl();
        setButtonClickEvents();
    }

    public void getControl(){
        btnMainAutoDetect = (Button) findViewById(R.id.btnMainAutoDetect);
        btnMainEnterLocation = (Button) findViewById(R.id.btnMainEnterLocation);
        btnMainChooseOnMap = (Button) findViewById(R.id.btnMainChooseOnMap);
        btnMainSettings = (Button) findViewById(R.id.btnMainSettings);
    }

    public void setButtonClickEvents(){
        btnMainAutoDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), AutoDetectActivity.class);
                startActivity(iGo);
            }
        });

        btnMainEnterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), EnterLocationActivity.class);
                startActivity(iGo);
            }
        });

        btnMainChooseOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGo = new Intent(v.getContext(), ChooseOnMapActivity.class);
                startActivity(iGo);
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
}
