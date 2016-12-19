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
import com.wpi.cs4518.werideshare.HomescreenActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Chat;

import java.util.ArrayList;
import java.util.List;

import static com.wpi.cs4518.werideshare.model.Model.CONVO_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    private ArrayAdapter<Chat> convoAdapter;
    private List<Chat> chats;
    private DatabaseReference chatRef;
    private ChildEventListener convoListener;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }

    private List<Chat> getConversations() {
        if (HomescreenActivity.currentUser == null)
            chats = new ArrayList<>();

        if (chats == null)
            chats = new ArrayList<>(HomescreenActivity.currentUser.getChats().values());

        return chats;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (chatRef == null)
            setupChatRef();
        setUpAdapter();
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
                .child(HomescreenActivity.currentUser.getUserId())
                .child(CONVO_ROOT);

        //create listener for conversation root
        convoListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    addChat(dataSnapshot.getKey(), chat);
                } catch (DatabaseException ex) {
                    Log.w("ERROR", ex.getMessage());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                removeChat(chat);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void removeChat(Chat chat){
        if(getConversations().contains(chat))
            getConversations().remove(chat);
    }

    private void addChat(String key, Chat chat) {
        if (!chats.contains(chat)) {
            HomescreenActivity.currentUser.addChat(key, chat);
            chats.add(chat);
            convoAdapter.notifyDataSetChanged();
        }
    }

    private void setUpAdapter(){
        getConversations().clear();
        convoAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, getConversations());
        ListView conversationList = (ListView) getView().findViewById(R.id.conversations_view);
        conversationList.setAdapter(convoAdapter);

        conversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chat item = (Chat) adapterView.getItemAtPosition(i);
                ((HomescreenActivity) getContext()).displayMessages(item.getId());
            }
        });
    }

}
