package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wpi.cs4518.werideshare.HomescreenActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wpi.cs4518.werideshare.model.Model.FCM_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.MSG_ROOT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    private static String TAG = "mgs";
    private ListView messageList;
    private List<Map<String, String>> messages;
    private SimpleAdapter messageAdapter;
    private final SimpleDateFormat fmt = new SimpleDateFormat("yy.MM.dd HH:mm z");
    private ChildEventListener listener;
    private DatabaseReference messageRef;
    private EditText messageText;

    private String chatId;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        removeListener();
    }

    private void removeListener() {
        try {
            messageRef.removeEventListener(listener);
        } catch (NullPointerException ex) {
            //make sure null errors are caught
            Log.w("MSG", ex.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setUp();

    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    private void setUp() {
        messageList = (ListView) getView().findViewById(R.id.messages_view);

        messages = new ArrayList<>();
        messageAdapter = new SimpleAdapter(getContext(), messages, android.R.layout.simple_list_item_2,
                new String[]{"message", "meta"}, new int[]{android.R.id.text1, android.R.id.text2});

        messageList.setAdapter(messageAdapter);
        setUpFirebase();
    }

    public void setUpFirebase() {
        //setup firebase references
        messageRef = FirebaseDatabase.getInstance().getReference()
                .child(FCM_ROOT)
                .child(MSG_ROOT)
                .child(chatId);

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Message message = dataSnapshot.getValue(Message.class);
                    addMessage(message);
                } catch (DatabaseException ex) {
                    Log.w("ERROR", ex.getMessage());
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
        };
        clearMessages();
        messageRef.addChildEventListener(listener);

    }

    public void addMessage(Message message) {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", message.getText());
        msg.put("meta", fmt.format(new Date(message.getTime())) + " "
                + message.getUsername());
        messages.add(msg);
        messageAdapter.notifyDataSetChanged();
    }

    private void clearMessages() {
        if (messages != null)
            messages.clear();
    }


    public void sendMessage() {
        messageText = (EditText) getActivity().findViewById(R.id.message_input);
        Message toSend = new Message(messageText.getText().toString(), HomescreenActivity.currentUser.getUsername());
        messageRef.push().setValue(toSend);
        messageText.setText("");
    }
}
