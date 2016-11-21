package com.wpi.cs4518.werideshare.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrampiah on 11/18/16.
 */

public class Conversation {
    private String id;
    private List<Message> messages;

    public Conversation(){
        //required for data snapshot
    }

    public Conversation(String id){
        this(id, new ArrayList<Message>());
    }

    public Conversation(String id, List<Message> messages){
        this.id = id;
        this.messages = messages;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public List<Message> getMessages(){
        return messages;
    }

    @Override
    public String toString(){
        return "From: " + id;
    }

    @Override
    public boolean equals(Object other){
        if (!( other instanceof Conversation) )
            return false;
        return ((Conversation) other).getId().equals(id);
    }
}
