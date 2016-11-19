package com.wpi.cs4518.werideshare.layout;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.wpi.cs4518.werideshare.ProfileActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Conversation;
import com.wpi.cs4518.werideshare.model.Message;
import com.wpi.cs4518.werideshare.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    private ArrayList<Message> messages;
    private ArrayList<String> formattedMessages;
    private ListView messageList;
    int selectedConversation = -1;


    public MessagesFragment() {
        // Required empty public constructor
    }

    public ArrayList<String> getFormattedMessages(){

        if(formattedMessages == null)
            formattedMessages = formatMessages();

        return formattedMessages;
    }

    public ArrayList<String> formatMessages(){

        if (formattedMessages == null)
            formattedMessages = new ArrayList<>();

        for (Message message : getMessages()) {
            String sender = message.getSender();
            StringBuilder displayMessage = new StringBuilder();
            //display whose message the current message is
            displayMessage.append(String.format("%s:\n", (sender == null ? "Me" : sender)));
            displayMessage.append(String.format("%s\n\n", message.getMessage()));
            formattedMessages.add(displayMessage.toString());
        }

        return formattedMessages;
    }

    public  ArrayList<Message> getMessages(){
        if(messages == null)
            messages = new ArrayList<>();
        return messages;
    }

    public void setSelectedConversation(int position){
        selectedConversation = position;

        if(Model.user != null && selectedConversation != -1)
            messages = Model.user.getConversation(selectedConversation).getMessages();
        messageList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getFormattedMessages()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(messageList == null)
            messageList = (ListView) getView().findViewById(R.id.messages_view);

        ProfileActivity activity = (ProfileActivity) getContext();
        Log.w("UPDATE", "accessing profile activity from messages fragment");
        setSelectedConversation(activity.getSelectedConversation());
    }


    public void addMessage(String sender, String message){
        try {
            StringBuilder displayMessage = new StringBuilder();
            //display whose message the current message is
            displayMessage.append(String.format("%s:\n", (sender == null ? "Me" : sender)));
            displayMessage.append(String.format("%s\n\n", message));
            getFormattedMessages().add(displayMessage.toString());
            ((BaseAdapter) messageList.getAdapter()).notifyDataSetChanged();
        }catch (NullPointerException ex){
            Log.w("NULL ERROR:", ex.getMessage());
        }
    }

}
