package com.wpi.cs4518.werideshare.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mrampiah on 11/18/16.
 */

public class Conversation {
    String sender;
    ArrayList<Message> messages;

    public Conversation(String sender, ArrayList<Message> messages){
        this.sender = sender;
        this.messages = messages;
    }

    public String getSender(){
        return sender;
    }

    @Override
    public String toString(){
        return "From: " + sender;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }

    public static Conversation createDummyConversation(String user){
        ArrayList<Message> msgs = new ArrayList<>();
        msgs.add(new Message("Me", "Hi, can I join your commute?"));
        msgs.add(new Message(user, "Yeah sure, where should I get you?\nHeads up, I'm a neat freak"));
        msgs.add(new Message("Me", "Lol sure, I wont be eating in the car"));
        msgs.add(new Message(user, "Great, I'll add you"));
        msgs.add(new Message("Me", "Awesome!"));

        return new Conversation(user, msgs);
    }

}
