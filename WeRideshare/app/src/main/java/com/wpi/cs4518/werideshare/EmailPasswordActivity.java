package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpi.cs4518.werideshare.model.Model;
import com.wpi.cs4518.werideshare.model.User;

import java.util.regex.Pattern;

import static com.wpi.cs4518.werideshare.model.Model.USER_ROOT;

public class EmailPasswordActivity extends BaseActivity {


    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView formStatus;

    //firebase fields
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button signInButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        setUp();
    }

    private void setUp() {
        // Views
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        formStatus = (TextView) findViewById(R.id.form_status);

        //set up persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    updateUI();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email,password))
            return;

        Intent registrationIntent = new Intent(EmailPasswordActivity.this, RegistrationActivity.class);
        registrationIntent.putExtra("email", email);
        registrationIntent.putExtra("password", password);
        startActivity(registrationIntent);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
    }

    public void onClickRegister(View v) {
        String email = mEmailField.getText().toString();
        String password =  mPasswordField.getText().toString();
        Log.w(TAG, "some email: " + email);
        if(validateForm(email, password))
            createAccount(email, password);
    }

    public void onClickSignIn(View v) {
        String email = mEmailField.getText().toString();
        String password =  mPasswordField.getText().toString();
        if(validateForm(email, password))
            signIn(email, password);
    }

    private void signIn(final String email, final String password) {
        Log.d(TAG, "signIn: " + email);
        if (!validateForm(email,password))
            return;
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the currentUser. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in currentUser can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        } else {//login success
                            //retrieve user and pass to home activity
                            formStatus.setText("Login success. Retrieving user...");
                            getCurrentUser(mAuth.getCurrentUser().getUid());
                        }
                        // [END_EXCLUDE]
                    }
                });
    }

    private void getCurrentUser(final String userId) {
        FirebaseDatabase.getInstance().getReference()
                .child(USER_ROOT)
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                Log.w(TAG, "Retrieved user: " + currentUser.getUsername());

                //finally hide progress dialog
                hideProgressDialog();

                //start home activity
                Intent homeIntent = new Intent(EmailPasswordActivity.this, HomescreenActivity.class);
                homeIntent.putExtra("userId", userId);
                homeIntent.putExtra("user", currentUser);
                startActivity(homeIntent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Check to see the registration form for valid input
     * includes checking if email matches with '@'
     *
     * @return true if the form is all good, false if there were errors such as a password not being longer than 6 chars
     */
    private boolean validateForm(String email, String password) {

        //email or password is "" if user left the field empty
        if (email.equals("") || password.equals("")) {
            if (formStatus != null)
                formStatus.setText(String.format("%s\n", "Email and/or pass cannot be blank"));
            return false;
        }
        //email pattern requires an '@' and '.com'
        if (!Pattern.matches(Constants.EMAIL_PATTERN, email)) {
            if (formStatus != null)
                formStatus.setText(String.format("%s\n", "Email should have '@.com'"));
            return false;
        }
        if (password.length() < 6 ) {
            if (formStatus != null)
                formStatus.setText(String.format("%s\n", "Password should be at least 6 chars'"));
            return false;
        }

        /**
         * Purposefully commented out to remove password constraint
        if (!Pattern.matches(Constants.PASSWORD_PATTERN, password))
           return false;
         */

        return true;
    }

    private void updateUI() {
        hideProgressDialog();
    }




}
