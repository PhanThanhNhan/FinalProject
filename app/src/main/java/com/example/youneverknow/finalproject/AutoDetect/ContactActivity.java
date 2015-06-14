package com.example.youneverknow.finalproject.AutoDetect;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.MainActivity;
import com.example.youneverknow.finalproject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YouNeverKnow on 6/5/2015.
 */
public class ContactActivity extends Activity implements View.OnClickListener, LoaderManager.LoaderCallbacks<List<String>>{

    Map<String, Integer> mapIndex;
    ListView lvContact;
    public static String[] contacts;
    String[] tempContact;
    String[] phoneNum;
    int contactIndex = 0;
    public final int MAX_CONTACTS = 1000;
    private static final int THE_LOADER = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        tempContact = new String[MAX_CONTACTS];
        phoneNum = new String[MAX_CONTACTS];
        fetchContacts();

        contacts = new String[contactIndex];
        for (int i = 0; i < contactIndex; i++)
            contacts[i] = tempContact[i];

        getLoaderManager().initLoader(THE_LOADER, null, this).forceLoad();

        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendSMS(position);
            }
        });
    }

    protected void sendSMS(int position) {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phoneNum[position + 1]);

        String message;
        if(AutoDetectActivity.isCalculated && dataFor10days.data[0].temperature < 293)
            message = getString(R.string.itsAbout) + String.valueOf(AutoDetectActivity.curTemperature - 273) + getString(R.string.gonnaBeCold);
        else if(AutoDetectActivity.isCalculated&& dataFor10days.data[0].temperature < 303)
            message = getString(R.string.itsAbout) + String.valueOf(AutoDetectActivity.curTemperature - 273) + getString(R.string.howAboutPicnic);
        else if(AutoDetectActivity.isCalculated)
            message = getString(R.string.itsAbout) + String.valueOf(AutoDetectActivity.curTemperature - 273) + getString(R.string.gonnaBeHot);
        else message = getString(R.string.longTimeNoSee);

        smsIntent.putExtra("sms_body", message);

        try {
            startActivity(smsIntent);
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this,
                    getString(R.string.sendSMSFail), Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        lvContact.setSelection(mapIndex.get(selectedIndex.getText()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void fetchContacts() {

        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;


        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {
                    tempContact[contactIndex] = name;
                    ++contactIndex;

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phoneNum[contactIndex] = phoneNumber;
                    }

                    phoneCursor.close();

                }
            }

        }
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        SampleLoader loader = new SampleLoader(this);
        return loader;    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> list) {
        final ListView listview = (ListView) findViewById(R.id.lvContact);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        final ListView listview = (ListView) findViewById(R.id.lvContact);
        listview.setAdapter(null);
    }

    private static class SampleLoader extends AsyncTaskLoader<List<String>> {

        public SampleLoader(Context context) {
            super(context);
        }
        @Override
        public List<String> loadInBackground() {
            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < contacts.length; ++i) {
                list.add(contacts[i]);
            }
            return list;
        }
    }

    String round2decimal(String number){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number.length(); i++){
            if(number.charAt(i) == '.'){
                stringBuilder.append(number.charAt(i));
                stringBuilder.append(number.charAt(i + 1));
                if(i + 2 >= number.length())
                    stringBuilder.append("0");
                else
                    stringBuilder.append(number.charAt(i + 2));

                break;
            }
            stringBuilder.append(number.charAt(i));
        }
        return stringBuilder.toString();
    }
}
