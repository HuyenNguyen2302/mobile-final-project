package com.wpi.cs4518.werideshare.model;

import java.util.Date;

/**
 * Created by mrampiah on 11/13/16.
 */

public class User {

    public enum UserType{
        Driver, Rider;
    }

    private int userId;
    private String username, password, firstName, lastName, email, phone;
    private Date dateRegistered;
    private UserType userType;

    public User(String username, String password){
        this(username, password, null, null, null, null);
    }

    public User(String username, String password, String firstName, String lastName,
                String email, String phone){
        this(username, password, firstName, lastName, email, phone, UserType.Rider);
    }


    public User(String username, String password, String firstName, String lastName,
                String email, String phone, UserType userType){
        this(username, password, firstName, lastName, email, phone, userType, new Date(System.currentTimeMillis()) );
    }

    public User(String username, String password, String firstName, String lastName,
                String email, String phone, UserType userType, Date dateRegistered){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.dateRegistered = dateRegistered;
    }

}
