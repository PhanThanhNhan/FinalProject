package com.example.youneverknow.finalproject.EnterLocation;

/**
 * Created by YouNeverKnow on 6/6/2015.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youneverknow.finalproject.R;

import java.util.Arrays;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    public CustomList(Activity context,
                      String[] web) {
        super(context, R.layout.list_item, web);
        this.context = context;
        this.web = web;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);

        ((TextView)rowView.findViewById(R.id.tvEnterLocationCityName)).setText(web[position]);
        ((TextView) rowView.findViewById(R.id.tvEnterLocationCountry)).setText("Vietnam");
        ((ImageView) rowView.findViewById(R.id.ivEntertLocationIcon)).setImageResource(R.drawable.vietname);

        return rowView;
    }
}
