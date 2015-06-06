package com.example.youneverknow.finalproject.EnterLocation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

import com.example.youneverknow.finalproject.R;

import java.util.List;


/**
 * Created by YouNeverKnow on 5/31/2015.
 */
public class EnterLocationActivity extends FragmentActivity {

    CustomAutoCompleteView myAutoComplete;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;

    // for database operations
    DatabaseHandler databaseH;

    // just to add some initial value
    String[] item = new String[] {"Please search..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterlocation);


        try{
            // instantiate database handler
            databaseH = new DatabaseHandler(EnterLocationActivity.this);
            // put data to database
            insertData();

            myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_style, item);
            myAutoComplete.setAdapter(myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void insertData(){

        databaseH.create( new MyObject("Bien Hoa") );
        databaseH.create( new MyObject("Ben Tre") );
        databaseH.create( new MyObject("Buon Ma Thuot") );
        databaseH.create( new MyObject("Ca Mau") );
        databaseH.create( new MyObject("Da Lat") );
        databaseH.create( new MyObject("Dong Hoi") );
        databaseH.create( new MyObject("Ha Tinh") );
        databaseH.create( new MyObject("Hai Duong") );
        databaseH.create( new MyObject("Hoa Binh") );
        databaseH.create( new MyObject("Hue") );
        databaseH.create( new MyObject("Kon Tum") );
        databaseH.create( new MyObject("Lang Son") );
        databaseH.create( new MyObject("Long Xuyen") );
        databaseH.create( new MyObject("My Tho") );
        databaseH.create( new MyObject("Nam Dinh") );
        databaseH.create( new MyObject("Ninh Binh") );
        databaseH.create( new MyObject("Phan Thiet") );
        databaseH.create( new MyObject("Pleiku") );
        databaseH.create( new MyObject("Quang Ngai") );
        databaseH.create( new MyObject("Quy Nhon") );
        databaseH.create( new MyObject("Rach Gia") );
        databaseH.create( new MyObject("Thanh Hoa") );
        databaseH.create( new MyObject("HoChiMinh City") );
        databaseH.create( new MyObject("Tra Vinh") );
        databaseH.create( new MyObject("Vinh Yen") );
        databaseH.create( new MyObject("Vung Tau") );


    }

    // this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getItemsFromDb(String searchTerm){

        // add items on the array dynamically
        List<MyObject> products = databaseH.read(searchTerm);
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (MyObject record : products) {

            item[x] = record.objectName;
            x++;
        }

        return item;
    }

}
