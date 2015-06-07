package com.example.youneverknow.finalproject.EnterLocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.List;
import android.widget.Toast;

import com.example.youneverknow.finalproject.MainActivity;
import com.example.youneverknow.finalproject.R;


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

        CustomList adapter = new CustomList(this, cities);
        ListView lvEnterLocationCity = (ListView) findViewById(R.id.lvEnterLocationCountryList);
        lvEnterLocationCity.setAdapter(adapter);
        lvEnterLocationCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myAutoComplete.setText(cities[position]);
            }
        });

        lvEnterLocationCity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                MainActivity.curLatitude = getLat(citiesCoordinate[position]);
                MainActivity.curLongitude = getLon(citiesCoordinate[position]);

                Intent res = new Intent();
                setResult(Activity.RESULT_OK, res);
                finish();
                return true;
            }
        });

        ImageButton ibtnEnterLocation = (ImageButton) findViewById(R.id.ibtnEnterLocationOK);
        ibtnEnterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = 0;
                boolean isFound = false;
                for (int i = 0; i < cities.length; i++){
                    if(cities[i].equals(myAutoComplete.getText().toString())){
                        isFound = true;
                        pos = i;
                        break;
                    }
                }
                if(!isFound){
                    Toast.makeText(getApplicationContext(), "Wrong Input", Toast.LENGTH_LONG).show();
                    myAutoComplete.requestFocus();
                    return;
                }
                MainActivity.curLatitude = getLat(citiesCoordinate[pos]);
                MainActivity.curLongitude = getLon(citiesCoordinate[pos]);

                Intent res = new Intent();
                setResult(Activity.RESULT_OK, res);
                finish();

            }
        });
    }
    // An array of string to create on database
    public static String[] cities = {
            "Bien Hoa",
            "Ben Tre",
            "Buon Ma Thuot",
            "Ca Mau",
            "Can Tho",
            "Cao Bang",
            "Da Lat",
            "Dong Hoi",
            "Ha Tinh",
            "Hai Duong",
            "Hai Phong",
            "Hoa Binh",
            "Hue",
            "Kon Tum",
            "Lang Son",
            "Long Xuyen",
            "My Tho",
            "Nam Dinh",
            "Ninh Binh",
            "Phan Thiet",
            "Pleiku",
            "Quang Ngai",
            "Quy Nhon",
            "Rach Gia",
            "Thanh Hoa",
            "Thanh pho Ho Chi Minh",
            "Tra Vinh",
            "Vinh Phuc",
            "Vung Tau",
            "Yen Bai",
    };

    public static String[] citiesCoordinate = {
            "10.950000-106.816673",
            "10.233330-106.383331",
            "12.666670-108.050003",
            "9.176940-105.150002",
            "10.033330-105.783333",
            "22.666670-106.250000",
            "11.946460-108.441933",
            "17.483330-106.599998",
            "18.333330-105.900002",
            "20.933331-106.316673",
            "20.856110-106.682220",
            "20.813330-105.338333",
            "16.466669-107.599998",
            "14.350000-108.000000",
            "21.833330-106.733330",
            "10.383330-105.416672",
            "10.350000-106.349998",
            "20.416670-106.166672",
            "20.253889-105.974998",
            "10.933330-108.099998",
            "13.983330-108.000000",
            "15.116670-108.800003",
            "13.766670-109.233330",
            "10.016670-105.083328",
            "19.799999-105.766670",
            "10.750000-106.666672",
            "9.934720-106.345284",
            "21.309999-105.596672",
            "10.345990-107.084259",
            "21.700001-104.866669",

    };

    public void insertData(){
            for (int i = 0; i < cities.length; i++){
                databaseH.create( new MyObject(cities[i]) );
            }
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

    public Double getLat(String data){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length(); i++){
            if(data.charAt(i) == '-')
                break;
            stringBuilder.append(data.charAt(i));
        }
        return Double.parseDouble(stringBuilder.toString());
    }

    public Double getLon(String data){
        StringBuilder stringBuilder = new StringBuilder();
        boolean isReach = false;
        for (int i = 0; i < data.length(); i++){
            if(data.charAt(i) != '-' && !isReach){
                continue;
            }
            if(data.charAt(i) == '-'){
                isReach = true;
                continue;
            }
            stringBuilder.append(data.charAt(i));
        }
        return Double.parseDouble(stringBuilder.toString());
    }



}
