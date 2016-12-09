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
    public static final String CARS_ROOT = "cars";
    public static final String MSG_ROOT = "messages";
    public static final String CONVO_ROOT = "chats";
    private static final String TAG = "MODEL";

    public static User currentUser;
    public static boolean loggedIn = false;
    private static List<User> users;

    private static final Random index = new Random();
    private static final String[] firstNames = {"John", "David", "Chris", "Papa", "Frank"};
    private static final String[] lastNames = {"Viper", "Stewart", "Sarpong", "Ampiah", "Mould"};

    public static DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference usersRef = firebase.child(USER_ROOT);
    public static DatabaseReference carsRef = firebase.child(CARS_ROOT);
    public static List<User> getUsers() {
        if (users == null)
            users = new ArrayList<>();

        return users;
    }

    public static void initDB() {
        Log.w("INIT", "initializing with 5 people");
        for(int i = 0; i < 5; i++)
            getDummyUser();

        //register listener to read users as they are added
        //and populate memory store of all users #temp
        firebase.child(USER_ROOT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                User user = dataSnapshot.getValue(User.class);
//                Log.w(TAG, "adding user to memory: " + user);
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

    public static User getDummyUser() {
        User user = new User(firstNames[index.nextInt(firstNames.length)],
                lastNames[index.nextInt(lastNames.length)]);

        if (!getUsers().contains(user)) {
            writeUserToDatabase(user);
        }
        return user;
    }

    public static void writeUserToDatabase(User user) {
        Log.w("WRITE", "writing user to database: " + user);

        usersRef.child(user.getUserId())
                .setValue(user);
    }

    public static String writeChatToDatabase(Chat chat, User user) {
        Log.w("WRITE", "writing chat to database: " + chat);
        DatabaseReference chatRef = usersRef
                .child(user.getUserId())
                .child(CONVO_ROOT);
        String key = chatRef.push().getKey();
        chatRef.child(key).setValue(chat);

        return key;
    }

    public static void writeCarToDatabase(Car car) {
        Log.w("WRITE", "writing car to database: " + car);

        carsRef.child(car.getUserId())
                .setValue(car);
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