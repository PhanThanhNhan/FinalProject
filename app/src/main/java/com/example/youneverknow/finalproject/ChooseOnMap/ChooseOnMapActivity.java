package com.example.youneverknow.finalproject.ChooseOnMap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.youneverknow.finalproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class ChooseOnMapActivity extends FragmentActivity {

    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseonmap);

        final GoogleMap googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.googleMap )).getMap();
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(location), 1000, null);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.clear();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

    }


}
