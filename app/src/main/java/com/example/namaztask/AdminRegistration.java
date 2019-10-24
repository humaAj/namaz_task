package com.example.namaztask;

import android.app.Dialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class AdminRegistration extends AppCompatActivity {

    EditText ename,eNo;
    String Name, mobileNo;
    public static final String Admin="com.example.namaztask.Admin";
    public static final String mob = "com.example.namaztask.mob";
    private static final String TAG = "addPrayerRoom";

    private static final int ERROR_DIALOG_REQUEST = 9001;


    Button btnLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        setContentView(R.layout.activity_admin_registration);
        ename = findViewById(R.id.ename);
        eNo = findViewById(R.id.eNo);

       btnLoc = findViewById(R.id.btnLoc);


        btnLoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {



                Name = "" + ename.getText().toString();
                mobileNo = ""+eNo.getText().toString();

                if(!Name.isEmpty() || !mobileNo.isEmpty()){

                    if(isServicesOK()) {
                        Intent intent = new Intent(AdminRegistration.this, AdminRegistrationMap.class);
                        intent.putExtra(Admin, Name);
                        intent.putExtra(mob, mobileNo);
                        startActivity(intent);
                    }}else{
                    Toast.makeText(getApplicationContext(),"Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AdminRegistration.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AdminRegistration.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }}



