package com.wpi.cs4518.werideshare.model;

/**
 * Created by mrampiah on 11/18/16.
 */

public class Conversation {
    private String id, title;

    public Conversation(){
        //required for data snapshot
    }

    public Conversation(String id, String title){
        this.id = id;
        this.title = title;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return "From: " + getTitle();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Conversation && ((Conversation) other).getId().equals(id);

    }
}
