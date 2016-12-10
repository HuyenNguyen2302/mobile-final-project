package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpi.cs4518.werideshare.HomescreenActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.Car;
import com.wpi.cs4518.werideshare.model.Model;
import com.wpi.cs4518.werideshare.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarProfileFragment extends Fragment {


    public CarProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_profile, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();

        try {
            User currentUser = ((HomescreenActivity) getActivity()).currentUser;
            DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

            firebase.child(Model.CARS_ROOT)
                    .child(currentUser.getUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Car car = dataSnapshot.getValue(Car.class);
                            TextView model = (TextView) getActivity().findViewById(R.id.model_text);
                            TextView registration = (TextView) getActivity().findViewById(R.id.reg_text);
                            TextView capacity = (TextView) getActivity().findViewById(R.id.capacity);

                            model.append(" " + car.getCarName());
                            registration.append(" " + car.getRegistrationId());
                            capacity.append(" " + car.getCapacity());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }catch (NullPointerException ex){
            Log.w("CAR_PROFILE", ex.getMessage());
        }
    }

}
