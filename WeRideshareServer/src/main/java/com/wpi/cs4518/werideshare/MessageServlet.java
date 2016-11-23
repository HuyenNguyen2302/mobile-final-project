package com.wpi.cs4518.werideshare;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.wpi.cs4518.werideshare.model.FirebaseLogger;
import com.wpi.cs4518.werideshare.model.Message;
import com.wpi.cs4518.werideshare.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrampiah on 11/20/16.
 */
public class MessageServlet extends HttpServlet {

    private static final String FCM_ROOT = "fcm";
    private static final String MSG_ROOT = "messages";
    public static final String USER_ROOT = "users";
    public static final String CONVO_ROOT = "conversations";
    private static final String ENDPOINT_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String TAG = "SERVLET";
    private static final String TITLE = "WeRideshare";
    private static final String FIREBASE_SERVER_KEY = "key=AIzaSyDa5h-UGxvrRKD0S0o9uOPmUbAPyG7se9Y";


    private FirebaseLogger logger;
    private DatabaseReference firebase, usersRef, messagesRef;
    private List<User> users;


    @Override
    public void init(ServletConfig config) {
        String credential = config.getInitParameter("credential");
        String databaseUrl = config.getInitParameter("databaseUrl");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(config.getServletContext().getResourceAsStream(credential))
                .setDatabaseUrl(databaseUrl)
                .build();
        FirebaseApp.initializeApp(options);
        firebase = FirebaseDatabase.getInstance().getReference();
        logger = new FirebaseLogger(firebase, "server");

        readUsers();
        messagesRef = firebase
                .child(FCM_ROOT)
                .child(MSG_ROOT);

        messagesRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(final DataSnapshot snapshot, String prevKey) {
                final String convoId = snapshot.getKey();
                messagesRef.child(convoId).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        for (User user : getUsers()) {
                            if (user.hasConversation(convoId)) {
                                try {
                                    sendNotification(message, user);
                                } catch (IOException e) {
                                    logger.log(TAG, e.getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
// [END replyToRequest]

            public void onCancelled(DatabaseError error) {
            }

            public void onChildChanged(DataSnapshot snapshot, String prevKey) {
            }

            public void onChildMoved(DataSnapshot snapshot, String prevKey) {
            }

            public void onChildRemoved(DataSnapshot snapshot) {
            }
        });
    }

    private void sendNotification(Message message, User sender) throws IOException {
        // Build apache httpclient POST request
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(ENDPOINT_URL);

        // Create JSON object for downstream data/notification
        JSONObject messageJson = new JSONObject();
        JSONObject outerBaseJsonObj = new JSONObject();
        try {

            // Notification payload has 'title' and 'body' key
            messageJson.put("title", TITLE);
            messageJson.put("message", message.getText());
            messageJson.put("sender", sender.getUsername());

            // This will be used in case of both 'notification' or 'data' payload
            outerBaseJsonObj.put("to", sender.getDeviceId());


            // Set priority of notification. For instant chat setting
            // high will
            // wake device from idle state - HIGH BATTERY DRAIN
            outerBaseJsonObj.put("priority", "normal");

            // Specify required payload key here either 'data' or
            // 'notification'. We can even use both payloads in single
            // message
            outerBaseJsonObj.put("data", messageJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Setup http entity with json data and 'Content-Type' header
        StringEntity requestEntity = new StringEntity(outerBaseJsonObj.toString(), ContentType.APPLICATION_JSON);

        // Setup required Authorization header
        post.setHeader("Authorization", FIREBASE_SERVER_KEY);

        // Pass setup entity to post request here
        post.setEntity(requestEntity);

        // Execute apache http client post response
        HttpResponse fcmResponse = client.execute(post);
        logger.log(TAG, fcmResponse.toString());

    }

    public List<User> getUsers() {
        if (users == null)
            users = new ArrayList<>();

        return users;
    }

    private void readUsers() {
        usersRef = firebase.child(USER_ROOT);
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                user.setupFirebase();
                addUser(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addUser(User user) {
        if (! getUsers().contains(user)) {
            getUsers().add(user);
        }
    }

}
