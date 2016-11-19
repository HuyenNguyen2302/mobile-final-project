package com.wpi.cs4518.werideshare.model;

/**
 * Created by mrampiah on 11/19/16.
 */

public class Message {
    String sender;
    String message;

    public Message(String sender, String message){
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
