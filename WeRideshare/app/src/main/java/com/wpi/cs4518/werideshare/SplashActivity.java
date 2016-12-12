package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mrampiah on 11/10/16.
 *
 * This class creates the intent showing the splash screen, which contains team information
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(SplashActivity.this, EmailPasswordActivity.class);
        startActivity(intent);

        //Activity.class function to end the activity. Make sure it is up for garbage removal
        finish();
    }
}
