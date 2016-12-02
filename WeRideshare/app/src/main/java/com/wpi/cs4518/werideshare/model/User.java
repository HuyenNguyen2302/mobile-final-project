package com.wpi.cs4518.werideshare.model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wpi.cs4518.werideshare.model.Model.CONVO_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.FCM_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

/**
 * Created by mrampiah on 11/13/16.
 */

public class  User {

    private static final String TAG = "USER";

    public enum UserType {
        Driver, Rider;
    }

    private String userId, firstName, lastName, username, deviceId;
    private UserType userType;
    private List<Conversation> conversations;
    private DatabaseReference firebase = FirebaseDatabase
                                                    .getInstance()
                                                    .getReference();
    private DatabaseReference convoRef;

    private boolean update = false;

    public User() {
        //required empty constructor for firebase database
    }

    public User(String userId, String firstName, String lastName) {
        this(userId, firstName, lastName,
                firstName.toLowerCase() + "_" + lastName.toLowerCase(), UserType.Rider);
    }

    public User(String userId, String firstName, String lastName, String username, UserType userType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.userType = userType;
        deviceId = "ecNVoxT2vBM:APA91bHBrqisoF6_dKu_PzuU21XPtCg4NOV7-pJ1gCfL3vu1afzulWnEql4GeDyVl3Puz85DTOEkVxYBGZq-7EPx6A4uZBbcykuTUGDjkxnrf4fqe9a6p6P2AUVxNq6xrSF0c9ihuPC7";
        setupFirebase();
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

    public List<Conversation> getConversations() {
        if (conversations == null)
            conversations = new ArrayList<>();

        return conversations;
    }

    public boolean hasConversation(Conversation convo){
        return getConversations().contains(convo);
    }

    public void addConversation(Conversation convo, User otherUser) {
        //dont add if user already has conversation
        if (hasConversation(convo))
            return;

        saveConversation(convo);
        convo.setTitle(username);

        if (otherUser != null && !otherUser.hasConversation(convo))
            otherUser.addConversation(convo, null);
    }

    private void saveConversation(Conversation convo) {
        if(convoRef == null)
            setupFirebase();
        convoRef.push().setValue(convo);
        Log.w(TAG, String.format("adding %s(other), to %s(this) to %s(convo)",
                convo.getTitle(), username, convo.getId()));
    }

    public void addConversation(Conversation convo) {
        if (!hasConversation(convo))
            getConversations().add(convo);
    }

    public String getConversationId(String user) {
        for (Conversation convo : getConversations()) {
            if (convo.getTitle().equals(user))
                return convo.getId();
        }
        return null;
    }

    private void setupFirebase() {
        convoRef = firebase
                .child(USER_ROOT)
                .child(userId)
                .child(CONVO_ROOT);
        convoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conversation convo = dataSnapshot.getValue(Conversation.class);
                if (!hasConversation(convo))
                    addConversation(convo);
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
