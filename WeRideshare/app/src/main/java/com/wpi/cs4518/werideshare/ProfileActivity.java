package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.wpi.cs4518.werideshare.layout.MessagesFragment;
import com.wpi.cs4518.werideshare.layout.ProfileDetails;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    public static final String SENDER_ID = "530810481145";
    private ProfileDetails profileDetails;
    private MessagesFragment messagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();

        if(intent != null
                && intent.getStringExtra("type") != null
                && intent.getStringExtra("type").equals("private message")){
            ImageButton button = (ImageButton) findViewById(R.id.messages_image_button);
            button.performClick();
            if(messagesFragment != null){
                String sender = intent.getStringExtra("sender");
                String message = intent.getStringExtra("message");
                messagesFragment.addMessage(sender, message);
            }
        }
    }

    public void onClickMessagesButton(View view) {
        System.out.printf("null fragment: %s\n", messagesFragment == null);
        if (messagesFragment == null)
            messagesFragment = new MessagesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, messagesFragment); // f1_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
        Log.w("Log: ", "started messages fragment");
    }

    public void onClickProfileButton(View view) {
        if (profileDetails == null)
            profileDetails = new ProfileDetails();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, profileDetails); // f1_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickSendMessage(View view) {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        String message = "Hello";
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Long.toString(System.currentTimeMillis()))
                .addData("message", message)
                .addData("my_action", FirebaseInstanceId.getInstance().getToken())
                .build());

        if (messagesFragment != null)
            messagesFragment.addMessage(null, message);
    }

    public void addMessage() {

    }
}
