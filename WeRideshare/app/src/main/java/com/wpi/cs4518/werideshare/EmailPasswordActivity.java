package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.wpi.cs4518.werideshare.map.MapsActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wpi.cs4518.werideshare.model.Model;

import java.util.regex.Pattern;

public class EmailPasswordActivity extends BaseActivity {


    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    //fiirebase fields
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
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

    @Override
    public void onSaveInstanceState(Bundle state){
        
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm())
            return;

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

    private boolean validateForm() {
        boolean validEmail = true, validPassword = true;
        TextView formStatus = (TextView) findViewById(R.id.form_status);

        String email = mEmailField.getText().toString();
        if (!Pattern.matches(Constants.EMAIL_PATTERN, email)) {
            validEmail = false;

            if (formStatus != null)
                formStatus.setText(String.format("%s\n", R.string.error_invalid_email));
        }

        String password = mPasswordField.getText().toString();
        if (!Pattern.matches(Constants.PASSWORD_PATTERN, password)) {
            validPassword = false;

            if (formStatus != null) {
                if (!validEmail)
                    formStatus.setText(String.format("%s %s\n", formStatus.getText(), R.string.error_invalid_password));
                else
                    formStatus.setText(String.format("%s\n", R.string.error_invalid_password));
            }
        }
//        return validEmail && validPassword;
        return password != null && email != null;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
    }

    public void onClickRegister(View v) {
        createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    public void onClickSignIn(View v) {
        signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    private void signIn(final String email, final String password) {
        Log.d(TAG, "signIn: " + email);
        if (!validateForm()) {
            Toast.makeText(EmailPasswordActivity.this, R.string.form_status_invalid,
                    Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(EmailPasswordActivity.this, ProfileActivity.class));

                            Model.currentUser = Model.getUser(mAuth.getCurrentUser().getUid());
                            if (Model.currentUser == null)
                                Model.currentUser = Model.getDummyUser(mAuth.getCurrentUser().getUid());
                            Model.currentUser.setDeviceId(FirebaseInstanceId.getInstance().getToken());
                            System.out.printf("fcm token: %s\n", FirebaseInstanceId.getInstance().getToken());
                        }
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }


}
