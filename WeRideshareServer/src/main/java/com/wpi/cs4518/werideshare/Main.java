package com.wpi.cs4518.werideshare;

import com.google.firebase.database.*;
import com.google.firebase.internal.Log;
import com.wpi.cs4518.werideshare.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mrampiah on 11/23/16.
 */
public class Main {

    public static final String FCM_ROOT = "fcm";
    public static final String USER_ROOT = "users";

    static Random index = new Random();
    static String[] firstNames = {"John", "David", "Chris", "Papa", "Frank"};
    static String[] lastNames = {"Viper", "Stewart", "Sarpong", "Ampiah", "Mould"};
    static DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private static List<User> users;

    public static void init(){
        initDB();
    }

    public static void initDB() {
        Log.w("INIT", "initializing with 5 people");
        firebase.child(FCM_ROOT)
                .child(USER_ROOT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if (!users.contains(user))
                    users.add(user);
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

//        while (getUsers().size() < 5)
//            getDummyUser("RD" + System.currentTimeMillis() % 1000000);
    }

    public static List<User> getUsers() {
        if (users == null)
            users = new ArrayList<>();

        return users;
    }
}
