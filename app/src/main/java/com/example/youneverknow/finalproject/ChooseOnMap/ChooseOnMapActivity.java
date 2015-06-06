package com.example.youneverknow.finalproject.ChooseOnMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.youneverknow.finalproject.AsyncTask.getWeatherUsingCoordinate;
import com.example.youneverknow.finalproject.AutoDetect.AutoDetectActivity;
import com.example.youneverknow.finalproject.AutoDetect.getLocation;
import com.example.youneverknow.finalproject.MainActivity;
import com.example.youneverknow.finalproject.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class ChooseOnMapActivity extends FragmentActivity {

    private Marker marker;
    public GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseonmap);

        googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.googleMap )).getMap();
        ImageButton ibtnChooseOnMapGPS = (ImageButton) findViewById(R.id.ibtnChooseOnMapGPS);
        final ImageButton ibtnChooseOnMapOK = (ImageButton) findViewById(R.id.ibtnChooseOnMapOK);
        ibtnChooseOnMapOK.setVisibility(View.INVISIBLE);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng));
                ibtnChooseOnMapOK.setVisibility(View.VISIBLE);
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.clear();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng));

                MainActivity.curLatitude = marker.getPosition().latitude;
                MainActivity.curLongitude = marker.getPosition().longitude;

                Intent res = new Intent();
                setResult(Activity.RESULT_OK, res);
                finish();
            }
        });

        ibtnChooseOnMapGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryGettingLocation();
                ibtnChooseOnMapOK.setVisibility(View.VISIBLE);
            }
        });

        ibtnChooseOnMapOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.curLatitude = marker.getPosition().latitude;
                MainActivity.curLongitude = marker.getPosition().longitude;

                Intent res = new Intent();
                setResult(Activity.RESULT_OK, res);
                finish();
            }
        });

    }

    public void tryGettingLocation(){
        getLocation gps = new getLocation(this);
        if(gps.canGetLocation()){
            /* Still getting */
            if(gps.getLatitude() == 0 && gps.getLongitude() == 0){
                Toast.makeText(getApplicationContext(), "GPS Location temporary not found", Toast.LENGTH_LONG).show();
                return;
            }
            LatLng myCoordinates = new LatLng(gps.getLatitude(), gps.getLongitude());
            CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 12);
            googleMap.clear();
            marker = googleMap.addMarker(new MarkerOptions().position(myCoordinates));
            googleMap.animateCamera(myLocation);
        } else{
            gps.showSettingsAlert();
        }
    }


}
