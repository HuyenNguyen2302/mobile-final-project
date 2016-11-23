package com.wpi.cs4518.werideshare.model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static User currentUser;
    private static List<User> users;

    static Random index = new Random();
    static String[] firstNames = {"John", "David", "Chris", "Papa", "Frank"};
    static String[] lastNames = {"Viper", "Stewart", "Sarpong", "Ampiah", "Mould"};
    static DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

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

        while (getUsers().size() < 5)
            getDummyUser("RD" + index.nextInt(1000));
    }

    public static void createDummyUsers() {
        users = new ArrayList<>();
        for (int i = 0; i < firstNames.length; i++) {
            User u = new User(String.valueOf(index.nextInt(5000)), firstNames[index.nextInt(firstNames.length)],
                    lastNames[index.nextInt(lastNames.length)]);
            if (!getUsers().contains(u))
                getUsers().add(u);
        }

        currentUser = getUsers().get(index.nextInt(getUsers().size() - 1));
//        writeToDatabase();
    }

    public static List<User> getUsers() {
        if (users == null)
            users = new ArrayList<>();

        return users;
    }

    public static User getDummyUser(String id) {
        User user = new User(id, firstNames[index.nextInt(firstNames.length)],
                lastNames[index.nextInt(lastNames.length)]);

        if (!getUsers().contains(user)) {
            getUsers().add(user);
            writeToDatabase(user);
        }
        return user;
    }

    private static void writeToDatabase(User user) {
        Log.w("WRITE", "writing user to database: " + user.getUsername());

        firebase.child(USER_ROOT)
                .child(user.getUserId())
                .setValue(user);
    }

    public static User getUser(final String id) {
        DatabaseReference firebase =
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child(USER_ROOT)
                        .child(id);
        final User[] user = {null};
        if (firebase != null) {
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    if (u.getUserId().equals(id))
                        user[0] = u;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return user[0];
    }
}
