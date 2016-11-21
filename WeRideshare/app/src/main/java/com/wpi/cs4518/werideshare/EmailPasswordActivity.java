package com.wpi.cs4518.werideshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wpi.cs4518.werideshare.model.Model;
import com.wpi.cs4518.werideshare.model.User;

import java.util.regex.Pattern;

public class EmailPasswordActivity extends BaseActivity implements
        View.OnClickListener {


    private static final String TAG = "EmailPassword";
    public static String userId = null;

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUp();
    }

    private void setUp() {
        FirebaseApp app = FirebaseApp.initializeApp(getApplicationContext());

        // Views
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
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
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
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
                        }else{
                            startActivity(new Intent(EmailPasswordActivity.this, RegistrationActivity.class));
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    private boolean validateForm() {
        boolean validEmail = true, validPassword = true;
        TextView formStatus = (TextView) findViewById(R.id.form_status);

        String email = mEmailField.getText().toString();
        if(!Pattern.matches(Constants.EMAIL_PATTERN, email)){
            validEmail = false;

            if(formStatus != null)
                formStatus.setText(String.format("%s\n", R.string.error_invalid_email));
        }

        String password = mPasswordField.getText().toString();
        if(!Pattern.matches(Constants.PASSWORD_PATTERN, password)){
            validPassword = false;

            if(formStatus != null){
                if(!validEmail)
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_sign_in_button)
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

        // [START sign_in_with_email]
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
                            if(task.getException() instanceof FirebaseAuthInvalidUserException)
                                createAccount(email, password);
                            else
                                Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                        }else{//login success
                            startActivity(new Intent(EmailPasswordActivity.this, ProfileActivity.class));
                            Log.w(TAG, String.format("token: %s\n", FirebaseInstanceId.getInstance().getToken()) );
                            Model.currentUser = Model.getUser(mAuth.getCurrentUser().getUid());
                            if(Model.currentUser == null)
                                Model.currentUser = Model.getDummyUser(mAuth.getCurrentUser().getUid());
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }


}
