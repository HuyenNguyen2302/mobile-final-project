package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wpi.cs4518.werideshare.fragments.CarDetailsFragment;
import com.wpi.cs4518.werideshare.fragments.UserDetailsFragment;
import com.wpi.cs4518.werideshare.model.Model;
import com.wpi.cs4518.werideshare.model.User;
import com.wpi.cs4518.werideshare.model.Car;


/**
 * This class implements the registration logic.
 * It uses two fragments (UserDetailsFragment and CarDetailsFragment) to separate the
 * registration of car details from user details
 *
 */
public class RegistrationActivity extends BaseActivity {

    private static final String TAG = "REG";
    private static final int PERSONAL = 1;
    private static final int CAR = 2;
    private static FirebaseAuth mAuth;

    private User newUser;
    private String email;
    private String password;
    private int currentPage;
    private boolean userCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_registration);

        if (getIntent() != null) {
            email = String.valueOf(getIntent().getStringExtra("email"));
            password = String.valueOf(getIntent().getStringExtra("password"));
            Log.w(TAG, "email: " + email);
        }
        addUserDetails();
    }

    private void addUserDetails() {
        addFragment(new UserDetailsFragment());
        currentPage = PERSONAL;


    }

    private void addVehicleDetails() {
        addFragment(new CarDetailsFragment());
        currentPage = CAR;
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.task_container, fragment); // f1_container is FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onClickProceedButton(View view) {

        getCurrentPage();
        if (currentPage == PERSONAL) {
            if(!validatePersonalPage()) //validate current page
                return;
            createAccount();
        } else if (currentPage == CAR) {
            if(!validateCarPage()) //validate current page
                return;
            //get vehicle fields from the form
            EditText carNameText = (EditText) findViewById(R.id.car_name);
            EditText registrationIdText = (EditText) findViewById(R.id.registration_id);
            Spinner capacitySpinner = (Spinner) findViewById(R.id.capacitySpinner);

            //create a new vehicle to assign to this driver

            Car newCar = new Car(carNameText.getText().toString(),
                    registrationIdText.getText().toString(), Integer.parseInt(String.valueOf(capacitySpinner.getSelectedItem())),
                    newUser.getUserId());
            Model.writeCarToDatabase(newCar);
            doNext();
        }
    }

    private boolean validatePersonalPage () {
        String firstName = ((EditText) findViewById(R.id.first_name)).getText().toString();
        if("".equals(firstName)) {
            Toast.makeText(this, "Enter in a first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        String lastName = ((EditText) findViewById(R.id.last_name)).getText().toString();
        if("".equals(lastName)) {
            Toast.makeText(this, "Enter in a last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phoneNum  = ((EditText) findViewById(R.id.phone_number)).getText().toString();
        if (phoneNum.length() != 14){ //14 because PhoneNumberFormattingTextWatcher adds spaces and ( and -
            Toast.makeText(this, "10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;

        }
        if(((RadioGroup)findViewById(R.id.radioButtonGroup)).getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Select rider or driver", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validateCarPage(){
        String carNameText = ((EditText) findViewById(R.id.car_name)).getText().toString();
        if("".equals(carNameText)){
            Toast.makeText(this, "Enter in a car name", Toast.LENGTH_SHORT).show();
            return false;
        }
        String registrationIdText = ((EditText) findViewById(R.id.registration_id)).getText().toString();
        if("".equals(registrationIdText)){
            Toast.makeText(this, "Enter in a registration id", Toast.LENGTH_SHORT).show();
            return false;
        }
        String capacitySpinner = ((Spinner) findViewById(R.id.capacitySpinner)).getSelectedItem().toString();
        if("(Vehicle Capacity)".equals(capacitySpinner)){
            Toast.makeText(this, "Enter in a vehicle capacity", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void getCurrentPage(){
        //check if current page is
        boolean personal = findViewById(R.id.first_name) != null;

        if(personal)
            currentPage = PERSONAL;
        else
            currentPage = CAR;
    }

    private void createUser(String userId) {

        //get user fields from form
        RadioButton driverButton = (RadioButton) findViewById(R.id.driverRadioButton);
        EditText firstNameText = (EditText) findViewById(R.id.first_name);
        EditText lastNameText = (EditText) findViewById(R.id.last_name);
        User.UserType userType = driverButton.isChecked() ? User.UserType.Driver : User.UserType.Rider;

        //create new user with given fields
        newUser = new User(userId, firstNameText.getText().toString(),
                lastNameText.getText().toString(), userType, FirebaseInstanceId.getInstance().getToken());
        Model.writeUserToDatabase(newUser);
    }

    /*
     * Perform the neccesary steps to add this user to Firebase Authentication.
     * Return the userId provided by Firebase
     */
    private void createAccount() {
        //skip account creation if user already created
        if(userCreated) {
            doNext();
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        Log.w(TAG, String.format("Creating account: email: %s, password: %s", email, password));
        if (email == null || password == null)
            return ;
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the currentUser. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in currentUser can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            userCreated = false;
                            hideProgressDialog();
                        } else {
                            String userId = mAuth.getCurrentUser().getUid();
                            Log.w(TAG, "userId: " + userId);

                            createUser(userId);
                            userCreated = true;

                            doNext();
                            hideProgressDialog();
                        }
                    }
                });
    }

    private void doNext() {
        RadioButton driverButton = (RadioButton) findViewById(R.id.driverRadioButton);

        //open the vehicle registration page if this is a driver
        if (currentPage == PERSONAL && driverButton.isChecked()) {
            addVehicleDetails();
            return ;
        }
        try {
            //start profile intent and display message if registration successful
            Intent profileIntent = new Intent(RegistrationActivity.this, HomescreenActivity.class);
            profileIntent.putExtra("user", newUser);
            profileIntent.putExtra("userId", newUser.getUserId());
            Toast.makeText(this, R.string.reg_success,
                    Toast.LENGTH_SHORT).show();

            startActivity(profileIntent);
        }catch (NullPointerException ex){
            Log.w(TAG, ex.getMessage());
        }
    }
}
