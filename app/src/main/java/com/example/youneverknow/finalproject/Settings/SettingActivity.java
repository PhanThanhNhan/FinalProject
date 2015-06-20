package com.example.youneverknow.finalproject.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.youneverknow.finalproject.R;



/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class SettingActivity extends FragmentActivity {

    private static final String[] temperatureUnits = new String[] {
            (char) 0x00B0 + "C", (char) 0x00B0 + "K", (char) 0x00B0 + "F"
    };

    private static final String[] pressureUnits = new String[] {
            "atm", "hPa", "mmHg"
    };

    private static final String[] velocityUnits = new String[] {
            "km/h",  "m/s", "mph", "ft/s"
    };

    public static final String tempC = (char) 0x00B0 + "C";
    public static final String tempK = (char) 0x00B0 + "K";
    public static final String tempF = (char) 0x00B0 + "F";

    public static final String pressATM = "atm";
    public static final String pressHPA = "hPa";
    public static final String pressMMHG = "mmHg";

    public static final String veloKMH = "km/h";
    public static final String veloMS = "m/s";
    public static final String veloMPH = "mph";
    public static final String veloFTS = "ft/s";

    public static final String spTemperature = "spTemperature";
    public static final String spPressure = "spPressure";
    public static final String spVelocity = "spVelocity";

    public static String temperatureUnit, pressureUnit, velocityUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner temperatureSpinner = (Spinner) findViewById(R.id.temperatureSpinner);
        ArrayAdapter<String> temperatureAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, temperatureUnits);
        temperatureAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        temperatureSpinner.setAdapter(temperatureAdapter);
        temperatureSpinner.setSelection(getTemperaturePosition());
        temperatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temperatureUnit = temperatureUnits[position];
                SharedPreferences preferences = view.getContext().getSharedPreferences(spTemperature, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("unit", temperatureUnit);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner pressureSpinner = (Spinner) findViewById(R.id.pressureSpinner);
        ArrayAdapter<String> pressureAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, pressureUnits);
        pressureAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        pressureSpinner.setAdapter(pressureAdapter);
        pressureSpinner.setSelection(getPressurePosition());
        pressureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pressureUnit = pressureUnits[position];
                SharedPreferences preferences = view.getContext().getSharedPreferences(spPressure, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("unit", pressureUnit);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner velocitySpinner = (Spinner) findViewById(R.id.velocitySpinner);
        ArrayAdapter<String> velocityAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, velocityUnits);
        velocityAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        velocitySpinner.setAdapter(velocityAdapter);
        velocitySpinner.setSelection(getVelocityPosition());
        velocitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                velocityUnit = velocityUnits[position];
                SharedPreferences preferences = view.getContext().getSharedPreferences(spVelocity, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("unit", velocityUnit);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public int getTemperaturePosition(){

        if(temperatureUnit.equals(tempC))
            return 0;
        else if(temperatureUnit.equals(tempK))
            return 1;
        else if(temperatureUnit.equals(tempF))
            return 2;
        else return 0;
    }

    public int getPressurePosition(){

        if(pressureUnit.equals(pressATM))
            return 0;
        else if(pressureUnit.equals(pressHPA))
            return 1;
        else if(pressureUnit.equals(pressMMHG))
            return 2;
        else return 1;
    }

    public int getVelocityPosition(){

        if(velocityUnit.equals(veloKMH))
            return 0;
        else if(velocityUnit.equals(veloMS))
            return 1;
        else if(velocityUnit.equals(veloMPH))
            return 2;
        else if(velocityUnit.equals(veloFTS))
            return 3;
        else return 1;
    }

}
