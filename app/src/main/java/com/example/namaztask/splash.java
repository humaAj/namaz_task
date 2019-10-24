package com.example.namaztask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread t = new Thread(){

          public void run(){

              try {
                  sleep(2000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }finally {
                  Intent intent =  new Intent(splash.this, ActivityOne.class);
                  startActivity(intent);
              }
          }

        };t.start();

    }
}
