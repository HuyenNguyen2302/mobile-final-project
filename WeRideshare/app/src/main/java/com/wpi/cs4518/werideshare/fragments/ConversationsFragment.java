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
import com.wpi.cs4518.werideshare.model.Conversation;
import com.wpi.cs4518.werideshare.model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.wpi.cs4518.werideshare.model.Model.CHAT_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.CONVO_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.FCM_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends Fragment {
    private final String TAG = "CONVO_FRAG";

    ListView conversationList;
    List<Conversation> conversations;
    ArrayAdapter<Conversation> convoAdapter;
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

    private List<Conversation> getConversations() {
        if (conversations == null)
            conversations = new ArrayList<>();

        return conversations;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearConversations();

        if (chatRef == null)
            setupChatRef();
        chatRef.addChildEventListener(convoListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (chatRef == null)
            setupChatRef();
        chatRef.removeEventListener(convoListener);
    }

    private void setupChatRef() {
        chatRef = FirebaseDatabase.getInstance().getReference()
                .child(USER_ROOT)
                .child(Model.currentUser.getUserId())
                .child(CONVO_ROOT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        convoListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Conversation convo = dataSnapshot.getValue(Conversation.class);
                    Log.w(TAG, "adding convo " + convo.getId());
                    if (Model.currentUser.hasConversation(convo))
                        addConversation(convo);
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

        //set up conversation adapter
        for (Conversation convo : Model.currentUser.getConversations())
            addConversation(convo);

        convoAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, getConversations());

        conversationList = (ListView) getView().findViewById(R.id.conversations_view);
        conversationList.setAdapter(convoAdapter);

        conversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conversation item = (Conversation) adapterView.getItemAtPosition(i);
                ((ProfileActivity) getContext()).displayMessages(item.getId());
            }
        });
    }

    public void clearConversations() {
        conversations = new ArrayList<>();
    }

    public void addConversation(Conversation convo) {
        if (getConversations().contains(convo))
            return;
        getConversations().add(convo);
        convoAdapter.notifyDataSetChanged();
    }

}
