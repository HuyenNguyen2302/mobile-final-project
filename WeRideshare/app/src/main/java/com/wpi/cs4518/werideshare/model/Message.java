package com.wpi.cs4518.werideshare.model;

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

    public String getUsername() { return username; }

    public Long getTime(){
        return time;
    }
}
