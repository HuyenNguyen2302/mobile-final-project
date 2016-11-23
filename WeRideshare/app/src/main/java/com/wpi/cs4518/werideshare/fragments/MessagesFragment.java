package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wpi.cs4518.werideshare.ProfileActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    private ListView messageList;
    private List<Map<String, String>> messages;
    private SimpleAdapter messageAdapter;
    private SimpleDateFormat fmt = new SimpleDateFormat("yy.MM.dd HH:mm z");
    ;


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

        if (messageList == null)
            messageList = (ListView) getView().findViewById(R.id.messages_view);

        messages = new ArrayList<>();
        messageAdapter = new SimpleAdapter(getContext(), messages, android.R.layout.simple_list_item_2,
                new String[]{"message", "meta"}, new int[]{android.R.id.text1, android.R.id.text2});

        messageList.setAdapter(messageAdapter);
    }


    public void addMessage(Message message) {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", message.getText());
        msg.put("meta", fmt.format(new Date(message.getTime())) + " "
                + message.getUsername());
        messages.add(msg);
        messageAdapter.notifyDataSetChanged();
    }

}
