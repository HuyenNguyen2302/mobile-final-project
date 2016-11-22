package com.wpi.cs4518.werideshare.model;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.wpi.cs4518.werideshare.model.Model.CONVO_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

/**
 * Created by mrampiah on 11/13/16.
 */

public class User {

    public enum UserType{
        Driver, Rider;
    }

    private String userId, firstName, lastName, username, deviceId;
    private UserType userType;
    private ArrayList<String> conversations;
    private DatabaseReference firebase;

    public User(){
        //required empty constructor for firebase database
    }

    public User(String userId, String firstName, String lastName){
        this(userId, firstName, lastName,
                firstName.toLowerCase() + "_" + lastName.toLowerCase(),  UserType.Rider);
    }

    public User(String userId, String firstName, String lastName, String username, UserType userType){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.userType = userType;
        setupFirebase();
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getUsername(){
        return username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ArrayList<String> getConversations(){
        if(conversations == null)
            conversations = new ArrayList<>();

        return conversations;
    }

    public void addConversation(String convoId){
        getConversations().add(convoId);

        if(firebase != null){
            for(String convo : getConversations())
                firebase
                        .child(convo)
                        .setValue(convo);
        }
    }

    private void setupFirebase(){
         firebase =
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child(USER_ROOT)
                        .child(userId)
                        .child(CONVO_ROOT);
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String convoId = dataSnapshot.getValue(String.class);
                if(!conversations.contains(convoId))
                    conversations.add(convoId);
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

    public boolean equals(Object other){
        if(!(other instanceof User))
            return false;
        String firstName = ((User)other).getFirstName();
        String lastName = ((User)other).getLastName();
        return this.firstName.equals(firstName) && this.lastName.equals(lastName);
    }
}
