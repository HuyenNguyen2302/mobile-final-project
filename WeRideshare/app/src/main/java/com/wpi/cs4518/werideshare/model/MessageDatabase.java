package com.wpi.cs4518.werideshare.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.wpi.cs4518.werideshare.model.Model.CHAT_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.MSG_ROOT;

/**
 * Created by mrampiah on 11/21/16.
 */

public class MessageDatabase {
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

    }
}
