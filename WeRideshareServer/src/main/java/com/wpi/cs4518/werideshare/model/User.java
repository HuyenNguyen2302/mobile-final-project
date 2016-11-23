package com.wpi.cs4518.werideshare.model;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

import static com.wpi.cs4518.werideshare.MessageServlet.CONVO_ROOT;
import static com.wpi.cs4518.werideshare.MessageServlet.USER_ROOT;

/**
 * Created by mrampiah on 11/13/16.
 */

public class User {

    private static final String TAG = "USER";
    public enum UserType {
        Driver, Rider;
    }

    private String userId, firstName, lastName, username, deviceId;
    private UserType userType;
    private Map<String, String> conversations;
    private DatabaseReference firebase;

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

    public Map<String, String> getConversations() {
        if (conversations == null)
            conversations = new HashMap<>();

        return conversations;
    }

    public boolean hasConversation(String convoId){
        return getConversations().containsKey(convoId);
    }

    public void addConversation(String convoId, User otherUser) {
        if(getConversations().containsValue(otherUser.getUsername()))
            return;
        saveConversation(convoId, otherUser.getUsername());
        if (!otherUser.getConversations().containsValue(username)) {
            otherUser.addConversation(convoId, username);
            otherUser.saveConversation(convoId, username);
        }
    }

    private void saveConversation(String convoId, String otherUser){
        firebase.child(convoId).setValue(otherUser);
    }

    public void addConversation(String convoId, String title) {
        if (!getConversations().containsValue(title))
            getConversations().put(convoId, title);
    }

    public String getConversationId(String user){
        for(Map.Entry<String, String> convos : getConversations().entrySet()){
            if(convos.getValue().equals(user))
                return convos.getKey();
        }
        return null;
    }

    public void setupFirebase() {
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
                String convoId = dataSnapshot.getKey();
                String convoTitle = dataSnapshot.getValue(String.class);
                if (!getConversations().containsValue(convoId))
                    addConversation(convoId, convoTitle);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String convoId = dataSnapshot.getKey();
                String convoTitle = dataSnapshot.getValue(String.class);
                if (!getConversations().containsValue(convoTitle))
                    addConversation(convoId, convoTitle);
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
}
