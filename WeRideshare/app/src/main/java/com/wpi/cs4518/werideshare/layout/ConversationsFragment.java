package com.wpi.cs4518.werideshare.layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.wpi.cs4518.werideshare.ProfileActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Conversation;
import com.wpi.cs4518.werideshare.model.Message;
import com.wpi.cs4518.werideshare.model.Model;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends Fragment {
    ArrayList<Conversation> conversations;
    ArrayList<String> formattedConversations;
    ListView conversationList;


    public ConversationsFragment() {
        // Required empty public constructor
    }


    public ArrayList<Conversation> getConversations() {
        if (conversations == null)
            conversations = new ArrayList<>();
        return conversations;
    }

    public ArrayList<String> getFormattedConversations() {
        if (formattedConversations == null)
            formattedConversations = formatConversations();

        return formattedConversations;
    }

    public ArrayList<String> formatConversations() {
        if (formattedConversations == null)
            formattedConversations = new ArrayList<String>();

        for (Conversation conversation : getConversations()) {
            String sender = conversation.getSender();
            StringBuilder displayMessage = new StringBuilder();
            //display whose message the current message is
            displayMessage.append(String.format("From: %s:\n", (sender == null ? "Me" : sender)));
            formattedConversations.add(displayMessage.toString());
        }

        return formattedConversations;
    }

    public void addConversation(String sender) {
        try {
            StringBuilder displayMessage = new StringBuilder();
            //display whose message the current message is
            displayMessage.append(String.format("From: %s\n", (sender == null ? "Me" : sender)));
            getFormattedConversations().add(displayMessage.toString());
            ((BaseAdapter) conversationList.getAdapter()).notifyDataSetChanged();
        } catch (NullPointerException ex) {
            Log.w("NULL ERROR:", ex.getMessage());
        }
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

        if (conversationList == null)
            conversationList = (ListView) getView().findViewById(R.id.conversations_view);

        if (Model.user != null)
            conversations = Model.user.getConversations();
        conversationList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getFormattedConversations()));


        conversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ProfileActivity activity = (ProfileActivity) getContext();
                activity.setSelectedConversation(position);
                activity.displayMessages();
            }
        });
    }

}
