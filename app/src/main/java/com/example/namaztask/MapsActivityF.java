package com.example.namaztask;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapsActivityF extends FragmentActivity implements OnMapReadyCallback {

    static GoogleMap mMap;
    static databaseHelper dbHelper;
    static Boolean stepOneDone = false;
    static private double lat,lng;
    static private int id, mID;
    static String name;
    static String Contact;
    Button view;
    String MID;
    String AID;
    String MasjidName,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_maps_f);
        dbHelper = new databaseHelper(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        view = findViewById(R.id.view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = dbHelper.getAlldata();

                if (res.getCount() == 0) {
                    showMessage("Error", "nothing found");
                    return;
                }
                StringBuffer sb = new StringBuffer();
                while (res.moveToNext()) {
                    sb.append("Masjid Name : " + res.getString(2) + "\n");
                    sb.append("fajar " + res.getString(3) + "\n");
                    sb.append("zohar " + res.getString(4) + "\n");
                    sb.append("Asar " + res.getString(5) + "\n");
                    sb.append("Maghrib " + res.getString(6) + "\n");
                    sb.append("Isha " + res.getString(7) + "\n");
                }

                showMessage("Data", sb.toString());
            }
        });


    }
 public void showMessage(String title, String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }

        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        name = "Sydney";*/

        GetJSON getJSON = new GetJSON();
        getJSON.execute();

       // addData();
        //viewAll();

    }


   /* public void addData(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


               // boolean isInserted = dbHelper.insertData(name, -34, 151, 123456789);

                if(isInserted == true){
                    Toast.makeText(getApplicationContext() , "Data inserted Successfully" ,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext() , "Data not inserted " ,Toast.LENGTH_LONG).show();
                }
               //dbHelper.insertData("lalazar masjid", -34, 151,12343545);
                return true;
            }
        });
   }*/

    class GetJSON extends AsyncTask<Void, Void, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("Show.Java", "MESSAGE"+ s);
            try {

                stepOneDone = true;
                JSONArray jsonArray = new JSONArray(s);
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


                    URL url = new URL("http://192.168.51.112/namaztask/TimingsRetrieve.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    jsonArray = new JSONArray(bufferedReader.readLine());
                    for (int j = 0; j < jsonArray.length(); j++) {
                       obj = jsonArray.getJSONObject(i);

                        final String fajar = obj.getString("fajar");
                        final String zohar = obj.getString("zohar");
                        final String asar = obj.getString("asar");
                        final String maghrib = obj.getString("maghrib");
                        final String isha = obj.getString("isha");
                        final String jumma = obj.getString("jumma");

                        Toast.makeText(getApplicationContext(), MasjidName,Toast.LENGTH_LONG).show();
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title(MasjidName)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                       // mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {


                           boolean isInserted = dbHelper.insertData(id, mID, MasjidName,fajar, zohar, asar,maghrib,isha,jumma, Contact, date);

                            if (isInserted == true) {
                                  Toast.makeText(getApplicationContext() , "Data inserted Successfully" ,Toast.LENGTH_LONG).show();
                            } else {
                                 Toast.makeText(getApplicationContext() , "Data not inserted " ,Toast.LENGTH_LONG).show();
                            }
                            //dbHelper.insertData("lalazar masjid", -34, 151,12343545);
                            return true;
                        }
                    });
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }}

            //in this method we are fetching the json string
        @Override
        protected String doInBackground(Void... voids) {

            try {

                URL url = new URL("http://192.168.51.112/namaztask/masjidRetrieve.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                return bufferedReader.readLine();
            } catch (Exception e) {
                Log.e("JSON", e.getMessage());
                return null;
            }}}

}
