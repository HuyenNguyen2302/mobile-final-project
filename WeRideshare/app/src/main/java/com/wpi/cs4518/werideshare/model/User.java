package com.wpi.cs4518.werideshare.model;

import java.util.ArrayList;

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
    }

    public boolean equals(Object other){
        if(!(other instanceof User))
            return false;
        String firstName = ((User)other).getFirstName();
        String lastName = ((User)other).getLastName();
        return this.firstName.equals(firstName) && this.lastName.equals(lastName);
    }
}
