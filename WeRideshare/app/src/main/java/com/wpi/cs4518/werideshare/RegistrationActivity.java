package com.wpi.cs4518.werideshare;

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

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadSpinnerCapacity();
        setUpVehicleRegistration();
    }

    private void loadSpinnerCapacity() {
        Spinner spinner = (Spinner) findViewById(R.id.capacitySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.VehicleCapacity, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void setUpVehicleRegistration() {
        final RadioButton driverRadioButton = (RadioButton) findViewById(R.id.driverRadioButton);
        final RadioButton riderRadioButton = (RadioButton) findViewById(R.id.riderRadioButton);
        final Fragment vehicleFragment = getSupportFragmentManager().findFragmentById(R.id.vehicleDetailsFragment);

        driverRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(android.R.animator.fade_in,
//                        android.R.animator.fade_out);
                if (vehicleFragment.isHidden()) {
                    ft.show(vehicleFragment);
                }
                ft.commit();
            }
        });

        riderRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(android.R.animator.fade_in,
//                        android.R.animator.fade_out);
                if (vehicleFragment.isVisible())
                    ft.hide(vehicleFragment);
                ft.commit();
            }
        });

        riderRadioButton.callOnClick();
        riderRadioButton.setSelected(false);
    }
}
