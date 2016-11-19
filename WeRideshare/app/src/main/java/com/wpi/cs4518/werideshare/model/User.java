package com.wpi.cs4518.werideshare.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mrampiah on 11/13/16.
 */

public class User {

    public enum UserType{
        Driver, Rider;
    }

    private int userId;
    private String registrationId, firstName, lastName, email, phone, deviceId;
    private Date dateRegistered;
    private UserType userType;
    private ArrayList<Conversation> conversations;

    public User(String registrationId){
        this(registrationId, null, null, null, null);
    }

    public User(String registrationId, String firstName, String lastName,
                String email, String phone){
        this(registrationId, firstName, lastName, email, phone, UserType.Rider);
    }


    public User(String registrationId, String firstName, String lastName,
                String email, String phone, UserType userType){
        this(registrationId, firstName, lastName, email, phone, userType, new Date(System.currentTimeMillis()) );
    }

    public User(String registrationId, String firstName, String lastName,
                String email, String phone, UserType userType, Date dateRegistered){
        this.registrationId = registrationId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.dateRegistered = dateRegistered;
        createDummyConversations();
    }

    public int getUserId() {
        return userId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ArrayList<Conversation> getConversations(){
        if(conversations == null)
            conversations = new ArrayList<>();

        return conversations;
    }

    private void createDummyConversations(){
        getConversations().add(Conversation.createDummyConversation("Bill"));
        getConversations().add(Conversation.createDummyConversation("Alice"));
        getConversations().add(Conversation.createDummyConversation("Tracy"));
    }

    public Conversation findConversation(String sender){
        for(Conversation convo : getConversations()){
            if(convo.getSender().equals(sender))
                return convo;
        }

        return null;//nothing found
    }

    public Conversation getConversation(int position){
        return position > getConversations().size()? null : conversations.get(position);//nothing found
    }
}
