package com.example.hatemragap.faculty_science_3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    DatabaseReference data_reference;
    FirebaseAuth mAuth;
    Button signUp;
    EditText na, ema, pass;
    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUp = findViewById(R.id.BtnSignUp);
        na = findViewById(R.id.nameUp);
        ema = findViewById(R.id.emailUp);
        pass = findViewById(R.id.passwordUp);
        mAuth = FirebaseAuth.getInstance();

        data_reference = FirebaseDatabase.getInstance().getReference().child("users");
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final User user = new User();
                String name = na.getText().toString().trim();
                String email = ema.getText().toString().trim();
                user.setEmail(email);
                user.setName(name);
                user.setToken(FirebaseInstanceId.getInstance().getToken());

                String password = pass.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            myId = mAuth.getCurrentUser().getUid();
                            user.setId(myId);
                            data_reference.child(myId).setValue(user);

                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(Register.this, "email  is exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}
