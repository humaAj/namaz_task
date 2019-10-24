package com.example.namaztask;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    View view;
    Button btn;
    public static final String Aid="com.example.namaztask.Aid";
    public static final String MID = "com.example.namaztask.MID";
    String AdminID, AdID;
    String MasjidID,mdID;
    TextView TVtiming;
    String name;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blank, container, false);
        btn = (Button) view.findViewById(R.id.Timings);
        TVtiming = (TextView) view.findViewById(R.id.TVtiming);

         Bundle args = getArguments();
         AdminID = args.getString("aID");
         MasjidID = args.getString("mID");
         AdID = args.getString("AID");
         mdID = args.getString("MID");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),updateNamazTiming.class);
                 intent.putExtra(Aid,AdminID);
                 intent.putExtra(MID, MasjidID);
                 intent.putExtra(Aid,AdID);
                 intent.putExtra(MID, mdID);
                startActivity(intent);
            }
        });



        try {
            URL url = new URL("http://192.168.51.112/namaztask/masjidRetrieve.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JSONArray jsonArray = new JSONArray(bufferedReader.readLine());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String mID = obj.getString("mID");
                name = obj.getString("MasjidName");


                url = new URL("http://192.168.51.112/namaztask/TimingsRetrieve.php");
                con = (HttpURLConnection) url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                jsonArray = new JSONArray(bufferedReader.readLine());
                for (int j = 0; j < jsonArray.length(); j++) {
                   obj = jsonArray.getJSONObject(i);

                    final String Adminid = obj.getString("AdminID");
                    final String fajar = obj.getString("fajar");
                    final String zohar = obj.getString("zohar");
                    final String asar = obj.getString("asar");
                    final String maghrib = obj.getString("maghrib");
                    final String isha = obj.getString("isha");
                    final String jumma = obj.getString("jumma");


                    if( Adminid.equals(AdminID) || Adminid.equals(AdID)) {

                        TVtiming.setText("Masjid Name " + name + "\n"
                                + "Fajar" + fajar + "\n" +
                                "Zohar " + zohar + "\n" +
                                "Asar " + asar + "\n" +
                                "Maghrib " + maghrib + "\n" +
                                "Isha " + isha + "\n" +
                                "Jumma" + jumma + "\n"
                        );

                    }
            }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }}
