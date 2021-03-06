package com.wpi.cs4518.werideshare.model;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.wpi.cs4518.werideshare.Main;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrampiah on 11/13/16.
 */

public class  User implements Serializable{

    private static final String TAG = "USER";
    private static final String CHAT_ROOT = "conversations";
    private static final String USER_ROOT = "users";

    public enum UserType {
        Driver, Rider;
    }

    private String userId, firstName, lastName, username, deviceId;
    private UserType userType;
    private Map<String, Chat> chats;

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
        setUpListeners();
    }

    private static String getNewUserId(){
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

    public boolean hasChatWith(String username){
        if(getUsername().equals(username))
            return true; //if it's the current user dont add chat
        for(Chat chat : getChats().values()){
            if(chat.getTitle().contains(username))
                return true;
        }
        return false;
    }

    public void addChat(Chat convo) {
        if (!hasChat(convo))
            chats.containsValue(convo);
    }

    private void setUpListeners(){
        /* required listeners:
         * users root: changes to current user - make edits
         * convo root: changes to user's chats - add/remove
         */

        //convo root listener
        ChildEventListener convoListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addChat(dataSnapshot.getValue(Chat.class));
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
        };

       FirebaseDatabase.getInstance().getReference().child(Main.USER_ROOT).child(getUserId())
                .child(CHAT_ROOT).addChildEventListener(convoListener);
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
//            Log.w(TAG, ex.getMessage());
        }
    }

    @Override
    public String toString(){
        return String.format("Firstname: %s, Lastname: %s\n", firstName, lastName);
    }
}
