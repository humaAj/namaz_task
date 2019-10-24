package com.example.namaztask;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity  {

    TextView tvPopUp;
    static databaseHelper dbHelper;
    static private double lat,lng;
    static private int id, mID;
    static String name;
    static String Contact;
    Button view;
    String MID;
    String AID;
    String MasjidName,date;



    // modify this code
//    TextView textViewPopUp;
//    static databaseHelper databaseHelper;
//    static private double latitude,longitude;
//    static private int id, masjidId;
//    static String name;
//    static String contact;
//    Button view;
//    String masjidId;
//    String adminId;
//    String masjidName, timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        dbHelper = new databaseHelper(this);

        tvPopUp = findViewById(R.id.tvPopUp);

        Intent intent = getIntent();
       // String MID = intent.getStringExtra(MapsActivity.NAME);
      //  String lngi = intent.getStringExtra(MapsActivity.L);
      /*  String fajar = intent.getStringExtra(MapsActivity.F);
        String zohar = intent.getStringExtra(MapsActivity.Z);
        String asar = intent.getStringExtra(MapsActivity.A);
        String maghrib = intent.getStringExtra(MapsActivity.M);
        String isha = intent.getStringExtra(MapsActivity.I);
        String jumma = intent.getStringExtra(MapsActivity.J);*/

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .4), (int) (height * .3));


        URL url = null;
        try {
            url = new URL("http://192.168.51.112/namaztask/TimingsRetrieve.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JSONArray jsonArray = new JSONArray(bufferedReader.readLine());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject obj1 = jsonArray.getJSONObject(i);
                    final String mid = obj1.getString("mID");
                    final String fajar = obj1.getString("fajar");
                    final String zohar = obj1.getString("zohar");
                    final String asar = obj1.getString("asar");
                    final String maghrib = obj1.getString("maghrib");
                    final String isha = obj1.getString("isha");
                    final String jumma = obj1.getString("jumma");

                    if(mid.equals(MID)){
                        tvPopUp.setText("Fajar " + fajar + "\n" +
                                "Zohar " + zohar + "\n" +
                                "Asar " + asar + "\n" +
                                "Maghrib " + maghrib + "\n" +
                                "Isha " + isha + "\n" +
                                "Jumma" + jumma + "\n");

                    }else{
                        tvPopUp.setText("Timings not available yet");
                    }

                }

            }


         /*   tvPopUp.setText("Fajar " + fajar + "\n" +
                    "Zohar " + zohar + "\n" +
                    "Asar " + asar + "\n" +
                    "Maghrib " + maghrib + "\n" +
                    "Isha " + isha + "\n" +
                    "Jumma" + jumma + "\n");*/

            Button fav = findViewById(R.id.fav);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetch();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        public void fetch(){

            URL url = null;

    try {
        url = new URL("http://192.168.51.112/namaztask/masjidRetrieve.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        JSONArray jsonArray = new JSONArray(bufferedReader.readLine());
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            // heroes[i] = obj.getString("name");

            MID = obj.getString("mID");
            MasjidName = obj.getString("MasjidName");
            Contact = obj.getString("contact");
            String lati = obj.getString("lat");
            String lngi = obj.getString("lng");
            date = obj.getString("date");
            AID = obj.getString("id");
            mID = Integer.parseInt(MID);
            id = Integer.parseInt(AID);
            lat = Double.parseDouble(lati);
            lng = Double.parseDouble(lngi);
    }
        try {
            url = new URL("http://192.168.51.112/namaztask/TimingsRetrieve.php");
            con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            jsonArray = new JSONArray(bufferedReader.readLine());
            for (int j = 0; j < jsonArray.length(); j++) {
               JSONObject obj = jsonArray.getJSONObject(j);

                final String fajar = obj.getString("fajar");
                final String zohar = obj.getString("zohar");
                final String asar = obj.getString("asar");
                final String maghrib = obj.getString("maghrib");
                final String isha = obj.getString("isha");
                final String jumma = obj.getString("jumma");

                boolean isInserted = dbHelper.insertData(id, mID, MasjidName,fajar, zohar, asar,maghrib,isha,jumma, Contact, date);

                if (isInserted == true) {
                    Toast.makeText(getApplicationContext() , "Prayer Room added successfully as Favorite" ,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext() , "Data not inserted " ,Toast.LENGTH_LONG).show();
                }


            }
} catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
}}