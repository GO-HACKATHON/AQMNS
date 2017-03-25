package com.example.icol.navast;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by icol on 25/03/2017.
 */
public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MapsActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}