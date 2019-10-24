package com.example.namaztask;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class abc extends AppCompatActivity {

   // EditText e1, e2, e3, e4;
   // Button s, view;
    databaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc);

        dbHelper = new databaseHelper(this );

      /*  e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        e4 = findViewById(R.id.e4);

        s = findViewById(R.id.s);
        view = findViewById(R.id.view);

        addData();
        viewAll();

    }

    public void viewAll(){

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = dbHelper.getAlldata();

                if(res.getCount() == 0 ){
                    showMessage("Error" , "nothing found");
                    return;
                }
                StringBuffer sb =  new StringBuffer();
                while(res.moveToNext()) {
                    sb.append("id : " + res.getString(0));
                    sb.append("name " + res.getString(1));
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

    public void addData(){
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*    boolean isInserted = dbHelper.insertData(e1.getText().toString(),e2.getText().toString(), e3.getText().toString(),e4.getText().toString());

              //   dbHelper.insertData();
                if(isInserted == true){
                    Toast.makeText(abc.this , "Data inserted Successfully" ,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(abc.this , "Data not inserted " ,Toast.LENGTH_LONG).show();
                }*/
          //  }
      //  });
  //  }*/
}}
