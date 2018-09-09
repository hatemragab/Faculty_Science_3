package com.example.hatemragap.faculty_science_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

     DatabaseReference data_reference;
     FirebaseAuth mAuth;
     EditText ema,pass;
     Button singIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // firebase authentication
        ema = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        singIn = findViewById(R.id.btnSginIn);
        mAuth = FirebaseAuth.getInstance();
        data_reference = FirebaseDatabase.getInstance().getReference().child("users");


        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String email=  ema.getText().toString().trim();
               String password=  pass.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            moveTMAinActivity();
                            finish();
                        }
                        else {
                            Toast.makeText(Login_Activity.this, "emai and pass wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });



    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser mu = FirebaseAuth.getInstance().getCurrentUser();
        if (mu != null) {
            moveTMAinActivity();
        }


    }


//    private void handleFacebookAccessToken(AccessToken token) {
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                            if (firebaseUser != null) {
//
//                                // insert user to firebase
//
//                                data_reference.child(firebaseUser.getUid()).setValue(user);
//
//                                // inform user
//                                Toast.makeText(Login_Activity.this,
//                                        "Logged in successful", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                        Log.e("facebook", task.getException().getMessage());
//                    }
//                });
//    }

    private void moveTMAinActivity() {

        startActivity(new Intent(Login_Activity.this, MainActivity.class));

    }

    public void needNewAccount(View view) {

        startActivity(new Intent(this, Register.class));
    }
}
