package com.example.namaztask;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemSelectedListener {

    static GoogleMap myMap;
    static Boolean stepOneDone = false;
    static private double lati;
    static private double lngi;
    static private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    public static final String id = "com.example.namaztask.id";
    private ViewGroup infoWindow;
    Button fav,report;
    TextView tv;
    //private favButton infoButtonListener;
    static  private String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_maps);

        Spinner spinner = findViewById(R.id.mapSpinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Want to see prayerRoom in 1km");
        categories.add("Want to see prayerRoom in 2km");
        categories.add("Want to see prayerRoom in 3km");

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if(!stepOneDone) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mapFragment.getMapAsync(this);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.custom_info_window, null);
        this.tv = infoWindow.findViewById(R.id.snippet);
        this.fav= infoWindow.findViewById(R.id.fav);
        this.report = infoWindow.findViewById(R.id.report);


    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            Toast.makeText(getApplicationContext(), "you need to install google play services", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            lati = location.getLatitude();
            lngi = location.getLongitude();
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            makeText(this, "You need to enable permissions to display location !", LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MapsActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();



                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();


                    }
                }

                break;
        }}


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

      /*  if (item.equals("1km")) {

            myMap.animateCamera(CameraUpdateFactory.zoomBy(2));
        } else if (item.equals("2km")) {

            myMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        }
*/
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        myMap =   googleMap;
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    class GetJSON extends AsyncTask<Void, Void, String> implements GoogleMap.OnInfoWindowClickListener {


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
            }}

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

                URL url = new URL("http://192.168.51.112/namaztask/TimingsRetrieve.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                JSONArray jsonArray1 = new JSONArray(bufferedReader.readLine());
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    String Mid = obj.getString("mID");
                    String name = obj.getString("MasjidName");
                    String Contact = obj.getString("contact");
                    String Lat = obj.getString("lat");
                    String lng = obj.getString("lng");
                    lati = Double.parseDouble(Lat);
                    lngi = Double.parseDouble(lng);
                    String des = obj.getString("description");
                    // String date = obj.getString("date");

                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject obj1 = jsonArray1.getJSONObject(i);
                        final String mid = obj1.getString("mID");
                        final String fajar = obj1.getString("fajar");
                        final String zohar = obj1.getString("zohar");
                        final String asar = obj1.getString("asar");
                        final String maghrib = obj1.getString("maghrib");
                        final String isha = obj1.getString("isha");
                        final String jumma = obj1.getString("jumma");

                        String info = name+ "\n"+
                                "Fajar " + fajar + "\n"
                                + "Zohar " + zohar +"\n"
                                + "Asar " + asar +"\n"
                                + "Maghrib " + maghrib +"\n"
                                + "Isha " + isha + "\n"
                                + "jumma" + jumma;

                        addMarker(myMap,lati,lngi,name,info);
                        //  myMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater()));

                        myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                tv.setText(marker.getTitle());
                                tv.setText(marker.getSnippet());
                                return infoWindow;
                            }
                        });

                        myMap.setOnInfoWindowClickListener(this);

                        Location location1;
                        location1 = new Location("dummy");
                        location1.setLatitude(lati);
                        location1.setLongitude(lngi);


                        double distance;

                  /*  if (location != null) {
                         distance = location.distanceTo(location1);
                        if(item.equals("Want to see prayerRoom in 50m")){*/

                        myMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lati, lngi)));
                        //  myMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                              /*   }

                      else if(item.equals("Want to see prayerRoom in 100m") ){

                                myMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lati,lngi)));
                                myMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                            }

                        else if(item.equals("Want to see prayerRoom in 150m")){

                                myMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lati, lngi)));
                                myMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
                            }*/
                    }

                }}
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //in this method we are fetching the json string
        }

        public void onInfoWindowClick(Marker marker) {
            //Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
        }

        private void addMarker(GoogleMap map, double lat, double lon,
                               String title, String snippet) {
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                    .title(title)
                    .snippet(snippet));
        }

    }}
