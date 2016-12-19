package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpi.cs4518.werideshare.fragments.ChatListFragment;
import com.wpi.cs4518.werideshare.fragments.MessagesFragment;
import com.wpi.cs4518.werideshare.fragments.ProfileFragment;
import com.wpi.cs4518.werideshare.fragments.ScheduleFragment;
import com.wpi.cs4518.werideshare.fragments.ScheduleListFragment;
import com.wpi.cs4518.werideshare.model.Chat;
import com.wpi.cs4518.werideshare.model.User;

import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.usersRef;

public class HomescreenActivity extends BaseActivity {
    private static final String TAG = "PROFILE_ACTIVITY";

    private ProfileFragment profileFragment;
    private MessagesFragment messagesFragment;
    private ChatListFragment chatsFragment;
    private ScheduleListFragment scheduleListFragment;
    private ScheduleFragment scheduleFragment;

    private final String[] navItems = {"Profile", "Messages", "Map", "Sign Out"};

    public static User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);


        if (getIntent() != null) {
            if(getIntent().getStringExtra("type") != null &&
                    getIntent().getStringExtra("type").equals("private message")){
                String userId = getIntent().getStringExtra("receiver");
                getCurrentUser(userId);
                displayMessages(getIntent().getStringExtra("chatId"));

            }else {
                currentUser = (User) getIntent().getSerializableExtra("user");
                Toast.makeText(this, String.format("Current user: %s",
                        currentUser.getUsername()), Toast.LENGTH_SHORT).show();

            }

        }
        setupNavMenu();
    }

    private void setupNavMenu() {
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<>(this,
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
        if (position >= navItems.length) //make sure we don't access beyond bounds
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
        if (profileFragment == null)
            profileFragment = new ProfileFragment();

        addFragment(profileFragment);
    }

    public void onClickMessagesButton(View view) {
        if (chatsFragment == null)
            chatsFragment = new ChatListFragment();

//        //temp: users listener, to create a convo for each and add to this user
        usersRef.orderByChild("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);

                    if (user != null && !currentUser.hasChatWith(user.getUsername())) {
                        //create chat and save to this user
                        Chat chat = new Chat(user.getUsername(), currentUser.getUsername());
                        currentUser.saveChat(chat);

                        //change the username associated with this chat and save to the other user
                        //this is to ensure reflexivity
                        user.saveChat(chat);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addFragment(chatsFragment);
    }
    private void getCurrentUser(final String userId) {
        showProgressDialog();
        try {
            FirebaseDatabase.getInstance().getReference()
                    .child(USER_ROOT)
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            currentUser = dataSnapshot.getValue(User.class);
                            Log.w(TAG, "Retrieved user: " + currentUser.getUsername());
                            hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }catch (Exception e){
            hideProgressDialog();
        }
    }

    public void displayMessages(String chatId) {
        if (messagesFragment == null)
            messagesFragment = new MessagesFragment();

        addFragment(messagesFragment);
        messagesFragment.setChatId(chatId);
    }

    public void displaySchedule(String scheduleId) {
        if (scheduleFragment == null)
            scheduleFragment = new ScheduleFragment();

        addFragment(scheduleFragment);
        if (scheduleId != null)
            scheduleFragment.setScheduleId(scheduleId);
    }

    public void onClickSendMessage(View view) {
        try {
            messagesFragment.sendMessage();
        } catch (NullPointerException ex) {
            Log.w(TAG, ex.getMessage());
        }
    }

    public void onClickMapButton(View view) {
        Intent goToMapFragment = new Intent(HomescreenActivity.this, MapsActivity.class);
        HomescreenActivity.this.startActivity(goToMapFragment);
    }

    public void onClickScheduleButton(View view) {
        if (scheduleListFragment == null)
            scheduleListFragment = new ScheduleListFragment();

        addFragment(scheduleListFragment);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, fragment); // f1_container is FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
