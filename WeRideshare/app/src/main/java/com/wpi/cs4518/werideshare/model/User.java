package com.wpi.cs4518.werideshare.model;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrampiah on 11/13/16.
 */

public class  User implements Serializable{

    private static final String TAG = "USER";

    public enum UserType {
        Driver, Rider
    }

    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private UserType userType;
    private Map<String, Chat> chats;
    private Map<String, Schedule> schedules;
    private boolean update = false;

    public User() {
        //required empty constructor for firebase database
    }

    public User(String firstName, String lastName) {
        this(getNewUserId(), firstName, lastName, UserType.Rider);
    }

    public User (String firstName, String lastName, UserType userType){
        this(getNewUserId(), firstName, lastName, userType );
    }

    public User(String userId, String firstName, String lastName, UserType userType) {
        this(userId, firstName, lastName, userType, "ecNVoxT2vBM:APA91bHBrqisoF6_dKu_PzuU21XPtCg4NOV7-pJ1gCfL3vu1afzulWnEql4Ge" +
                "DyVl3Puz85DTOEkVxYBGZq-7EPx6A4uZBbcykuTUGDjkxnrf4fqe9a6p6P2AUVxNq6xrSF0c9ihuPC7");
    }

    public User(String userId, String firstName, String lastName, UserType userType, String deviceId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = firstName.toLowerCase() + "_" + lastName.toLowerCase();
        this.userType = userType;
        //String deviceId1 = deviceId;
    }

    private static String getNewUserId() {
        return "RD" + System.currentTimeMillis() % 1000000;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }


    //------ Chat related functions. Need to revise ----- //

    public Map<String, Chat> getChats() {
        if (chats == null)
            chats = new HashMap<>();

        return chats;
    }

    private boolean hasChat(Chat convo) {
        return getChats() != null && getChats().containsValue(convo);
    }

    public boolean hasChatWith(String username) {
        if (getUsername().equals(username))
            return true; //if it's the current user dont add chat
        for (Chat chat : getChats().values()) {
            if (chat.getTitle().contains(username))
                return true;
        }
        return false;
    }

    public void saveChat(Chat convo) {
        if (!hasChat(convo)) {
            Log.w(TAG, String.format("saving chat: %s\n", convo));
            String key = Model.writeChatToDatabase(convo, this);
            addChat(key, convo);
        }
    }

    public void addChat(String key, Chat convo) {
        if (!hasChat(convo))
            chats.put(key, convo);
    }


    //------ Schdeule related functions. Copy of chat so might need to revise ----- //


    private Map<String, Schedule> getSchedules() {
        if (schedules == null)
            schedules = new HashMap<>();

        return schedules;
    }

    private boolean hasSchedule(Schedule schedule){
        return getSchedules().containsValue(schedule);
    }

    public void addSchedule(String key, Schedule schedule) {
        if (!hasSchedule(schedule))
            schedules.put(key, schedule);
    }



    public boolean equals(Object other) {
        if (!(other instanceof User))
            return false;
        String firstName = ((User) other).getFirstName();
        String lastName = ((User) other).getLastName();
        return this.firstName.equals(firstName) && this.lastName.equals(lastName);
    }


    @Override
    public String toString(){
        return String.format("Firstname: %s, Lastname: %s\n", firstName, lastName);
    }
}
