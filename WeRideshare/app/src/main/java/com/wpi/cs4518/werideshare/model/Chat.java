package com.wpi.cs4518.werideshare.model;

import java.io.Serializable;

/**
 * Created by mrampiah on 11/18/16.
 */

public class Chat implements Serializable{
    private String id, user1, user2, title;

    public Chat() {
        //required for data snapshot
    }

    public Chat(String user1, String user2) {
        this.id = getNewChatId();
        this.user1 = user1;
        this.user2 = user2;
    }

    private static String getNewChatId() {
        return "CH" + System.currentTimeMillis() % 1000000;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getTitle() {
        title = String.format("Between: %s and %s\n", getUser1(), getUser2());
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }


    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Chat))
            return false;

        return ((Chat) other).getId().equals(id);
    }
}
