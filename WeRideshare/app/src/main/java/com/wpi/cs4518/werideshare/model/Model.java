package com.wpi.cs4518.werideshare.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public static final String SCHEDULE_ROOT = "schedules";
    private static final String TAG = "MODEL";

    public static User currentUser;
    private static List<User> users;

    private static final Random index = new Random();
//    private static final String[] firstNames = {"John", "David", "Chris", "Papa", "Frank"};
//    private static final String[] lastNames = {"Viper", "Stewart", "Sarpong", "Ampiah", "Mould"};

    private static final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference usersRef = firebase.child(USER_ROOT);
    private static final DatabaseReference carsRef = firebase.child(CARS_ROOT);
    private static List<User> getUsers() {
        if (users == null)
            users = new ArrayList<>();

        return users;
    }

    public static void writeUserToDatabase(User user) {
        Log.w(TAG, "writing user to database: " + user);

        usersRef.child(user.getUserId())
                .setValue(user);
    }

    public static String writeChatToDatabase(Chat chat, User user) {
        Log.w(TAG, "writing chat to database: " + chat);
        DatabaseReference chatRef = usersRef
                .child(user.getUserId())
                .child(CONVO_ROOT);
        String key = chatRef.push().getKey();
        chatRef.child(key).setValue(chat);

        return key;
    }


    public static void writeScheduleToDatabase(Schedule schedule, User user) {
        Log.w(TAG, "writing schedule to database: " + schedule.getId());
        DatabaseReference scheduleRef = usersRef
                .child(user.getUserId())
                .child(SCHEDULE_ROOT)
                .child(schedule.getId());
        scheduleRef.setValue(schedule);
    }

    public static void writeCarToDatabase(Car car) {
        Log.w(TAG, "writing car to database: " + car);

        carsRef.child(car.getUserId())
                .setValue(car);
    }

}
