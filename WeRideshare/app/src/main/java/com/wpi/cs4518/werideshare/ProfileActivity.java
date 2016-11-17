package com.wpi.cs4518.werideshare;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wpi.cs4518.werideshare.layout.MessagesFragment;
import com.wpi.cs4518.werideshare.layout.ProfileDetails;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void onClickMessagesButton(View view){
        MessagesFragment f1 = new MessagesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, f1); // f1_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickProfileButton(View view){
        ProfileDetails f1 = new ProfileDetails();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, f1); // f1_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
