package com.example.namaztask;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class updateNamazTiming extends AppCompatActivity {



    EditText etfajar, etzohar, etasar, etmaghrib, etisha, eJumma;
    Button submit;
    String mID,AdminID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_update_namaz_timing);
      Intent intent = getIntent();
      mID = intent.getStringExtra(BlankFragment.MID);
      AdminID = intent.getStringExtra(BlankFragment.Aid);


      /*  URL url = null;
        try {
            url = new URL("http://192.168.110.14/TimingsRetrieve.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JSONArray jsonArray = new JSONArray(bufferedReader.readLine());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                final String Adminid = obj.getString("AdminID");
                final String fajar = obj.getString("fajar");
                final String zohar = obj.getString("zohar");
                final String asar = obj.getString("asar");
                final String maghrib = obj.getString("maghrib");
                final String isha = obj.getString("isha");
                final String jumma = obj.getString("jumma");

                if(AdminID.equals(Adminid)){

                    etfajar.setText(fajar);
                    etzohar.setText(zohar);
                    etasar.setText(asar);
                    etmaghrib.setText(maghrib);
                    etisha.setText(isha);
                    eJumma.setText(jumma);

                }
        }*/
            etfajar = findViewById(R.id.eFajar);
            etzohar = findViewById(R.id.eZohar);
            etasar = findViewById(R.id.eAsar);
            etmaghrib = findViewById(R.id.eMaghrib);
            etisha = findViewById(R.id.eIsha);
            eJumma = findViewById(R.id.eJumma);
            submit =findViewById(R.id.sub);

     submit.setOnClickListener(new View.OnClickListener() {

            InputStream is = null;

            @Override
            public void onClick(View arg0) {


                String fajar = "" + etfajar.getText().toString();
                String zohar = "" + etzohar.getText().toString();
                String asar = "" + etasar.getText().toString();
                String maghrib = "" + etmaghrib.getText().toString();
                String isha = "" + etisha.getText().toString();
                String jumma = "" + eJumma.getText().toString();

                List<NameValuePair> nameValuePairs = new ArrayList<>(1);

                nameValuePairs.add(new BasicNameValuePair("mID", mID));
                nameValuePairs.add(new BasicNameValuePair("AdminID", AdminID));
                nameValuePairs.add(new BasicNameValuePair("fajar", fajar));
                nameValuePairs.add(new BasicNameValuePair("zohar", zohar));
                nameValuePairs.add(new BasicNameValuePair("asar", asar));
                nameValuePairs.add(new BasicNameValuePair("maghrib", maghrib));
                nameValuePairs.add(new BasicNameValuePair("isha", isha));
                nameValuePairs.add(new BasicNameValuePair("jumma", jumma));

                if(!fajar.isEmpty() || !zohar.isEmpty() || !asar.isEmpty() || !maghrib.isEmpty() || !isha.isEmpty() || !jumma.isEmpty()){
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://192.168.51.112/namaztask/namazTiming.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();
                    String msg = "Data Entered Successfully";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } catch (ClientProtocolException e) {
                    Log.e("ClientProtocol", "Log_tag");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("Log_tag", "IOException");
                    e.printStackTrace();
                }
            }else{
                    Toast.makeText(getApplicationContext(), "please fill all fields" ,Toast.LENGTH_LONG).show();
                }
            }

        });
    }}