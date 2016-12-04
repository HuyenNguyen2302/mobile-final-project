package com.wpi.cs4518.werideshare.model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mrampiah on 11/18/16.
 */

public class Model {

    public static final String FCM_ROOT = "fcm";
    public static final String CHAT_ROOT = "chats";
    public static final String USER_ROOT = "users";
    public static final String MSG_ROOT = "messages";
    public static final String CONVO_ROOT = "conversations";
    private static final String TAG = "MODEL";

    public static User currentUser;
    public static boolean loggedIn = false;
    private static List<User> users;

    private static final Random index = new Random();
    private static final String[] firstNames = {"John", "David", "Chris", "Papa", "Frank"};
    private static final String[] lastNames = {"Viper", "Stewart", "Sarpong", "Ampiah", "Mould"};

    private static final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference usersRef = firebase.child(USER_ROOT);

    public static List<User> getUsers() {
        if (users == null)
            users = new ArrayList<>();

        return users;
    }

    public static void initDB() {
        Log.w("INIT", "initializing with 5 people");
        firebase.child(USER_ROOT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                Log.w(TAG, "adding user: " + user);
                if (!getUsers().contains(user))
                    getUsers().add(user);
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

        for(int i = 0; i < 5; i++)
            getDummyUser("RD" + System.currentTimeMillis() % 1000000);
    }

    public static User getDummyUser(String id) {
        User user = new User(id, firstNames[index.nextInt(firstNames.length)],
                lastNames[index.nextInt(lastNames.length)]);

        if (!getUsers().contains(user)) {
            writeToDatabase(user);
        }
        return user;
    }

    private static void writeToDatabase(User user) {
        Log.w("WRITE", "writing user to database: " + user);

        usersRef.child(user.getUserId())
                .setValue(user);
    }

    public static void getAllUsers(){
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                Log.w(TAG,  "all users size: " + getUsers().size());
                if(!getUsers().contains(user))
                    getUsers().add(user);
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
    }

    public static void getUser(final String id) {

        final Query userQuery = usersRef.orderByKey().equalTo(id);

        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getUserId().equals(id)) {
                    Log.w(TAG, "user found");
                    Model.setCurrentUser(user);
                    userQuery.removeEventListener(this);
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
    }

    public static void setCurrentUser(User user){
        currentUser = user;
    }
}
