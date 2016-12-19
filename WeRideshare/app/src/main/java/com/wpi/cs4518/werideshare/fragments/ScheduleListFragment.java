package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wpi.cs4518.werideshare.HomescreenActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Schedule;

import java.util.ArrayList;
import java.util.List;

import static com.wpi.cs4518.werideshare.model.Model.SCHEDULE_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleListFragment extends Fragment {

    private ArrayAdapter<Schedule> scheduleAdapter;
    private List<Schedule> schedules;
    private DatabaseReference scheduleRef;
    private ChildEventListener scheduleListener;

    public ScheduleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_list, container, false);
    }

    /**
     * Get the current user's listed schedules
     * @return
     */
    private List<Schedule> getSchedules() {
        if (schedules == null)
            schedules = new ArrayList<>();

        return schedules;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupScheduleRef();
        setupAdapter();
        scheduleRef.addChildEventListener(scheduleListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        setupScheduleRef();
        scheduleRef.removeEventListener(scheduleListener);
    }

    /**
     * create and register schedule listener if they don't exist
     */
    private void setupScheduleRef(){
//        if(scheduleRef != null && scheduleListener != null)
//            return;

        scheduleRef = FirebaseDatabase.getInstance().getReference()
                .child(USER_ROOT)
                .child(HomescreenActivity.currentUser.getUserId())
                .child(SCHEDULE_ROOT);


        //create listener for conversation root
        scheduleListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    addSchedule(dataSnapshot.getKey(), schedule);
                } catch (DatabaseException ex) {
                    Log.w("ERROR", ex.getMessage());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Schedule schedule = dataSnapshot.getValue(Schedule.class);
                removeSchedule(schedule);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    /**
     * Remove a schedule from this user's list of schedules
     * @param schedule
     */
    private void removeSchedule(Schedule schedule){
        if(getSchedules().contains(schedule))
            getSchedules().remove(schedule);
    }

    private void setupAdapter(){
        getSchedules().clear();
        scheduleAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, getSchedules());
        ListView scheduleList = (ListView) getView().findViewById(R.id.schedules_view);
        scheduleList.setAdapter(scheduleAdapter);

        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Schedule item = (Schedule) adapterView.getItemAtPosition(i);
                ((HomescreenActivity) getContext()).displaySchedule(item.getId());
            }
        });

        Button newScheduleButton = (Button) getView().findViewById(R.id.newScheduleButton);
        newScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomescreenActivity) getContext()).displaySchedule(null);
            }
        });
    }

    private void addSchedule(String key,Schedule schedule) {
        if (!getSchedules().contains(schedule)) {
            HomescreenActivity.currentUser.addSchedule(key, schedule);
            schedules.add(schedule);
            scheduleAdapter.notifyDataSetChanged();
        }
    }

}
