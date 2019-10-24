package com.example.namaztask;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdminRegistrationMap extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    public static final String Latitud = "com.example.namaztask.Latitud";
    public static final String Longitud = "com.example.namaztask.Longitud";
    public static final String ID = "com.example.namaztask.ID";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private String AdminName;
    private String contactNo;
    private String email,password;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Button confirmBtn;
    double latitude,longitude;
    String lat, lng;

    static private double lati;
    static private double lngi;
    String name,Email,pass;
    String Contact, AdminID,id;

    //List<NameValuePair> nameValuePairs;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //   Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration_map);
        confirmBtn = findViewById(R.id.confirmBtn);

        Intent intent = getIntent();
        AdminName = intent.getStringExtra(AdminFragment.Admin);
        email = intent.getStringExtra(AdminFragment.email);
        password = intent.getStringExtra(AdminFragment.pass);
        contactNo = intent.getStringExtra(AdminFragment.mob);
        getLocationPermission();


        confirmBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                try {

                    URL url = new URL("http://192.168.51.112/namaztask/RetrieveAdmin.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    JSONArray jsonArray = new JSONArray(bufferedReader.readLine());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        id = obj.getString("AdminID");
                        name = obj.getString("AdminName");
                        Email = obj.getString("email");
                        pass = obj.getString("password");
                        Contact = obj.getString("contactNo");
                        String Lat = obj.getString("lat");
                        String Lng = obj.getString("lng");
                        lati = Double.parseDouble(Lat);
                        lngi = Double.parseDouble(Lng);
                        int Aid = Integer.parseInt(id);

                       // Toast.makeText(getApplicationContext(), name ,Toast.LENGTH_LONG).show();
                        if (!name.equals(AdminName)&& !email.equals(Email) && !pass.equals(password) && !Contact.equals(contactNo) && lati != latitude && lngi != longitude) {
                            Log.e("IF", "IF");

                            Aid = Aid+1;
                            lat = Double.toString(latitude);
                            lng = Double.toString(longitude);
                            AdminID = Integer.toString(Aid);


                            List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                            nameValuePairs.add(new BasicNameValuePair("AdminID",AdminID));
                            nameValuePairs.add(new BasicNameValuePair("AdminName", AdminName));
                            nameValuePairs.add(new BasicNameValuePair("email",email));
                            nameValuePairs.add(new BasicNameValuePair("password", password));
                            nameValuePairs.add(new BasicNameValuePair("contactNo", contactNo));
                            nameValuePairs.add(new BasicNameValuePair("lat", lat));
                            nameValuePairs.add(new BasicNameValuePair("lng", lng));

                            try {
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost("http://192.168.51.112/namaztask/admin.php");
                                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                                InputStream is = null;

                                HttpResponse response = httpClient.execute(httpPost);
                                HttpEntity entity = response.getEntity();

                                is = entity.getContent();
                                String msg = "Data Entered Successfully";



                                Intent intent = new Intent(AdminRegistrationMap.this, addPrayerRoom.class);
                                intent.putExtra(Latitud , lat );
                                intent.putExtra(Longitud, lng);
                                intent.putExtra(ID, AdminID);
                                Toast.makeText(getApplicationContext(), AdminID, Toast.LENGTH_LONG).show();
                                startActivity(intent);

                            } catch (ClientProtocolException e) {
                                Log.e("ClientProtocol", "Log_tag");
                                e.printStackTrace();
                            } catch (IOException e) {
                                Log.e("Log_tag", "IOException");
                                e.printStackTrace();
                            }


                            // Toast.makeText(getApplicationContext(), "MasjidName is " + ContactNo, Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("IF", "ELSE");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminRegistrationMap.this);
                            builder.setCancelable(true);
                            builder.setMessage("User already registered");
                            builder.show();
                            break;
                        }
                     }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            latitude = currentLocation.getLatitude();
                            longitude = currentLocation.getLongitude();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);



                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(AdminRegistrationMap.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        Toast.makeText(this, "confirm your location, if not correct wait for few seconds" , Toast.LENGTH_LONG).show();


    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(AdminRegistrationMap.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
}



