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
        this.username = displayName;
        time = System.currentTimeMillis();
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public void setTime(Long time) { this.time = time; }

    @Override
    public String toString(){
        return String.format("text: %s\nusername: %s\n", text, username);
    }

    public Long getTime(){
        return time;
    }
}
