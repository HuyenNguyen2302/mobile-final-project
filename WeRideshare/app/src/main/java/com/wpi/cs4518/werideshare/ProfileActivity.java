package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpi.cs4518.werideshare.fragments.ConversationsFragment;
import com.wpi.cs4518.werideshare.fragments.MessagesFragment;
import com.wpi.cs4518.werideshare.fragments.ProfileDetails;
import com.wpi.cs4518.werideshare.model.Chat;
import com.wpi.cs4518.werideshare.model.Message;
import com.wpi.cs4518.werideshare.model.Model;
import com.wpi.cs4518.werideshare.model.User;

import java.util.List;

import static com.wpi.cs4518.werideshare.model.Model.CHAT_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.FCM_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.MSG_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.currentUser;

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

    private String[] navItems = {"Profile", "Messages", "Map", "Sign Out"};
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    public static User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebase = FirebaseDatabase.getInstance().getReference();
        Model.initDB();

        if (getIntent() != null) {
            currentUser = (User) getIntent().getSerializableExtra("user");
            if (currentUser == null)
                getCurrentUser(getIntent().getStringExtra("userId"));

            if(getIntent().getStringExtra("type") != null &&
                    getIntent().getStringExtra("type").equals("private message"))
                displayMessages(getIntent().getStringExtra("chatId"));

        }



        setupNavMenu();
    }

    private void getCurrentUser(String userId) {
        firebase.child(USER_ROOT).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                Log.w(TAG, "Retrieved user: " + currentUser.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupNavMenu() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, navItems));
        // Set the list's click listener
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectNavItem(position);
            }
        });
    }

    private void selectNavItem(int position) {
        if (position >= navItems.length) //make sure we dont access beyond bounds
            return;

        switch (position) {
            case 0: //profile
                break;
            case 1: //messages
                break;
            case 2: //maps
                break;
            case 3://sign out
                signOut();
                break;
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, EmailPasswordActivity.class));
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

    public void onClickMessagesButton(View view) {
        if (conversationsFragment == null)
            conversationsFragment = new ConversationsFragment();

        //setup firebase references
        chatRef = FirebaseDatabase.getInstance().getReference()
                .child(FCM_ROOT)
                .child(CHAT_ROOT);


        //temp: users listener, to create a convo for each and add to this user
        Model.usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null && !currentUser.hasChatWith(user.getUsername())){
                    //create chat and save to this user
                    Chat chat = new Chat(user.getUsername(), currentUser.getUsername());
                    currentUser.saveChat(chat);

                    //change the username associated with this chat and save to the other user
                    //this is to ensure reflexivity
                    user.saveChat(chat);
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
        addFragment(conversationsFragment);
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
    }

    public void onClickSendMessage(View view) {
        EditText messageText = (EditText) findViewById(R.id.message_input);
        Message toSend = new Message(messageText.getText().toString(), currentUser.getUsername());
        messageRef.push().setValue(toSend);
        messageText.setText("");
    }

    public void onClickMapButton(View view) {

    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, fragment); // f1_container is FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
