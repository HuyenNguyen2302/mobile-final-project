package com.wpi.cs4518.werideshare.model;

import com.google.firebase.database.ServerValue;

import java.util.Map;

/**
 * Created by mrampiah on 11/19/16.
 */

public class Message {
    private String text;
    private String username;
    private Long time;

    public Message() {}

    public Message(String text) {
        this(text, "NO_NAME");
    }

    public Message(String text, String displayName) {
        this.text = text;
        username = displayName;
        time = System.currentTimeMillis();
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public void setTime(Long time) { this.time = time; }

    public Long getTime(){
        return time;
    }
}
