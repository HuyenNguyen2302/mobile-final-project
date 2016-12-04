package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.wpi.cs4518.werideshare.model.Model;

import java.util.regex.Pattern;

public class EmailPasswordActivity extends BaseActivity {


    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

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

        signInButton = (Button) findViewById(R.id.signInButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });


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

    private void signIn(final String email, final String password) {
        Log.d(TAG, "signIn: " + email);
        if (!validateForm(email,password)) {

            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        hideProgressDialog();

                        // If sign in fails, display a message to the currentUser. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in currentUser can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {//login success
                            startActivity(new Intent(EmailPasswordActivity.this, HomescreenActivity.class));
                            String userId = mAuth.getCurrentUser().getUid();
                            Model.getUser(userId);
                            if(Model.currentUser == null)
                                Model.setCurrentUser(Model.getDummyUser(userId));
//                            Model.currentUser.setDeviceId(FirebaseInstanceId.getInstance().getToken());
                        }
                        // [END_EXCLUDE]
                    }
                });
    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email,password)) {
            Toast.makeText(EmailPasswordActivity.this, R.string.error_invalid_email,
                    Toast.LENGTH_SHORT).show();
            return;
        }

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
                        Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(EmailPasswordActivity.this, RegistrationActivity.class));
                    }
                    hideProgressDialog();
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
            Toast.makeText(EmailPasswordActivity.this, "Email and Password cannot be blank.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        //email pattern requires an '@' and '.com'
        if (!Pattern.matches(Constants.EMAIL_PATTERN, email)) {
            Toast.makeText(EmailPasswordActivity.this, R.string.error_invalid_email,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6 ) {
            Toast.makeText(EmailPasswordActivity.this, "Password should be at least 6 characters.",
                    Toast.LENGTH_SHORT).show();
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
