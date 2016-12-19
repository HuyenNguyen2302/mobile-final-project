package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wpi.cs4518.werideshare.HomescreenActivity;
import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();

        try {
            User currentUser = ((HomescreenActivity) getActivity()).currentUser;
            System.out.printf("current user: %s", currentUser);

            //get controls
            TextView firstnameText = (TextView) getActivity().findViewById(R.id.first_name);
            TextView lastnameText = (TextView) getActivity().findViewById(R.id.last_name);
            TextView usernameText = (TextView) getActivity().findViewById(R.id.username_text);
            //TextView phoneNumberText = (TextView) getActivity().findViewById(R.id.phone_numberTextView);

            firstnameText.setText(currentUser.getFirstName().toLowerCase());
            lastnameText.setText(currentUser.getLastName().toUpperCase());
            usernameText.setText(currentUser.getUsername());
            //phoneNumberText.setText(currentUser.getPhoneNumber());

            //show car details if driver, otherwise return
            if(currentUser.getUserType().equals(User.UserType.Rider))
                return;

            CarProfileFragment carFragment = new CarProfileFragment();
            addFragment(carFragment);

        }catch(NullPointerException ex){
            String TAG = "PROFILE";
            Log.w(TAG, ex.getMessage());
        }
    }


    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.car_container, fragment); // f1_container is FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

}
