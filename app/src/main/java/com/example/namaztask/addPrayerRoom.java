package com.example.namaztask;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class addPrayerRoom extends AppCompatActivity {

    public static final String Aid="com.example.namaztask.Aid";
    public static final String MID = "com.example.namaztask.MID";

    EditText ename,eNo,des;
    String MasjidName, description, ContactNo;
    Button btnloc;
    String Lat ,Lng;
    String mID;
    double latitude;
    double longitude;
    String masjidName, lat, lng, desc, AdminID,contact,id,MasjidID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_add_prayer_room);

        ename = findViewById(R.id.ename);
        eNo = findViewById(R.id.eNo);
        des = findViewById(R.id.des);
        btnloc = findViewById(R.id.btnLoc);

        Intent intent = getIntent();
        lat = intent.getStringExtra(AdminRegistrationMap.Latitud);
        lng = intent.getStringExtra(AdminRegistrationMap.Longitud);
        id = intent.getStringExtra(AdminRegistrationMap.ID);
        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lng);

        btnloc.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                MasjidName = ""+ename.getText().toString();
                contact = "" + eNo.getText().toString();
                description = "" + des.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                String date = ""+currentTime.toString();

                if(!MasjidName.isEmpty() || !ContactNo.isEmpty() || !description.isEmpty()){

                    try {

                        URL url = new URL("http://192.168.51.112/namaztask/MasjidRetrieve.php");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        JSONArray jsonArray = new JSONArray(bufferedReader.readLine());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            MasjidID = obj.getString("mID");
                            masjidName = obj.getString("MasjidName");
                            ContactNo = obj.getString("contact");
                            Lat = obj.getString("lat");
                            Lng = obj.getString("lng");
                            desc = obj.getString("description");
                            AdminID = obj.getString("id");

                            double lati = Double.parseDouble(Lat);
                            double lngi = Double.parseDouble(Lng);
                            int Mid = Integer.parseInt(MasjidID);

                            // Toast.makeText(getApplicationContext(), name ,Toast.LENGTH_LONG).show();
                            if (!masjidName.equals(MasjidName)&& !ContactNo.equals(contact) && latitude != lati  && lngi!= longitude ) {
                                Log.e("IF", "IF");

                                Mid = Mid+1;
                                lat = Double.toString(lati);
                                lng = Double.toString(lngi);
                                mID = Integer.toString(Mid);


                                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                                nameValuePairs.add(new BasicNameValuePair("mID",mID));
                                nameValuePairs.add(new BasicNameValuePair("MasjidName", MasjidName));
                                nameValuePairs.add(new BasicNameValuePair("contact",contact));
                                nameValuePairs.add(new BasicNameValuePair("lat", lat));
                                nameValuePairs.add(new BasicNameValuePair("lng", lng));
                                nameValuePairs.add(new BasicNameValuePair("description", description));
                                nameValuePairs.add(new BasicNameValuePair("date", date));
                                nameValuePairs.add(new BasicNameValuePair("id", id));

                                try {
                                    HttpClient httpClient = new DefaultHttpClient();
                                    HttpPost httpPost = new HttpPost("http://192.168.51.112/namaztask/masjid.php");
                                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                                    InputStream is = null;

                                    HttpResponse response = httpClient.execute(httpPost);
                                    HttpEntity entity = response.getEntity();

                                    is = entity.getContent();
                                    String msg = "Data Entered Successfully";
                                    Toast.makeText(getApplicationContext(), msg ,Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(addPrayerRoom.this, Main2Activity.class);
                                    intent.putExtra(Aid, id);
                                    intent.putExtra(MID, mID);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(addPrayerRoom.this);
                                builder.setCancelable(true);
                                builder.setMessage("Masjid already registered");
                                builder.show();
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}

               /* Bundle bundle = new Bundle();
                bundle.putString("Aid", "id");
                bundle.putString("MID", mID);
                BlankFragment myfragment = new BlankFragment();
                myfragment.setArguments(bundle);*/


            }
        });

    }}



