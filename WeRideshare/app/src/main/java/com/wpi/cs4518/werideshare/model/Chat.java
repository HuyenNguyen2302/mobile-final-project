package com.wpi.cs4518.werideshare.model;

import java.io.Serializable;

/**
 * Created by mrampiah on 11/18/16.
 */

public class Chat implements Serializable{
    private String id;
    private String user1;
    private String user2;

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

    private String getUser1() {
        return user1;
    }

    private String getUser2() {
        return user2;
    }

    public String getTitle() {
        String title = String.format("Between: %s and %s\n", getUser1(), getUser2());
        return title;
    }


    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Chat && ((Chat) other).getId().equals(id);

    }
}
