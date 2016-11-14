package com.wpi.cs4518.werideshare.layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wpi.cs4518.werideshare.R;
import com.wpi.cs4518.werideshare.model.User.UserType;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetails extends Fragment {
    private String firstName, lastName, phone;
    private UserType userType;

    public PersonalDetails() {
    }

    /**
     * Save the details of this fragment
     */
    public void saveDetails(){
        try{
            Bundle arguments = new Bundle();
            arguments.putString("firstName", firstName);
            arguments.putString("lastName", lastName);
            arguments.putString("phone", phone);
            arguments.putString("userType", userType.toString());
            setArguments(arguments);
        }catch (NullPointerException ex){
            System.out.printf("Null exception.\n%s\n", ex.getMessage());
        }
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Get the value of the firstName EditText
     * @return first name
     */
    public String getFirstName(){
        EditText firstNameView = (EditText) getView().findViewById(R.id.first_name);
        return firstNameView == null? null : firstNameView.getText().toString();
    }

    /**
     * Get the user type from the selected radio button
     * @return
     */
    public UserType getUserType(){
        RadioGroup buttonGroup = (RadioGroup) getView().findViewById(R.id.radioButtonGroup);
        if(buttonGroup == null)
            return null;
        int selectedId = buttonGroup.getCheckedRadioButtonId();
        RadioButton button = (RadioButton) getView().findViewById(selectedId);
        if(button == null)
            return null;
        if(button.getId() == R.id.driverRadioButton){
            //process for driver
            return UserType.Driver;
        }else{
            //process for rider
            return UserType.Rider;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_details, container, false);
    }

}
