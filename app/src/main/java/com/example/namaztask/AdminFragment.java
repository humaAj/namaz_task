package com.example.namaztask;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class AdminFragment extends Fragment {

    EditText ename,eNo,ePassword,eEmail;
    String Name, mobileNo,Email, password;
    TextView lgn;
    public static final String Admin="com.example.namaztask.Admin";
    public static final String email = "com.example.namaztask.email";
    public static final String pass = "com.example.namaztask.pass";
    public static final String mob = "com.example.namaztask.mob";
    private static final String TAG = "admin";
    Button btnLoc;
    private static final int ERROR_DIALOG_REQUEST = 9001;


    public AdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final View view =inflater.inflate(R.layout.fragment_admin, container, false);

        ename =(EditText) view.findViewById(R.id.ename);
        eEmail =  (EditText) view.findViewById(R.id.eEmail);
        ePassword =  (EditText) view.findViewById(R.id.ePassword);
        eNo =  (EditText) view.findViewById(R.id.eNo);

        lgn = (TextView) view.findViewById(R.id.login);
        String text = "Already Registered? Login";
        SpannableString ss = new SpannableString(text);
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        };
        ss.setSpan(cs, 20, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        lgn.setText(ss);
        lgn.setMovementMethod(LinkMovementMethod.getInstance());

        btnLoc = (Button)view.findViewById(R.id.btnLoc);


        btnLoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Name = "" + ename.getText().toString();
                Email = ""+eEmail.getText().toString();
                password = ""+ePassword.getText().toString();
                mobileNo = ""+eNo.getText().toString();


                if(!Name.isEmpty() || !mobileNo.isEmpty() || !password.isEmpty() || !Email.isEmpty()){

                    if(isServicesOK()) {
                             Intent intent = new Intent(view.getContext(), AdminRegistrationMap.class);
                             intent.putExtra(Admin, Name);
                             intent.putExtra(email, Email);
                             intent.putExtra(pass, password);
                             intent.putExtra(mob,mobileNo);
                              startActivity(intent);
                    }}else{
                    Toast.makeText(view.getContext(),"Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;

    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}