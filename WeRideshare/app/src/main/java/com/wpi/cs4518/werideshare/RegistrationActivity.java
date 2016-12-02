package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.wpi.cs4518.werideshare.fragments.PersonalDetailsFragment;
import com.wpi.cs4518.werideshare.fragments.VehicleDetailsFragment;

public class RegistrationActivity extends AppCompatActivity {
    private PersonalDetailsFragment personalDetailsFragment;
    private VehicleDetailsFragment vehicleDetailsFragment;

    private static final int PERSONAL = 1;
    private static final int VEHICLE = 2;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //add personal details fragment
        addPersonalDetails();
    }

    private void addPersonalDetails() {
        if (personalDetailsFragment == null)
            personalDetailsFragment = new PersonalDetailsFragment();
        addFragment(personalDetailsFragment);
        currentPage = PERSONAL;
    }

    private void addVehicleDetails() {
        if (vehicleDetailsFragment == null)
            vehicleDetailsFragment = new VehicleDetailsFragment();
        addFragment(vehicleDetailsFragment);
        currentPage = VEHICLE;
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, fragment); // f1_container is FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickProceedButton(View view) {
        RadioButton driverButton = (RadioButton) findViewById(R.id.driverRadioButton);
        if (currentPage == VEHICLE){
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        if (driverButton.isChecked() && currentPage == PERSONAL)
            addVehicleDetails();
    }
}
