package com.example.sebastiaan.sit207project2kana;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static int TIMEOUT = 2000;
    android.os.Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lets boot into a splashscreen before getting to the login screen
        setContentView(R.layout.splashscreen);
        getSupportActionBar().setTitle("Kana App");

        // This is used to delay the new intent, the timeout is set to 2 seconds.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // We open a new intent
                Intent signInIntent = new Intent(MainActivity.this, SignIn.class);
                MainActivity.this.startActivity(signInIntent);
            }
        }, TIMEOUT);
    }
}
