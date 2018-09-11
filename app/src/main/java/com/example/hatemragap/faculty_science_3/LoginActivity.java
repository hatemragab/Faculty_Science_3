package com.example.hatemragap.faculty_science_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference usersReference;
    FirebaseAuth auth;
    EditText emailTxt, passwordTxt;
    Button singIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.activity_login);
        emailTxt = findViewById(R.id.emailEdtTxt);
        passwordTxt = findViewById(R.id.passwordEdtTxt);
        singIn = findViewById(R.id.signInBtn);

        // firebase authentication
        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");


        singIn.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            moveToMainActivity();
        }
    }

    private void moveToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

    }

    public void needNewAccount(View view) {
        startActivity(new Intent(this, Register.class));
    }

    @Override
    public void onClick(View v) {

        // get email and password from views
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty())
            // sign in using them
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                moveToMainActivity();
                                finish();
                            }
                        }
                    });
        // wrong email or password
        Toast.makeText(LoginActivity.this, "email or password are wrong", Toast.LENGTH_SHORT)
                .show();
    }
}
