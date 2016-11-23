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

import com.wpi.cs4518.werideshare.ProfileActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Conversation;
import com.wpi.cs4518.werideshare.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends Fragment {

    ListView conversationList;
    List<String> conversations;
    ArrayAdapter<String> convoAdapter;


    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set up conversation adapter
        conversations = new ArrayList<>();
        conversations.addAll(Model.currentUser.getConversations().values());
        convoAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, conversations);

        conversationList = (ListView) getView().findViewById(R.id.conversations_view);
        conversationList.setAdapter(convoAdapter);

        conversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                ProfileActivity activity = (ProfileActivity) getContext();
                String id = Model.currentUser.getConversationId(item);
                activity.displayMessages(id != null? id : "With: " + item);
            }
        });

        Log.w("CONVO", "conversation fragment created");
    }

    public void clearConversations(){
        conversations = new ArrayList<>();
    }

    public void addConversation(Conversation convo) {
        if (conversations != null && !conversations.contains(convo.getTitle()))
            conversations.add(convo.getTitle());
        convoAdapter.notifyDataSetChanged();
    }

}
