package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpi.cs4518.werideshare.HomescreenActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Model;
import com.wpi.cs4518.werideshare.model.Schedule;
import com.wpi.cs4518.werideshare.model.ScheduleTime;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.wpi.cs4518.werideshare.model.Model.SCHEDULE_ROOT;
import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {
    private static String TAG = "SCHEDULE_FRAGMENT";
    String scheduleId;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

    private DatabaseReference scheduleRef;
    private ChildEventListener listener;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (scheduleRef == null)
            init();
    }


    public void setScheduleId(final String scheduleId) {
//        Log.w(TAG, "setting schedule: " + scheduleId);
        this.scheduleId = scheduleId;
        if (scheduleRef == null)
            setUpFirebase();
    }

    private void init() {
//        Log.w(TAG, "initializing schedule fragment");
        setUpFirebase();

        Button saveButton = (Button) getView().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSchedule();
            }
        });
    }

    public void setUpFirebase() {
        //setup firebase references
//        Log.w(TAG, "initializing firebase");
        scheduleRef = FirebaseDatabase.getInstance().getReference()
                .child(USER_ROOT)
                .child(HomescreenActivity.currentUser.getUserId())
                .child(SCHEDULE_ROOT);

        if (scheduleId == null)
            return;

        scheduleRef.child(scheduleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Schedule schedule = dataSnapshot.getValue(Schedule.class);
//                Log.w(TAG, "found schedule: " + schedule);
                loadSchedule(schedule);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadSchedule(Schedule schedule) {
        try {
            //get input fields
            EditText nameText = (EditText) getView().findViewById(R.id.scheduleName);
            EditText departTime = (EditText) getView().findViewById(R.id.departTime);
            EditText returnTime = (EditText) getView().findViewById(R.id.returnTime);

            //get checkboxes
            CheckBox mondayCheckBox = (CheckBox) getView().findViewById(R.id.mondayCheckBox);
            CheckBox tuesdayCheckBox = (CheckBox) getView().findViewById(R.id.tuesdayCheckBox);
            CheckBox wednesdayCheckBox = (CheckBox) getView().findViewById(R.id.wednesdayCheckBox);
            CheckBox thursdayCheckBox = (CheckBox) getView().findViewById(R.id.thursdayCheckBox);
            CheckBox fridayCheckBox = (CheckBox) getView().findViewById(R.id.fridayCheckBox);
            CheckBox sundayCheckBox = (CheckBox) getView().findViewById(R.id.sundayCheckBox);
            CheckBox saturdayCheckBox = (CheckBox) getView().findViewById(R.id.saturdayCheckBox);

            nameText.setText(schedule.getName());
            List<ScheduleTime> times = new ArrayList<>(schedule.getTimes().values());
            Time departValue = new Time(times.get(0).getDepartTimeMillis());
            Time returnValue = new Time(times.get(0).getReturnTimeMillis());
            departTime.setText(simpleDateFormat.format(departValue));
            returnTime.setText(simpleDateFormat.format(returnValue));

            Map timeMap = schedule.getTimes();
            if(timeMap.containsKey(Schedule.DayOfWeek.Monday.toString()))
                mondayCheckBox.setChecked(true);

            if(timeMap.containsKey(Schedule.DayOfWeek.Tuesday.toString()))
                tuesdayCheckBox.setChecked(true);

            if(timeMap.containsKey(Schedule.DayOfWeek.Wednesday.toString()))
                wednesdayCheckBox.setChecked(true);

            if(timeMap.containsKey(Schedule.DayOfWeek.Thursday.toString()))
                thursdayCheckBox.setChecked(true);

            if(timeMap.containsKey(Schedule.DayOfWeek.Friday.toString()))
                fridayCheckBox.setChecked(true);

            if(timeMap.containsKey(Schedule.DayOfWeek.Saturday.toString()))
                saturdayCheckBox.setChecked(true);

            if(timeMap.containsKey(Schedule.DayOfWeek.Sunday.toString()))
                sundayCheckBox.setChecked(true);

        } catch (NullPointerException ex) {

        }

    }

    private void saveSchedule() {
        try {
            Log.w(TAG, "saving schedule...");
            //get input fields
            EditText nameText = (EditText) getView().findViewById(R.id.scheduleName);
            EditText departTime = (EditText) getView().findViewById(R.id.departTime);
            EditText returnTime = (EditText) getView().findViewById(R.id.returnTime);

            //get checkboxes
            CheckBox mondayCheckBox = (CheckBox) getView().findViewById(R.id.mondayCheckBox);
            CheckBox tuesdayCheckBox = (CheckBox) getView().findViewById(R.id.tuesdayCheckBox);
            CheckBox wednesdayCheckBox = (CheckBox) getView().findViewById(R.id.wednesdayCheckBox);
            CheckBox thursdayCheckBox = (CheckBox) getView().findViewById(R.id.thursdayCheckBox);
            CheckBox fridayCheckBox = (CheckBox) getView().findViewById(R.id.fridayCheckBox);
            CheckBox sundayCheckBox = (CheckBox) getView().findViewById(R.id.sundayCheckBox);
            CheckBox saturdayCheckBox = (CheckBox) getView().findViewById(R.id.saturdayCheckBox);

            String[] departTimeValues = departTime.getText().toString().split(":", 2);
            String[] returnTimeValues = returnTime.getText().toString().split(":", 2);

            for (String s : departTimeValues)
                System.out.printf("depart: %s\n", s);


            for (String s : returnTimeValues)
                System.out.printf("return: %s\n", s);

            ScheduleTime times = new ScheduleTime(departTimeValues, returnTimeValues);

            String name = nameText.getText().toString();

            //instantiate schedule and then add days one-by-one below
            scheduleId = scheduleRef.push().getKey();
            System.out.printf("schedule id: %s\n", scheduleId);
            Schedule schedule = new Schedule(scheduleId, name, null, null);


            //add day if checked
            if (mondayCheckBox.isChecked())
                schedule.addScheduleTime(Schedule.DayOfWeek.Monday, times);


            if (tuesdayCheckBox.isChecked())
                schedule.addScheduleTime(Schedule.DayOfWeek.Tuesday, times);


            if (wednesdayCheckBox.isChecked())
                schedule.addScheduleTime(Schedule.DayOfWeek.Wednesday, times);

            if (thursdayCheckBox.isChecked())
                schedule.addScheduleTime(Schedule.DayOfWeek.Thursday, times);


            if (fridayCheckBox.isChecked())
                schedule.addScheduleTime(Schedule.DayOfWeek.Friday, times);


            if (saturdayCheckBox.isChecked())
                schedule.addScheduleTime(Schedule.DayOfWeek.Saturday, times);


            if (sundayCheckBox.isChecked())
                schedule.addScheduleTime(Schedule.DayOfWeek.Sunday, times);

            //TODO: test for null exception
            Model.writeScheduleToDatabase(schedule, HomescreenActivity.currentUser);
            Toast.makeText(getContext(), "Saved Schedule!",
                    Toast.LENGTH_SHORT).show();
        } catch (NullPointerException ex) {
            Log.w(TAG, ex.getMessage());
        }

    }
}
