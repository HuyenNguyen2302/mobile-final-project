package com.werideshare.server;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mrampiah on 11/20/16.
 */
public class MessageServlet extends HttpServlet {
    private static final String MSG_ROOT = "messages";
    private DatabaseReference firebase;

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

// [START replyToRequest]
    /*
     * Receive a request from an Android client and reply back its inbox ID.
     * Using a transaction ensures that only a single Servlet instance replies
     * to the client. This lets the client knows to which Servlet instance
     * to send consecutive user event logs.
     */
        firebase.child(MSG_ROOT).addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot snapshot, String prevKey) {
                firebase.child(IBX + "/" + snapshot.getValue()).runTransaction(new Transaction.Handler() {
                    public Transaction.Result doTransaction(MutableData currentData) {
                        // The only first Servlet instance will write
                        // its ID to the client inbox.
                        if (currentData.getValue() == null) {
                            currentData.setValue(inbox);
                        }
                        return Transaction.success(currentData);
                    }

                    public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                    }
                });
                firebase.child(REQLOG).removeValue();
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
}
