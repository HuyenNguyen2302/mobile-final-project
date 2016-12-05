package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wpi.cs4518.werideshare.ProfileActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wpi.cs4518.werideshare.model.Model.CONVO_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.currentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends Fragment {
    private final String TAG = "CONVO_FRAG";

    ListView conversationList;
    ArrayAdapter<Chat> convoAdapter;
    DatabaseReference chatRef;
    ChildEventListener convoListener;


    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }

    private ArrayList<Chat> getConversations() {
        if(ProfileActivity.currentUser  == null)
            return null;

        return new ArrayList(ProfileActivity.currentUser.getChats().values());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (chatRef == null)
            setupChatRef();
//        chatRef.addChildEventListener(convoListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (chatRef == null)
            setupChatRef();
//        chatRef.removeEventListener(convoListener);
    }

    private void setupChatRef() {
        chatRef = FirebaseDatabase.getInstance().getReference()
                .child(USER_ROOT)
                .child(ProfileActivity.currentUser.getUserId())
                .child(CONVO_ROOT);

        //create listener for conversation root
        convoListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
//                    Chat convo = dataSnapshot.getValue(Chat.class);
//                    addConversation(convo);
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        convoAdapter = new ArrayAdapter<Chat>(getContext(),
                android.R.layout.simple_list_item_1, getConversations());

        conversationList = (ListView) getView().findViewById(R.id.conversations_view);
        conversationList.setAdapter(convoAdapter);

        conversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chat item = (Chat) adapterView.getItemAtPosition(i);
                ((ProfileActivity) getContext()).displayMessages(item.getId());
            }
        });
    }
}
