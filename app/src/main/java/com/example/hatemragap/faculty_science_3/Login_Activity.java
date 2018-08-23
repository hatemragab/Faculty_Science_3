package com.example.hatemragap.faculty_science_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
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
    CallbackManager mCallbackManager;
    LoginButton loginButton;
    private FirebaseAuth mAuth;
    DatabaseReference data_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

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
                Toast.makeText(Login_Activity.this, "you must accept to continue", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login_Activity.this, "you have error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        // make firebase user
        FirebaseUser mu = FirebaseAuth.getInstance().getCurrentUser();
        if (mu != null) {
            startActivity(new Intent(Login_Activity.this, MainActivity.class));
            finish();
        }
        super.onStart();


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
                            FirebaseUser user = mAuth.getCurrentUser();
                            Users users = new Users();
                            users.setEmail(user.getEmail());
                            users.setName(user.getDisplayName());
                            users.setId(user.getUid());
                            users.setImgUrl(user.getPhotoUrl().toString());
                            data_reference.child(user.getUid()).setValue(users);
                            updateUI(user);
                        }


                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(Login_Activity.this, MainActivity.class).putExtra("name", user.getDisplayName()));
        finish();
    }

}
