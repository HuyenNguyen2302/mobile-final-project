package com.wpi.cs4518.werideshare.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mrampiah on 11/18/16.
 */

public class Model {
    public static User currentUser;
    private static List<User> users;

    static Random index = new Random();
    static String[] firstNames = {"John", "David", "Chris", "Papa", "Frank"};
    static String[] lastNames = {"Viper", "Stewart", "Sarpong", "Ampiah", "Mould"};
    static DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

    public static void createDummyUsers() {
        users = new ArrayList<>();
        for (int i = 0; i < firstNames.length; i++) {
            User u = new User(String.valueOf(index.nextInt(5000)), firstNames[index.nextInt(firstNames.length)],
                    lastNames[index.nextInt(lastNames.length)]);
            if (!getUsers().contains(u))
                getUsers().add(u);
        }

        currentUser = getUsers().get(index.nextInt(getUsers().size() - 1));
//        writeToDatabase();
    }

    public static List<User> getUsers(){
        if(users == null)
            users = new ArrayList<>();
        return users;
    }

    public static User getDummyUser(String id){
        User user = new User(id, firstNames[index.nextInt(firstNames.length)],
                lastNames[index.nextInt(lastNames.length)]);

        if(!getUsers().contains(user)){
            getUsers().add(user);
            writeToDatabase(user);
        }

        return user;
    }

    private static void writeToDatabase(User user){

        firebase.child("users")
                .child(user.getUserId())
                .setValue(user);
    }

    public static User getUser(final String id){
        DatabaseReference firebase =
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("users")
                        .child(id);
        final User[] user = {null};
        if(firebase != null){
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    if(u.getUserId().equals(id))
                        user[0] = u;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return user[0];
    }
}
