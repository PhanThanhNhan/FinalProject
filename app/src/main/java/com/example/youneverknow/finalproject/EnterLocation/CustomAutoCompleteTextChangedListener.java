package com.example.youneverknow.finalproject.EnterLocation;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import com.example.youneverknow.finalproject.R;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        EnterLocationActivity enterLocationActivity = ((EnterLocationActivity) context);

        // query the database based on the user input
        enterLocationActivity.item = enterLocationActivity.getItemsFromDb(userInput.toString());

        // update the adapater
        enterLocationActivity.myAdapter.notifyDataSetChanged();
        enterLocationActivity.myAdapter = new ArrayAdapter<String>(enterLocationActivity, R.layout.dropdown_style, enterLocationActivity.item);
        enterLocationActivity.myAutoComplete.setAdapter(enterLocationActivity.myAdapter);

    }

}