package com.example.hatemragap.faculty_science_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_Activity extends AppCompatActivity {

    private LoginButton loginButton;

    private CallbackManager mCallbackManager;
    private DatabaseReference data_reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // firebase authentication
        mAuth = FirebaseAuth.getInstance();
        data_reference = FirebaseDatabase.getInstance().getReference().child("users");

        // facebook call back manager
        mCallbackManager = CallbackManager.Factory.create();

        // login button
        loginButton = findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setEnabled(false);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login_Activity.this, "Sorry but you must " +
                        "accept the conditions to continue", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login_Activity.this, "Error in login facebook " +
                        ", please check you account then try again \n" +
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        // make firebase user
        super.onStart();
        FirebaseUser mu = FirebaseAuth.getInstance().getCurrentUser();
        if (mu != null) {
            updateUI(mu);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {

                                // insert user to firebase
                                User user = new User(firebaseUser);
                                data_reference.child(firebaseUser.getUid()).setValue(user);

                                // inform user
                                Toast.makeText(Login_Activity.this,
                                        "Logged in successful", Toast.LENGTH_SHORT).show();

                                updateUI(firebaseUser);
                                return;
                            }
                        }
                        Log.e("facebook", task.getException().getMessage());
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {

        startActivity(
                new Intent(Login_Activity.this, MainActivity.class)
                        .putExtra("user", new User(firebaseUser))
        );
        finish();
    }

}
