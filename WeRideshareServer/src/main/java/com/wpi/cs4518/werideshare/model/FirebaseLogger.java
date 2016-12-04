package com.wpi.cs4518.werideshare.model;

import com.google.firebase.database.DatabaseReference;
import com.wpi.cs4518.werideshare.LogEntry;

/**
 * Created by mrampiah on 11/22/16.
 */
public class FirebaseLogger {

    private DatabaseReference logRef;
    private String tag;

    public FirebaseLogger(DatabaseReference firebase, String path) {
        logRef = firebase.child(path);
    }

    public void log(String tag, String message) {
        LogEntry entry = new LogEntry(tag, message);
        logRef.push().setValue(entry);
    }
}
