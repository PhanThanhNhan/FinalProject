package com.example.youneverknow.finalproject.AutoDetect.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;


import com.example.youneverknow.finalproject.R;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class SuperAwesomeCardFragment extends Fragment{

    private static final String ARG_POSITION = "position";

    @InjectView(R.id.textView)
    TextView textView;
    private int position;

    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        if(position == 0){
            rootView = inflater.inflate(R.layout.fragment_card_today,container,false);
        }
        else{
            rootView = inflater.inflate(R.layout.fragment_card_next6days,container,false);
        }
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        textView.setText("CARD "+position);
        return rootView;
    }

}
