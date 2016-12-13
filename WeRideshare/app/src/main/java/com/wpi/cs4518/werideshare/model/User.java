package com.wpi.cs4518.werideshare.model;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

/**
 * Created by mrampiah on 11/13/16.
 */

public class  User implements Serializable{

    private static final String TAG = "USER";

    public enum UserType {
        Driver, Rider;
    }

    private String userId, firstName, lastName, username, deviceId;
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
        this.deviceId = deviceId;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    //------ Chat related functions. Need to revise ----- //

    public Map<String, Chat> getChats() {
        if (chats == null)
            chats = new HashMap<>();

        return chats;
    }

    public boolean hasChat(Chat convo){
        return getChats().containsValue(convo);
    }

    public boolean hasChat(String convoId){
        for(Chat chat : getChats().values()){
            if(chat.getId().equals(convoId))
                return true;
        }
        return false;
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


    public Map<String, Schedule> getSchedules() {
        if (schedules == null)
            schedules = new HashMap<>();

        return schedules;
    }

    public boolean hasSchedule(Schedule schedule){
        return getSchedules().containsValue(schedule);
    }

    public boolean hasSchedule(String scheduleId){
        for(Schedule schedule : getSchedules().values()){
            if(schedule.getId().equals(scheduleId))
                return true;
        }
        return false;
    }

    public void saveSchedule(Schedule schedule) {
        if (!hasSchedule(schedule)) {
            Log.w(TAG, String.format("saving schedule: %s\n", schedule));
            Model.writeScheduleToDatabase(schedule, this);
            addSchedule(schedule.getId(), schedule);
        }
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


    private void updateUser() {
        try {
            if (!update)
                return;

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(USER_ROOT)
                    .child(userId)
                    .setValue(this);

            update = false;//reset the update variable on each update
        } catch (NullPointerException ex) {//make sure null errors dont cause app to break
            Log.w(TAG, ex.getMessage());
        }
    }

    @Override
    public String toString(){
        return String.format("Firstname: %s, Lastname: %s\n", firstName, lastName);
    }
}
