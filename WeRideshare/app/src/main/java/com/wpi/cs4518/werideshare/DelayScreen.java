package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mrampiah on 11/10/16.
 */

public class DelayScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        Thread thread = new Thread(){
            @Override
            public void start(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    startActivity(new Intent(DelayScreen.this, LoginActivity.class));
                }
            }
        };

        thread.start();
        finish();
    }
}
