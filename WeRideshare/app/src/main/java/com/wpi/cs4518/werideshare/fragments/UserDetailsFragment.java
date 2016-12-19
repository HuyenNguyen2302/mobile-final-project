package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.User.UserType;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailsFragment extends Fragment {
    private String firstName, lastName, phone;
    private UserType userType;

    public UserDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_personal_details, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle SavedInstanceState){
        super.onViewCreated(view, SavedInstanceState);
        EditText phoneNumberTextEdit = (EditText) getView().findViewById(R.id.phone_number);
        phoneNumberTextEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());    }


}
