package com.example.namaztask;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    static databaseHelper dbHelper;
    View view;
    TextView tv;
    Cursor res;
    StringBuffer sb;
    String text;
    int count = 0;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view =inflater.inflate(R.layout.fragment_user, container, false);
        Button btn1 = (Button) view.findViewById(R.id.btnView);
        Button btn2 = (Button) view.findViewById(R.id.btnFav);
        tv =  (TextView) view.findViewById(R.id.tv1);
        dbHelper = new databaseHelper(view.getContext());

        res = dbHelper.getAlldata();

        if(res.getCount() == 0 ){
            //showMessage("Error" , "nothing found");
            tv.setText("No favourites added");

        }

        else{
            sb =  new StringBuffer();
            while (res.moveToNext()) {
                //  sb.append(res.getString(2)+ "\n");
                text = "FAVOURITES" + "\n" + "Masjid Name : " + res.getString(2) + "\n";
                sb.append("fajar " + res.getString(3) + "\n");
                sb.append("zohar " + res.getString(4) + "\n");
                sb.append("Asar " + res.getString(5) + "\n");
                sb.append("Maghrib " + res.getString(6) + "\n");
                sb.append("Isha " + res.getString(7) + "\n");
            }

            // showMessage("Data", sb.toString());

            SpannableString ss = new SpannableString(text);
            ClickableSpan cs = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if(count==0) {
                        showMessage("info", sb.toString());
                        count++;
                    }
                }
            };
            ss.setSpan(cs, 25, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(ss);
            tv.setMovementMethod(LinkMovementMethod.getInstance());

        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() ,MapsActivity.class);
                startActivity(intent);
            }
        });




        return view;
    }
    public void showMessage(String title, String msg){
      //  Toast.makeText(getActivity(), "Data is  "+msg , Toast.LENGTH_LONG).show();

       tv.append(msg);

    }

}
