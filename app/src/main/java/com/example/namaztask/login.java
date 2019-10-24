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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends AppCompatActivity {

    EditText User, pass1;
    Button login;
    public static final String Aid="com.example.namaztask.Aid";
    public static final String MID = "com.example.namaztask.MID";

    private String AdminName;
    private String password;
    private String name,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);

        User = findViewById(R.id.user);
        pass1 = findViewById(R.id.pass);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    URL url = new URL("http://192.168.43.114/RetrieveAdmin.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    JSONArray jsonArray = new JSONArray(bufferedReader.readLine());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        // heroes[i] = obj.getString("name");

                        AdminName = ""+ User.getText().toString();
                        password = ""+ pass1.getText().toString();

                        String AdminId = obj.getString("AdminID");
                        name = obj.getString("AdminName");
                        pass = obj.getString("password");

                       // Toast.makeText(getApplicationContext(), AdminName+" "+password , Toast.LENGTH_LONG).show();

                        if (AdminName.equals(name) && password.equals(pass)) {

                            url = new URL("http://192.168.43.114/masjidRetrieve.php");
                            con = (HttpURLConnection) url.openConnection();
                           bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                            jsonArray = new JSONArray(bufferedReader.readLine());
                            for (int j = 0; j < jsonArray.length(); j++) {
                                obj = jsonArray.getJSONObject(i);
                                // heroes[i] = obj.getString("name");

                                String MasjidID = obj.getString("mID");
                                String AdID = obj.getString("id");

                            if(AdminId.equals(AdID)){

                                Intent intent = new Intent(login.this, Main2Activity.class);
                                intent.putExtra(Aid,AdminId);
                                intent.putExtra(MasjidID, MID);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();

                            }



                        }
                      /*  else {
                            Log.e("IF", "ELSE");
                            AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                            builder.setCancelable(true);
                            builder.setMessage("User does not registered");
                            builder.show();
                            break;
                        }*/
                    }
                    if(!AdminName.equals(name) && password.equals(pass)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                    builder.setCancelable(true);
                    builder.setMessage("User does not exist");
                    builder.show();}
                   // break;

                }} catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            });

    }}
