package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.wpi.cs4518.werideshare.fragments.ConversationsFragment;
import com.wpi.cs4518.werideshare.fragments.MessagesFragment;
import com.wpi.cs4518.werideshare.fragments.ProfileDetails;
import com.wpi.cs4518.werideshare.map.MapsActivity;
import com.wpi.cs4518.werideshare.model.Conversation;
import com.wpi.cs4518.werideshare.model.Message;
import com.wpi.cs4518.werideshare.model.MessageDatabase;
import com.wpi.cs4518.werideshare.model.Model;
import com.wpi.cs4518.werideshare.model.User;

import java.util.List;
import java.util.Random;

import static com.wpi.cs4518.werideshare.model.Model.CHAT_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.FCM_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.MSG_ROOT;

public class ProfileActivity extends AppCompatActivity {
    public static final String SENDER_ID = "530810481145";
    private static final String TAG = "PROFILE_ACTIVITY";

    private ProfileDetails profileDetails;
    private MessagesFragment messagesFragment;
    private ConversationsFragment conversationsFragment;
    private List<String> convoKeys;

    //realtime database fields
    private DatabaseReference firebase;
    private DatabaseReference chatRef;
    private DatabaseReference messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebase = FirebaseDatabase.getInstance().getReference();
        Model.initDB();
    }

    public void onClickMessagesButton(View view) {
        if (conversationsFragment == null)
            conversationsFragment = new ConversationsFragment();
        conversationsFragment.clearConversations();

        //setup firebase references
        chatRef = firebase
                .child(FCM_ROOT)
                .child(CHAT_ROOT);

        createDummyConversations();//add dummy conversations to firebase server
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)           {
                try {
                    String convoId = dataSnapshot.getKey();
                    String convoTitle = dataSnapshot.getValue(String.class);
                    Log.w(TAG, String.format("Convo title: %s\n", convoTitle));
                    if (conversationsFragment != null && Model.currentUser.getConversations().containsValue(convoId))
                        conversationsFragment.addConversation(new Conversation(convoId, convoTitle));
                } catch (DatabaseException ex) {
                    Log.w("ERROR", ex.getMessage());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, conversationsFragment); // f1_container is FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
        Log.w("Log: ", "started conversations fragment");
    }

    public void displayMessages(String convoId) {
        if (messagesFragment == null)
            messagesFragment = new MessagesFragment();

        //setup firebase references
        messageRef = firebase
                .child(FCM_ROOT)
                .child(MSG_ROOT)
                .child(convoId);

        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (messagesFragment != null)
                        messagesFragment.addMessage(message);
                } catch (DatabaseException ex) {
                    Log.w("ERROR", ex.getMessage());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        EditText messageText = (EditText) findViewById(R.id.message_input);
        Message toSend = new Message(messageText.getText().toString(), Model.currentUser.getUsername());
        messageRef.push().setValue(toSend);
        messageText.setText("");
    }

    public void onClickMapButton(View view){

    }

    private void createDummyConversations() {
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            String key = chatRef.push().getKey();
            User randomUser = Model.getUsers().get(random.nextInt(Model.getUsers().size()));
            User randomUser2 = Model.getUsers().get(random.nextInt(Model.getUsers().size()));

            while(randomUser.equals(randomUser2))
                randomUser2 = Model.getUsers().get(random.nextInt(Model.getUsers().size()));

            randomUser.addConversation(key, randomUser2);
        }
    }
}
