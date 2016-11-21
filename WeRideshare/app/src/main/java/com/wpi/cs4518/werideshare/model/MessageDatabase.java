package com.wpi.cs4518.werideshare.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mrampiah on 11/21/16.
 */

public class MessageDatabase {
    public static final String FCM_ROOT = "fcm";
    public static final String CHAT_ROOT = "chats";
    public static final String MEMBER_ROOT = "members";
    public static final String MSG_ROOT = "messages";

    private static User currentUser;

    private static DatabaseReference firebase;

    public MessageDatabase(DatabaseReference firebase){
        this.firebase = firebase;
        currentUser = Model.currentUser;
    }

    private void setupListeners(){
        firebase.child(CHAT_ROOT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //add listener to each messages channel of current currentUser
        for(String conversationId : currentUser.getConversations()){
            firebase.child(MSG_ROOT).child(conversationId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
